package uworkers.api;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.java.Log;
import trip.spi.ServiceProvider;
import trip.spi.ServiceProviderException;
import trip.spi.helpers.filter.Filter;
import uworkers.core.config.EndpointConsumerConfiguration;
import uworkers.core.config.FixedEndpointConsumerConfiguration;
import uworkers.core.config.UWorkerConfiguration;
import uworkers.utils.AnnotatedClasses;
import uworkers.utils.EndpointNamed;

@Log
@Getter
@Accessors( fluent=true )
@RequiredArgsConstructor
@SuppressWarnings("rawtypes")
public class WorkerService {

	final ExecutorService executor = Executors.newCachedThreadPool();
	final UWorkerConfiguration workerConfiguration;
	final ServiceProvider provider;

	public void start() throws ServiceProviderException, WorkerException {
		final Iterable<Class<Consumer>> consumerClasses = loadConsumerClasses();
		final List<EndpointConsumerConfiguration> consumerConfigs = workerConfiguration.getEndpointConsumers();
		for ( Class<Consumer> consumerClass : consumerClasses ){
			final EndpointConsumerConfiguration configuration = retrieveConfigForConsumer(consumerClass, consumerConfigs);
			tryStartConsumerClass(consumerClass, configuration);
		}
	}

	private Iterable<Class<Consumer>> loadConsumerClasses() {
		return provider.loadClassesImplementing( Consumer.class, AnnotatedClasses.with( Autostart.class ) );
	}

	public EndpointConsumerConfiguration retrieveConfigForConsumer(
			Class<Consumer> consumerClass, List<EndpointConsumerConfiguration> consumerConfigs )
	{
		final EndpointConsumerConfiguration configuration = FixedEndpointConsumerConfiguration.from(consumerClass);
		EndpointConsumerConfiguration configForConsumer = 
				Filter.first( consumerConfigs, EndpointNamed.with( configuration.getName() ) );
		if ( configForConsumer == null )
			configForConsumer = configuration;
		return configForConsumer;
	}

	public void tryStartConsumerClass( Class<Consumer> consumerClass, EndpointConsumerConfiguration configuration )
			throws WorkerException, ServiceProviderException {
		for ( int i=0; i<configuration.getNumberOfInstances(); i++ )
			start( instantiate( consumerClass, configuration ) );
	}

	public Consumer instantiate(Class<Consumer> consumerClass,
			EndpointConsumerConfiguration configuration) throws WorkerException {
		try {
			final Consumer consumer = consumerClass.newInstance();
			final String endpointName = configuration.getEndpoint();
			if ( endpointName != null )
				consumer.endpointName( endpointName );
			return consumer;
		} catch (  InstantiationException | IllegalAccessException cause ) {
			throw new WorkerException(cause);
		}
	}

	public void start( Consumer consumer ) throws ServiceProviderException {
		log.info( "Deploying consumer: " + consumer );
		provider.provideOn(consumer);
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
		final UWorkerConfiguration workerConfiguration = UWorkerConfiguration.load( rootConfiguration );
		return newInstance(workerConfiguration);
	}

	private static WorkerService newInstance(
			final UWorkerConfiguration workerConfiguration) {
		final ServiceProvider serviceProvider = new ServiceProvider();
		return new WorkerService( workerConfiguration, serviceProvider );
	}
}
