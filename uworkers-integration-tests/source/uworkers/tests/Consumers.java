package uworkers.tests;

import uworkers.api.Worker;

public class Consumers {

	@Worker( "test.worker" )
	public void receiveWorker( SearchMessage message ) {
		System.out.println( message.query() );
	}
}
