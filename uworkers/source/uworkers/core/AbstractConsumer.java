package uworkers.core;

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
		} catch ( InterruptedException cause ) {
			log.info( "Stopping consumer " + this );
		} catch ( Throwable cause ) {
			handleFailure( cause );
		} finally {
			endpoint().stop();
			log.info( "Stopped: " + this );
		}
	}

	@SuppressWarnings( "unchecked" )
	protected void receiveAndHandleMessage() throws InterruptedException {
		try {
			T receivedMessage = (T)endpoint().receive();
			handle( receivedMessage );
		} catch ( Throwable cause ) {
			handleFailure( cause );
		}
	}

	protected void handleFailure( Throwable cause ) {
		log.severe( cause.getMessage() );
		cause.printStackTrace();
	}
}
