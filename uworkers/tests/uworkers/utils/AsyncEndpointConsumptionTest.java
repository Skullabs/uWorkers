package uworkers.utils;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.jms.JMSException;

import org.junit.Test;

import trip.spi.Name;
import trip.spi.Provided;
import uworkers.core.endpoint.MQProvider;

public class AsyncEndpointConsumptionTest extends TestCase {

	final ExecutorService executor = Executors.newCachedThreadPool();
	final CountDownLatch counter = new CountDownLatch( 10 );

	@Provided
	@Name( "activemq-test" )
	MQProvider mqProvider;

	@Test
	public void grantThatSendAndReceiveDataFromWorkerEndpoint() throws JMSException, InterruptedException {

	}
}
