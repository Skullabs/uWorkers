package uworkers.core.endpoint;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicSession;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors( fluent = true )
@RequiredArgsConstructor
public class SubscriberEndpointConnection extends AbstractEndpointConnection<TopicSession> {

	@NonNull String endpointName;
	final MQProvider mqProvider;
	Topic destination;

	@Override
	public Connection<TopicSession> createConnection() throws JMSException {
		final TopicConnection connection = mqProvider().createSubscriptionConnection();
		final TopicSession session = connection.createTopicSession( false, Session.AUTO_ACKNOWLEDGE );
		destination = session.createTopic( endpointName );
		return new Connection<TopicSession>( connection, session );
	}

	@Override
	MessageConsumer createMessageConsumer() {
		try {
			return currentSession().createSubscriber( destination() );
		} catch ( final JMSException cause ) {
			throw new RuntimeException( cause );
		}
	}
}
