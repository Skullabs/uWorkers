package uworkers.core.endpoint;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueSession;
import javax.jms.Session;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public class WorkerEndpointConnection extends AbstractEndpointConnection<QueueSession> {

	@NonNull
	String endpointName;

	final MQProvider mqProvider;

	Queue destination;

	@Override
	public Connection<QueueSession> createConnection() throws JMSException {
		final QueueConnection connection = mqProvider().createWorkerConnection();
		final QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
		destination = session.createQueue(endpointName);
		return new Connection<QueueSession>(connection, session);
	}

	@Override
	MessageConsumer createMessageConsumer() {
		try {
			return currentSession().createConsumer(destination());
		} catch (final JMSException cause) {
			throw new RuntimeException(cause);
		}
	}
}
