package uworkers.core.endpoint;

import java.io.Serializable;

import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.java.Log;
import uworkers.api.Endpoint;
import uworkers.utils.Fibonacci;

@Log
@Getter
@Accessors( fluent = true )
public abstract class AbstractEndpoint implements ExceptionListener, Endpoint {

	private static final int TIME_TO_WAIT_BEFORE_START_LOGGING_ERROR = 10;
	private static final int LESS_THAN_A_MINUTE = 50;
	private static final int SECONDS = 1000;

	javax.jms.Connection connection;
	Session currentSession;

	@Override
	public void onException( JMSException exception ) {
		try {
			log.severe( "Failed to communicate with broker: " + exception.getMessage() );
			exception.printStackTrace();
			setupEndpoint();
		} catch ( InterruptedException cause ) {
			cause.printStackTrace();
		}
	}

	@Override
	public void start() throws InterruptedException, JMSException {
		setupEndpoint();
		this.connection.start();
	}

	public void setupEndpoint() throws InterruptedException {
		while ( true )
			for ( Integer seconds : Fibonacci.until( LESS_THAN_A_MINUTE ) )
				try {
					tryConnect();
					return;
				} catch ( JMSException e ) {
					log.info( e.getMessage() );
					sleep( seconds );
				}
	}

	protected void sleep( Integer seconds ) throws InterruptedException {
		if ( seconds > TIME_TO_WAIT_BEFORE_START_LOGGING_ERROR )
			log.warning( "Could estabilish connection to broker. Trying again in " + seconds + " seconds." );
		Thread.sleep( seconds * SECONDS );
	}

	protected void tryConnect() throws JMSException {
		stop();
		connect();
		log.fine( this.toString() + " connected to " + connection );
	}

	@Override
	public void connect() throws JMSException {
		Connection connection = createConnection();
		this.connection = connection.connection();
		this.connection.setExceptionListener( this );
		this.currentSession = connection.session();
	}

	protected abstract Connection createConnection() throws JMSException;

	@Override
	public void stop() {
		try {
			if ( connection != null )
				connection.close();
		} catch ( JMSException cause ) {
			log.severe( cause.getMessage() );
		}
	}

	@Override
	public void send( Serializable object ) throws JMSException {
		ObjectMessage message = currentSession.createObjectMessage();
		message.setObject( object );
		send( message );
	}

	public void send( ObjectMessage message ) throws JMSException {
		producer().send( message );
	}

	protected abstract MessageProducer producer();

	@Override
	@SuppressWarnings( "unchecked" )
	public <T extends Serializable> T receive( Class<T> target ) throws JMSException {
		return (T)receive();
	}

	@Override
	public Object receive() throws JMSException {
		ObjectMessage received = (ObjectMessage)consumer().receive();
		return received.getObject();
	}

	protected abstract MessageConsumer consumer();
}
