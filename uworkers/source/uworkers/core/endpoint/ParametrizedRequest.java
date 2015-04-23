package uworkers.core.endpoint;

import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.TextMessage;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ParametrizedRequest {

	final TextMessage message;
	final MessageProducer producer;
	
	public ParametrizedRequest setCorrelationId( String id ) throws JMSException {
		message.setJMSCorrelationID(id);
		return this;
	}
	
	public ParametrizedRequest setMessageId( String id ) throws JMSException {
		message.setJMSMessageID(id);
		return this;
	}
	
	public void send() throws JMSException {
		producer.send(message);
	}
}
