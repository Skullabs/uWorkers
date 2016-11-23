package uworkers.core;

import java.io.IOException;

import javax.jms.JMSException;

import lombok.extern.java.Log;
import uworkers.api.Consumer;
import uworkers.api.EndpointConnection;
import uworkers.core.endpoint.ParametrizedResponse;
import uworkers.utils.Fibonacci;

@Log
public abstract class AbstractConsumer<T> implements Runnable, Consumer<T> {

	final Fibonacci waitingCounter = Fibonacci.until(120);

	protected abstract EndpointConnection endpoint();

	@Override
	public void run() {
		try {
			endpoint().startAndListenMessages();
			while (true)
				receiveAndHandleMessage();
		} catch (final InterruptedException cause) {
			log.info("Stopping consumer " + this);
		} catch (final Throwable cause) {
			handleFailure(cause);
			log.severe("Stopping consumer " + this);
		} finally {
			endpoint().stop();
			log.info("Stopped: " + this);
		}
	}

	protected void receiveAndHandleMessage() throws InterruptedException {
		try {
			final ParametrizedResponse<T> received = receive();
			handle(received);
			received.aknowledge();
			waitingCounter.reset();
		} catch (final JMSException cause) {
			handleJMSException(cause);
		} catch (final Throwable cause) {
			handleFailure(cause);
		}
	}

	public void handle(final ParametrizedResponse<T> received) throws Exception {
		final T receivedMessage = received.getResponse();
		handle(receivedMessage);
	}

	public abstract void handle( T receivedMessage ) throws Exception;

	private void handleJMSException(final JMSException cause) throws InterruptedException {
		final Integer next = waitingCounter.hasNext() ? waitingCounter.next() : waitingCounter.current();
		if (next > 5)
			handleFailure(cause);
		Thread.sleep(next * 1500);
	}

	protected ParametrizedResponse<T> receive() throws JMSException, IOException {
		return endpoint().receiveParametrizedResponse(getExpectedObjectClass());
	}

	public abstract Class<T> getExpectedObjectClass();

	protected void handleFailure(final Throwable cause) {
		log.severe(cause.getMessage());
		cause.printStackTrace();
	}
}
