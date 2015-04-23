package uworkers.utils;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import javax.jms.JMSException;

import org.junit.Test;

import trip.spi.Provided;
import trip.spi.ServiceProviderException;
import uworkers.api.EndpointConnection;
import uworkers.api.Worker;
import uworkers.api.UWorkerService;
import uworkers.core.endpoint.MQProvider;

public class AsyncEndpointConsumptionTest extends TestCase {

	final static int TOTAL_OF_MSGS = 10;
	final UWorkerService service = UWorkerService.newInstance();
	final CountDownLatch counter = new CountDownLatch( TOTAL_OF_MSGS );

	@Provided MQProvider mqProvider;
	@Provided @Worker( name = "worker.test" ) EndpointConnection worker;

	@Override
	public void setup() throws ServiceProviderException {
		assertThat( mqProvider, notNullValue() );
		HelloWorker helloWorker = new HelloWorker( "worker.test", mqProvider, counter );
		service.start( helloWorker );
	}

	@Test
	public void grantThatSendAndReceiveDataFromWorkerEndpoint() throws JMSException, InterruptedException, IOException {
		assertThat( worker, notNullValue() );
		for ( int i = 0; i < TOTAL_OF_MSGS; i++ )
			worker.send( new Hello( "WORLD" ) );
		counter.await();
	}
}
