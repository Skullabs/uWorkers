package uworkers.utils;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import javax.jms.JMSException;

import org.junit.Test;

import trip.spi.Name;
import trip.spi.Provided;
import uworkers.core.endpoint.MQProvider;
import uworkers.core.endpoint.SubscriberEndpoint;
import uworkers.core.endpoint.WorkerEndpoint;

public class EndpointsTest extends TestCase {

	static final String WORLD = "World";

	@Provided
	@Name( "activemq-test" )
	MQProvider mqProvider;

	@Test
	public void grantThatSendAndReceiveDataFromWorkerEndpoint() throws JMSException, InterruptedException {
		WorkerEndpoint endpoint = new WorkerEndpoint( "TEST.QUEUE.ENDPOINT", mqProvider );
		try {
			endpoint.start();
			endpoint.send( new Hello( WORLD ) );
			Hello hello = endpoint.receive( Hello.class );
			assertThat( hello.getWorld(), is( WORLD ) );
		} finally {
			endpoint.stop();
		}
	}

	@Test
	public void grantThatSendAndReceiveDataFromSubscriberEndpoint() throws JMSException, InterruptedException {
		SubscriberEndpoint endpoint = new SubscriberEndpoint( "TEST.TOPIC.ENDPOINT", mqProvider );
		try {
			endpoint.start();
			endpoint.send( new Hello( WORLD ) );
			Hello hello = endpoint.receive( Hello.class );
			assertThat( hello.getWorld(), is( WORLD ) );
		} finally {
			endpoint.stop();
		}
	}

}
