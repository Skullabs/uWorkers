package uworkers.core;

import java.io.IOException;

import javax.jms.JMSException;

import lombok.extern.java.Log;
import uworkers.api.Consumer;
import uworkers.api.EndpointConnection;

@Log
public abstract class AbstractConsumer<T> implements Runnable, Consumer<T> {

	protected abstract EndpointConnection endpoint();

	@Override
	public void run() {
		try {
			endpoint().startAndListenMessages();
			while ( true )
				receiveAndHandleMessage();
		} catch ( final InterruptedException cause ) {
			log.info( "Stopping consumer " + this );
		} catch ( final Throwable cause ) {
			handleFailure( cause );
		} finally {
			endpoint().stop();
			log.info( "Stopped: " + this );
		}
	}

	protected void receiveAndHandleMessage() throws InterruptedException {
		try {
			final T receivedMessage = receive();
			handle( receivedMessage );
		} catch ( final Throwable cause ) {
			handleFailure( cause );
		}
	}
	
	protected T receive() throws JMSException, IOException {
	  return endpoint().receive( getExpectedObjectClass() );
	}
	
	public abstract Class<T> getExpectedObjectClass();

	protected void handleFailure( final Throwable cause ) {
		log.severe( cause.getMessage() );
		cause.printStackTrace();
	}
}
