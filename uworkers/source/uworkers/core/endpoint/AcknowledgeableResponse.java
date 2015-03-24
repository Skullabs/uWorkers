package uworkers.core.endpoint;

import javax.jms.JMSException;
import javax.jms.Message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AcknowledgeableResponse<T> {

	@Getter
	final T response;

	final Message message;

	public void aknowledge() throws JMSException {
		message.acknowledge();
	}
}