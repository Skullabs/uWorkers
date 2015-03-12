package uworkers.tests;

import java.util.concurrent.CountDownLatch;

import trip.spi.Provided;
import trip.spi.Singleton;
import uworkers.api.Worker;

@Singleton
public class UnserializedObjectsWorker {

	final static String EXPECTED_STRING = "UNSERIALIZED_STRING";

	@Provided
	CountDownLatch counter;

	@Worker(name = "unserialized.worker", serialized = false)
	public void receive(String data) {
		counter.countDown();
	}
}
