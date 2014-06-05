package uworkers.utils;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.concurrent.CountDownLatch;

import javax.jms.JMSException;

import org.junit.Test;

import trip.spi.Provided;
import uworkers.api.Endpoint;
import uworkers.api.Worker;
import uworkers.api.WorkerService;
import uworkers.core.endpoint.MQProvider;

public class AsyncEndpointConsumptionTest extends TestCase {

	final static int TOTAL_OF_MSGS = 10;
	final WorkerService service = new WorkerService();
	final CountDownLatch counter = new CountDownLatch( TOTAL_OF_MSGS );

	@Provided MQProvider mqProvider;
	@Provided @Worker( "worker.test" ) Endpoint worker;

	@Override
	public void setup() {
		assertThat( mqProvider, notNullValue() );
		HelloWorker helloWorker = new HelloWorker( "worker.test", mqProvider, counter );
		service.start( helloWorker );
	}

	@Test( timeout = 15000 )
	public void grantThatSendAndReceiveDataFromWorkerEndpoint() throws JMSException, InterruptedException {
		assertThat( worker, notNullValue() );
		for ( int i = 0; i < TOTAL_OF_MSGS; i++ )
			worker.send( new Hello( "WORLD" ) );
		counter.await();
	}
}
