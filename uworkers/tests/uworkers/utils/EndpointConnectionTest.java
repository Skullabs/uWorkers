package uworkers.utils;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

import javax.jms.JMSException;

import lombok.RequiredArgsConstructor;

import org.junit.Test;

import trip.spi.Provided;
import uworkers.api.EndpointConnection;
import uworkers.core.endpoint.MQProvider;
import uworkers.core.endpoint.SubscriberEndpointConnection;
import uworkers.core.endpoint.WorkerEndpointConnection;


public class EndpointConnectionTest extends TestCase {

	static final String WORLD = "World";

	final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

	@Provided
	MQProvider mqProvider;

	@Test
	public void grantThatSendAndReceiveDataFromWorkerEndpoint() throws JMSException, InterruptedException, IOException {
		WorkerEndpointConnection endpoint = new WorkerEndpointConnection( "TEST.QUEUE.ENDPOINT", mqProvider );
		try {
			endpoint.startAndListenMessages();
			endpoint.send( new Hello( WORLD ) );
			Hello hello = endpoint.receive( Hello.class );
			assertThat( hello.getWorld(), is( WORLD ) );
		} finally {
			endpoint.stop();
		}
	}

	@Test
	public void grantThatSendAndReceiveDataFromSubscriberEndpoint() throws JMSException, InterruptedException, ExecutionException, IOException {
		SubscriberEndpointConnection endpoint = new SubscriberEndpointConnection( "TEST.TOPIC.ENDPOINT", mqProvider );
		try {
			endpoint.startAndListenMessages();
			Future<String> world = executor.submit( new ReceivableHello(endpoint) );
			endpoint.send( new Hello( WORLD ) );
			assertThat( world.get(), is( WORLD ) );
		} finally {
			endpoint.stop();
		}
	}

	@RequiredArgsConstructor
	class ReceivableHello implements Callable<String> {
		
		final EndpointConnection endpoint;

		@Override
		public String call() throws Exception {
			return endpoint.receive( Hello.class ).getWorld();
		}
	}

}
