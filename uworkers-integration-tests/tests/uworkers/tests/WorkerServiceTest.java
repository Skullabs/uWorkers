package uworkers.tests;

import java.util.concurrent.CountDownLatch;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;

import org.junit.Before;
import org.junit.Test;

import trip.spi.Provided;
import trip.spi.ServiceProvider;
import trip.spi.ServiceProviderException;
import uworkers.api.EndpointConnection;
import uworkers.api.Worker;
import uworkers.api.UWorkerException;
import uworkers.api.UWorkerService;
import uworkers.core.AbstractSubscriber;
import uworkers.core.endpoint.MQProvider;
import uworkers.tests.PingPongConsumers.Ping;
import uworkers.tests.PingPongConsumers.Pong;

public class WorkerServiceTest {

	@Provided
	@Worker( name = "pingpong.ping" )
	EndpointConnection pong;

	final UWorkerService workerService = UWorkerService.newInstance();
	final CountDownLatch counter = new CountDownLatch(1);
	
	@Before
	public void setup() throws ServiceProviderException, UWorkerException{
		final ServiceProvider provider = workerService.provider();
		provider.provideOn(this);
		workerService.start();
	}

	@Test
	public void grantThatTriedToAutoInstantiatePingPongConsumersAsExpected() throws Exception {
		workerService.start( new PongReceiver() );
		pong.send( new Ping() );
		counter.await();
	}

	@Getter
	@Setter
	@Accessors( fluent=true )
	class PongReceiver extends AbstractSubscriber<Pong> {
		
		@NonNull String endpointName = "pingpong.pong.responses";
		@Provided MQProvider mqProvider;

		@Override
		public void handle(Pong receivedMessage) throws UWorkerException, InterruptedException {
			counter.countDown();
		}
		
		@Override
		public String toString() {
			return "The Pong Receiver( " + endpointName + " )";
		}

		@Override
		public Class<Pong> getExpectedObjectClass() {
			return Pong.class;
		}
	}
}
