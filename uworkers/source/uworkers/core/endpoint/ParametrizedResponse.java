package uworkers.core.endpoint;

import javax.jms.JMSException;
import javax.jms.Message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ParametrizedResponse<T> {

	@Getter
	final T response;
	final Message message;

	public void aknowledge() throws JMSException {
		message.acknowledge();
	}

	public String getMessageId() throws JMSException {
		return message.getJMSMessageID();
	}

	public String getCorrelationId() throws JMSException {
		return message.getJMSCorrelationID();
	}
}