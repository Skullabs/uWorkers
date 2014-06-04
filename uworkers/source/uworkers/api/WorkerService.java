package uworkers.api;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import trip.spi.Provided;
import trip.spi.ServiceProvider;
import trip.spi.ServiceProviderException;

@Log
@NoArgsConstructor
@RequiredArgsConstructor
public class WorkerService {

	final ExecutorService executor = Executors.newCachedThreadPool();
	@Provided @NonNull ServiceProvider provider;

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
		WorkerService workerService = new WorkerService();
		workerService.provider = new ServiceProvider();
		return workerService;
	}
}
