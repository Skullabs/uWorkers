package uworkers.core;

import javax.jms.JMSException;

import lombok.extern.java.Log;
import uworkers.api.Consumer;
import uworkers.api.Endpoint;
import uworkers.api.WorkerException;

@Log
public abstract class AbstractConsumer<T> implements Runnable, Consumer<T> {

	protected abstract Endpoint endpoint();

	@Override
	public void run() {
		try {
			endpoint().start();
			while ( true )
				receiveAndHandleMessage();
		} catch ( InterruptedException cause ) {
			log.info( "Stopping consumer " + this );
		} catch ( JMSException cause ) {
			handleFailure( cause );
		} finally {
			endpoint().stop();
		}
	}

	@SuppressWarnings( "unchecked" )
	protected void receiveAndHandleMessage() throws InterruptedException {
		try {
			T receivedMessage = (T)endpoint().receive();
			handle( receivedMessage );
		} catch ( JMSException | WorkerException cause ) {
			handleFailure( cause );
		}

	}

	protected void handleFailure( Exception cause ) {
		log.severe( cause.getMessage() );
		cause.printStackTrace();
	}
}
