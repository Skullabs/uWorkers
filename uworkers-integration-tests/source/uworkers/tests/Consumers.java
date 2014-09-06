package uworkers.tests;

import uworkers.api.Subscriber;
import uworkers.api.Worker;

public class Consumers {

	@Worker( name = "test.worker" )
	public void receiveWorker( SearchMessage message ) {
		System.out.println( message.query() );
	}

	@Subscriber( topic = "test.subscriber", name = "secret")
	public void subscribeFor( SearchMessage message ) {
		System.out.println( message.query() );
	}
}
