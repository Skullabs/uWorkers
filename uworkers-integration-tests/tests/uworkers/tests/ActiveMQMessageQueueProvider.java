package uworkers.tests;

import javax.jms.JMSException;
import javax.jms.QueueConnection;
import javax.jms.TopicConnection;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import org.apache.activemq.ActiveMQConnectionFactory;

import trip.spi.Service;
import uworkers.core.endpoint.MQProvider;

@Service
@Getter
@Accessors( fluent = true )
@NoArgsConstructor
public class ActiveMQMessageQueueProvider implements MQProvider {

	final String url = "vm://127.0.0.1";
	final ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory( url );

	@Override
	public QueueConnection createWorkerConnection() throws JMSException {
		return connectionFactory().createQueueConnection();
	}

	@Override
	public TopicConnection createSubscriptionConnection() throws JMSException {
		return connectionFactory().createTopicConnection();
	}
}
