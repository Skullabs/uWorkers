package uworkers.tests;

import org.junit.Before;
import org.junit.Test;

import trip.spi.Provided;
import trip.spi.ServiceProvider;
import trip.spi.ServiceProviderException;
import uworkers.api.EndpointConnection;
import uworkers.api.Subscriber;
import uworkers.api.WorkerException;
import uworkers.api.WorkerService;

public class WorkerServiceTest {

	@Provided
	@Subscriber( "pingpong.pong.responses" )
	EndpointConnection pongResp;

	final WorkerService workerService = WorkerService.newInstance();
	
	@Before
	public void setup() throws ServiceProviderException, WorkerException{
		final ServiceProvider provider = workerService.provider();
		provider.provideOn(this);
		workerService.start();
	}

	@Test
	public void grantThatTriedToAutoInstantiatePingPongConsumersAsExpected(){
		
	}
}
