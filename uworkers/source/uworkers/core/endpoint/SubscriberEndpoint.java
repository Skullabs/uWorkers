package uworkers.core.endpoint;

import javax.jms.JMSException;
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
public class SubscriberEndpoint extends AbstractEndpoint<TopicSession> {

	final String endpointName;
	final MQProvider mqProvider;
	Topic destination;

	@Override
	public Connection<TopicSession> createConnection() throws JMSException {
		TopicConnection connection = mqProvider().createSubscriptionConnection();
		TopicSession session = connection.createTopicSession( false, Session.AUTO_ACKNOWLEDGE );
		destination = session.createTopic( endpointName );
		return new Connection<TopicSession>( connection, session );
	}
}
