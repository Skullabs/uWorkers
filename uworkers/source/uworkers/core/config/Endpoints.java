package uworkers.core.config;

import java.util.List;

import lombok.val;
import trip.spi.helpers.filter.Filter;
import uworkers.api.Consumer;
import uworkers.api.Subscriber;
import uworkers.api.Worker;
import uworkers.utils.EndpointNamed;

public class Endpoints {

	public static String extractNameFromConsumer(final Class<?> targetConsumer) {
		val worker = targetConsumer.getAnnotation(Worker.class);
		if ( worker != null && !worker.name().isEmpty() )
			return worker.name();
		val subscriber = targetConsumer.getAnnotation(Subscriber.class);
		if ( subscriber != null && !subscriber.name().isEmpty() )
			return subscriber.name();
		throw new IllegalStateException("Missing @Worker or @Subscriber annotation on consumer class.");
	}
	
	//TODO: testar isso aqui
	public static String extractQueueOrTopicNameFromConsumer(final Class<?> targetConsumer) {
		val worker = targetConsumer.getAnnotation(Worker.class);
		if ( worker != null && !worker.queue().isEmpty() )
			return worker.queue();
		val subscriber = targetConsumer.getAnnotation(Subscriber.class);
		if ( subscriber != null && !subscriber.topic().isEmpty() )
			return subscriber.topic();
		return null;
	}
	
	@SuppressWarnings("rawtypes") 
	public static EndpointConsumerConfiguration retrieveConfigForConsumer(
			final Class<Consumer> consumerClass, final List<EndpointConsumerConfiguration> consumerConfigs )
	{
		final EndpointConsumerConfiguration configuration = FixedEndpointConsumerConfiguration.from(consumerClass);
		return retrieveConfigForConsumer(consumerConfigs, configuration);
	}

	public static EndpointConsumerConfiguration retrieveConfigForConsumer(Worker worker,
			List<EndpointConsumerConfiguration> endpointConsumers) {
		val name = worker.name();
		final EndpointConsumerConfiguration configuration = new FixedEndpointConsumerConfiguration( name, name );
		return retrieveConfigForConsumer( endpointConsumers, configuration);
	}
	
	public static EndpointConsumerConfiguration retrieveConfigForConsumer(Subscriber subscriber,
			List<EndpointConsumerConfiguration> endpointConsumers) {
		val name = subscriber.name();
		final EndpointConsumerConfiguration configuration = new FixedEndpointConsumerConfiguration( name, name );
		return retrieveConfigForConsumer( endpointConsumers, configuration);
	}
	
	public static EndpointConsumerConfiguration retrieveConfigForConsumer(
			final List<EndpointConsumerConfiguration> consumerConfigs,
			final EndpointConsumerConfiguration configuration) {
		EndpointConsumerConfiguration configForConsumer = 
				Filter.first( consumerConfigs, EndpointNamed.with( configuration.getName() ) );
		if ( configForConsumer == null )
			configForConsumer = configuration;
		return configForConsumer;
	}
}