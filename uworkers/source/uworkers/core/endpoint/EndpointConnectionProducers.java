package uworkers.core.endpoint;

import javax.jms.IllegalStateException;
import javax.jms.JMSException;

import lombok.val;
import trip.spi.Producer;
import trip.spi.Provided;
import trip.spi.ProviderContext;
import uworkers.api.EndpointConnection;
import uworkers.api.Subscriber;
import uworkers.api.Worker;
import uworkers.core.config.Endpoints;
import uworkers.core.config.UWorkerConfiguration;

public class EndpointConnectionProducers {

	@Provided
	MQProvider mqProvider;
	
	@Provided
	UWorkerConfiguration configuration;

	@Producer
	public WorkerEndpointConnection produceWorkerEndpointConnection( final ProviderContext context ) throws InterruptedException, JMSException {
		final Worker worker = context.getAnnotation( Worker.class );
		if ( worker == null || worker.name().isEmpty())
			throw new IllegalStateException( "Could not produce endpoint: missing @Worker annotation." );
		return produceWorkerEndpointConnection( worker );
	}

	WorkerEndpointConnection produceWorkerEndpointConnection( final Worker worker ) throws InterruptedException, JMSException {
		val consumerConfiguration = Endpoints.retrieveConfigForConsumer( worker, configuration.getEndpointConsumers() );
		val endpoint = new WorkerEndpointConnection( consumerConfiguration.getEndpoint(), mqProvider );
		endpoint.start();
		return endpoint;
	}

	@Producer
	public SubscriberEndpointConnection produceSubscriberEndpointConnection( final ProviderContext context ) throws InterruptedException, JMSException {
		final Subscriber subscriber = context.getAnnotation( Subscriber.class );
		if ( subscriber == null || subscriber.name().isEmpty())
			throw new IllegalStateException( "Could not produce endpoint: missing @Subscriber annotation." );
		return produceSubscriberEndpointConnection( subscriber );
	}

	SubscriberEndpointConnection produceSubscriberEndpointConnection( final Subscriber subscriber ) throws InterruptedException, JMSException {
		val consumerConfiguration = Endpoints.retrieveConfigForConsumer( subscriber, configuration.getEndpointConsumers() );
		val endpoint = new SubscriberEndpointConnection( consumerConfiguration.getEndpoint(), mqProvider );
		endpoint.start();
		return endpoint;
	}

	@Producer
	public EndpointConnection produceEndpointConnection( final ProviderContext context ) throws InterruptedException, JMSException {
		final Worker worker = context.getAnnotation( Worker.class );
		if ( worker != null )
			return produceWorkerEndpointConnection( worker );
		final Subscriber subscriber = context.getAnnotation( Subscriber.class );
		if ( subscriber != null )
			return produceSubscriberEndpointConnection( subscriber );
		throw new IllegalStateException( "Could not produce endpoint: missing @Subscriber/@Worker annotation." );
	}
}
