package uworkers.core.endpoint;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicSession;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors( fluent = true )
@RequiredArgsConstructor
public class SubscriberEndpoint extends AbstractEndpoint {

	final String endpointName;
	final MQProvider mqProvider;
	MessageConsumer consumer;
	MessageProducer producer;

	@Override
	public Connection createConnection() throws JMSException {
		TopicConnection connection = mqProvider().createSubscriptionConnection();
		TopicSession session = connection.createTopicSession( false, Session.AUTO_ACKNOWLEDGE );
		Topic destination = session.createTopic( endpointName );
		consumer = session.createConsumer( destination );
		producer = session.createProducer( destination );
		return new Connection( connection, session );
	}

}
