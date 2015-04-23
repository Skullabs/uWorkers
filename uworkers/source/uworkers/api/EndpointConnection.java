package uworkers.api;

import java.io.IOException;

import javax.jms.JMSException;

import uworkers.core.endpoint.ParametrizedResponse;
import uworkers.core.endpoint.ParametrizedRequest;

public interface EndpointConnection {

	void start() throws InterruptedException, JMSException;

	void startAndListenMessages() throws InterruptedException, JMSException;

	void connect() throws JMSException;

	void stop();

	void send(Object object) throws JMSException, IOException;

	<T> T receive(Class<T> target) throws JMSException, IOException;

	boolean shouldSerializeBeforeSendOrReceiveObjects();

	void shouldSerializeBeforeSendOrReceiveObjects(boolean shouldSerializeBeforeSendOrReceiveObjects);

	<V> ParametrizedResponse<V> receiveParametrizedResponse(Class<V> target)
			throws JMSException, IOException;

	ParametrizedRequest sendWithParameters(Object object) throws JMSException, IOException;
}