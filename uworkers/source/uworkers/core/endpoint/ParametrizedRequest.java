package uworkers.core.endpoint;

import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.TextMessage;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ParametrizedRequest {

	@NonNull TextMessage message;
	final MessageProducer producer;
	
	public ParametrizedRequest setCorrelationId( String id ) throws JMSException {
		message.setJMSCorrelationID(id);
		return this;
	}
	
	public ParametrizedRequest setMessageId( String id ) throws JMSException {
		message.setJMSMessageID(id);
		return this;
	}

	public TextMessage send() throws JMSException {
		producer.send(message);
		return message;
	}
}
