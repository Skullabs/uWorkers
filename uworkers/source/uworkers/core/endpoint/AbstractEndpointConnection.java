package uworkers.core.endpoint;

import java.io.IOException;

import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.IllegalStateException;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.java.Log;
import uworkers.api.EndpointConnection;
import uworkers.utils.Fibonacci;

import com.fasterxml.jackson.databind.ObjectMapper;

@Log
@Getter
@Accessors(fluent = true)
public abstract class AbstractEndpointConnection<T extends Session> implements ExceptionListener, EndpointConnection {

	private static final int TIME_TO_WAIT_BEFORE_START_LOGGING_ERROR = 10;
	private static final int LESS_THAN_A_MINUTE = 50;
	private static final int SECONDS = 1000;

	private final ObjectMapper mapper = new ObjectMapper();
	boolean shouldSerializeBeforeSendOrReceiveObjects = true;
	boolean isListeningMessages = false;

	MessageConsumer consumer;
	MessageProducer producer;
	javax.jms.Connection connection;
	T currentSession;

	public MessageConsumer consumer() throws JMSException {
		ensureSessionStarted();
		if (consumer == null)
			synchronized (this) {
				if (consumer == null)
					consumer = createMessageConsumer();
			}
		return consumer;
	}

	public MessageProducer producer() throws JMSException {
		ensureSessionStarted();
		if (producer == null)
			synchronized (this) {
				if (producer == null)
					producer = createMessageProducer();
			}
		return producer;
	}

	void ensureSessionStarted() throws JMSException {
		if (currentSession == null)
			throw new IllegalStateException("Connection not configured/started.");
	}

	@Override
	public void onException(JMSException exception) {
		try {
			log.severe("Failed to communicate with broker: " + exception.getMessage());
			exception.printStackTrace();
			setupEndpoint();
		} catch (InterruptedException cause) {
			cause.printStackTrace();
		}
	}

	@Override
	public void start() throws InterruptedException, JMSException {
		if (connection == null)
			setupEndpoint();
	}

	@Override
	public void startAndListenMessages() throws InterruptedException, JMSException {
		start();
		this.connection.start();
		isListeningMessages = true;
		log.info(this.toString() + " started to receive messages.");
	}

	public void setupEndpoint() throws InterruptedException {
		while (true)
			for (Integer seconds : Fibonacci.until(LESS_THAN_A_MINUTE))
				try {
					tryConnect();
					return;
				} catch (JMSException e) {
					log.info(e.getMessage());
					sleep(seconds);
				}
	}

	protected void sleep(Integer seconds) throws InterruptedException {
		if (seconds > TIME_TO_WAIT_BEFORE_START_LOGGING_ERROR)
			log.warning("Could estabilish connection to broker. Trying again in " + seconds + " seconds.");
		Thread.sleep(seconds * SECONDS);
	}

	protected void tryConnect() throws JMSException {
		stop();
		connect();
		if (isListeningMessages)
			this.connection.start();
		log.info(this.toString() + " connected to " + connection);
	}

	@Override
	public void connect() throws JMSException {
		Connection<T> connection = createConnection();
		this.connection = connection.connection();
		this.connection.setExceptionListener(this);
		this.currentSession = connection.session();
	}

	protected abstract Connection<T> createConnection() throws JMSException;

	@Override
	public void stop() {
		closeSession();
		closeConnection();
		consumer = null;
		producer = null;
	}

	void closeSession() {
		try {
			if (currentSession != null)
				currentSession.close();
			currentSession = null;
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	void closeConnection() {
		try {
			if (connection != null)
				connection.close();
			connection = null;
		} catch (JMSException cause) {
			log.severe(cause.getMessage());
		}
	}

	@Override
	public void send(Object object) throws JMSException, IOException {
		TextMessage message = currentSession.createTextMessage();
		String jsonString = serialize(object);
		message.setText(jsonString);
		producer().send(message);
	}

	public String serialize(Object object) throws IOException {
		try {
			if (object == null)
				return null;
			if (!shouldSerializeBeforeSendOrReceiveObjects)
				return object.toString();
			return mapper.writeValueAsString(object);
		} catch (IOException cause) {
			throw new IOException(cause);
		}
	}

	@Override
	public <V> V receive(Class<V> target) throws JMSException, IOException {
		TextMessage received = (TextMessage) consumer().receive();
		String jsonString = received.getText();
		V unserialize = unserialize(jsonString, target);
		return unserialize;
	}

	@Override
	public <V> AcknowledgeableResponse<V> receiveAcknowledgeableResponse(Class<V> target) throws JMSException, IOException {
		TextMessage received = (TextMessage) consumer().receive();
		String jsonString = received.getText();
		V unserialize = unserialize(jsonString, target);
		return new AcknowledgeableResponse<>(unserialize, received);
	}

	@SuppressWarnings("unchecked")
	public <V> V unserialize(String input, Class<V> targetClass) throws IOException {
		try {
			if (!shouldSerializeBeforeSendOrReceiveObjects)
				return (V) input;
			return mapper.readValue(input, targetClass);
		} catch (IOException cause) {
			throw new IOException(cause);
		}
	}

	abstract MessageConsumer createMessageConsumer();

	MessageProducer createMessageProducer() {
		try {
			MessageProducer producer = currentSession().createProducer(destination());
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			return producer;
		} catch (JMSException cause) {
			throw new RuntimeException(cause);
		}
	}

	protected abstract Destination destination();

	@Override
	public boolean shouldSerializeBeforeSendOrReceiveObjects() {
		return shouldSerializeBeforeSendOrReceiveObjects;
	}

	@Override
	public void shouldSerializeBeforeSendOrReceiveObjects(boolean shouldSerializeBeforeSendOrReceiveObjects) {
		this.shouldSerializeBeforeSendOrReceiveObjects = shouldSerializeBeforeSendOrReceiveObjects;
	}

	@Override
	public String toString() {
		return "Endpoint( " + destination() + " )";
	}
}
