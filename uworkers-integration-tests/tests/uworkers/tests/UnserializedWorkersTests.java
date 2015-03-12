package uworkers.tests;

import java.util.concurrent.CountDownLatch;

import lombok.SneakyThrows;

import org.junit.Before;
import org.junit.Test;

import trip.spi.Provided;
import trip.spi.ServiceProvider;
import trip.spi.ServiceProviderException;
import uworkers.api.EndpointConnection;
import uworkers.api.UWorkerException;
import uworkers.api.UWorkerService;
import uworkers.api.Worker;

public class UnserializedWorkersTests {

	@Provided
	@Worker(name = "unserialized.worker", serialized = false)
	EndpointConnection unserializedWorker;

	final UWorkerService workerService = UWorkerService.newInstance();
	final CountDownLatch counter = new CountDownLatch(1);

	@Before
	public void setup() throws ServiceProviderException, UWorkerException {
		final ServiceProvider provider = workerService.provider();
		provider.providerFor(CountDownLatch.class, counter);
		provider.provideOn(this);
		workerService.start();
	}

	@Test(timeout = 10000)
	@SneakyThrows
	public void ensureThatCouldSendAMessageToTheUnserializedObjectsWorker() {
		unserializedWorker.send(UnserializedObjectsWorker.EXPECTED_STRING);
		counter.await();
	}
}
