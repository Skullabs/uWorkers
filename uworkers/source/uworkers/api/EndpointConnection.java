package uworkers.api;

import java.io.IOException;
import java.io.Serializable;

import javax.jms.JMSException;

public interface EndpointConnection {

	void start() throws InterruptedException, JMSException;

	void startAndListenMessages() throws InterruptedException, JMSException;

	void connect() throws JMSException;

	void stop();

	void send( Object object ) throws JMSException, IOException;

	<T> T receive( Class<T> target ) throws JMSException, IOException;

}