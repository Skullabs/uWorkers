package uworkers.tests;

import trip.spi.Name;
import uworkers.api.Subscriber;
import uworkers.api.Worker;

public class Consumers {

	@Worker( "test.worker" )
	public void receiveWorker( SearchMessage message ) {
		System.out.println( message.query() );
	}

	@Name( "secret" )
	@Subscriber( "test.subscriber" )
	public void subscribeFor( SearchMessage message ) {
		System.out.println( message.query() );
	}
}
