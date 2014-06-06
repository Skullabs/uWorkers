package uworkers.core.endpoint;

import javax.jms.IllegalStateException;
import javax.jms.JMSException;

import trip.spi.Producer;
import trip.spi.Provided;
import trip.spi.ProviderContext;
import uworkers.api.EndpointConnection;
import uworkers.api.Subscriber;
import uworkers.api.Worker;

public class EndpointConnectionProducers {

	@Provided MQProvider mqProvider;

	@Producer
	public WorkerEndpointConnection produceWorkerEndpointConnection( ProviderContext context ) throws InterruptedException, JMSException {
		Worker worker = context.getAnnotation( Worker.class );
		if ( worker == null )
			throw new IllegalStateException( "Could not produce endpoint: missing @Worker annotation." );
		return produceWorkerEndpointConnection( worker );
	}

	WorkerEndpointConnection produceWorkerEndpointConnection( Worker worker ) throws InterruptedException, JMSException {
		WorkerEndpointConnection endpoint = new WorkerEndpointConnection( worker.value(), mqProvider );
		endpoint.start();
		return endpoint;
	}

	@Producer
	public SubscriberEndpointConnection produceSubscriberEndpointConnection( ProviderContext context ) throws InterruptedException, JMSException {
		Subscriber subscriber = context.getAnnotation( Subscriber.class );
		if ( subscriber == null )
			throw new IllegalStateException( "Could not produce endpoint: missing @Subscriber annotation." );
		return produceSubscriberEndpointConnection( subscriber );
	}

	SubscriberEndpointConnection produceSubscriberEndpointConnection( Subscriber worker ) throws InterruptedException, JMSException {
		SubscriberEndpointConnection endpoint = new SubscriberEndpointConnection( worker.value(), mqProvider );
		endpoint.start();
		return endpoint;
	}

	@Producer
	public EndpointConnection produceEndpointConnection( ProviderContext context ) throws InterruptedException, JMSException {
		Worker worker = context.getAnnotation( Worker.class );
		if ( worker != null )
			return produceWorkerEndpointConnection( worker );
		Subscriber subscriber = context.getAnnotation( Subscriber.class );
		if ( subscriber != null )
			return produceSubscriberEndpointConnection( subscriber );
		throw new IllegalStateException( "Could not produce endpoint: missing @Subscriber/@Worker annotation." );
	}
}
