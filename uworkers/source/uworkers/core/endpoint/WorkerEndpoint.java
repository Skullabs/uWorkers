package uworkers.core.endpoint;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueSession;
import javax.jms.Session;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors( fluent = true )
@RequiredArgsConstructor
public class WorkerEndpoint extends AbstractEndpoint {

	final String endpointName;
	final MQProvider mqProvider;
	MessageConsumer consumer;
	MessageProducer producer;

	@Override
	public Connection createConnection() throws JMSException {
		QueueConnection connection = mqProvider().createWorkerConnection();
		QueueSession session = connection.createQueueSession( false, Session.AUTO_ACKNOWLEDGE );
		Queue destination = session.createQueue( endpointName );
		consumer = session.createConsumer( destination );
		producer = session.createProducer( destination );
		return new Connection( connection, session );
	}
}
