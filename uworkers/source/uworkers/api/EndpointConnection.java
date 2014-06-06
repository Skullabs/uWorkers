package uworkers.api;

import java.io.Serializable;

import javax.jms.JMSException;

public interface EndpointConnection {

	void start() throws InterruptedException, JMSException;

	void startAndListenMessages() throws InterruptedException, JMSException;

	void connect() throws JMSException;

	void stop();

	void send( Serializable object ) throws JMSException;

	<T extends Serializable> T receive( Class<T> target ) throws JMSException;

	Object receive() throws JMSException;

}