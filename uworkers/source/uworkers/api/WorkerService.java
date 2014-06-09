package uworkers.api;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import trip.spi.ServiceProvider;
import trip.spi.ServiceProviderException;
import uworkers.core.config.UWorkerConfiguration;

@Log
@RequiredArgsConstructor
public class WorkerService {

	final ExecutorService executor = Executors.newCachedThreadPool();
	final UWorkerConfiguration workerConfiguration;
	final ServiceProvider provider;

	public void start() throws ServiceProviderException {
		for ( Consumer<?> consumer : provider.loadAll( Consumer.class ) )
			start( consumer );
	}

	public void start( Consumer<?> consumer ) {
		log.info( "Deploying consumer: " + consumer );
		executor.submit( consumer );
	}

	public void stop() {
		executor.shutdownNow();
	}

	public static WorkerService newInstance() {
		final UWorkerConfiguration workerConfiguration = UWorkerConfiguration.load();
		return newInstance(workerConfiguration);
	}

	public static WorkerService newInstance( String rootConfiguration ) {
		UWorkerConfiguration workerConfiguration = UWorkerConfiguration.load( rootConfiguration );
		return newInstance(workerConfiguration);
	}

	private static WorkerService newInstance(
			final UWorkerConfiguration workerConfiguration) {
		final ServiceProvider serviceProvider = new ServiceProvider();
		return new WorkerService( workerConfiguration, serviceProvider );
	}
}
