package uworkers.core.endpoint;

import javax.jms.JMSException;
import javax.jms.QueueConnection;
import javax.jms.TopicConnection;

public interface MQProvider {

	QueueConnection createWorkerConnection() throws JMSException;

	TopicConnection createSubscriptionConnection() throws JMSException;

}