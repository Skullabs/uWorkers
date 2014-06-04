package uworkers.core.endpoint;

import javax.jms.IllegalStateException;
import javax.jms.JMSException;

import trip.spi.Producer;
import trip.spi.Provided;
import trip.spi.ProviderContext;
import uworkers.api.Endpoint;
import uworkers.api.Subscriber;
import uworkers.api.Worker;

public class EndpointProducers {

	@Provided MQProvider mqProvider;

	@Producer
	public WorkerEndpoint produceWorkerEndpoint( ProviderContext context ) throws InterruptedException, JMSException {
		Worker worker = context.getAnnotation( Worker.class );
		if ( worker == null )
			throw new IllegalStateException( "Could not produce endpoint: missing @Worker annotation." );
		return produceWorkerEndpoint( worker );
	}

	WorkerEndpoint produceWorkerEndpoint( Worker worker ) throws InterruptedException, JMSException {
		WorkerEndpoint endpoint = new WorkerEndpoint( worker.value(), mqProvider );
		endpoint.start();
		return endpoint;
	}

	@Producer
	public SubscriberEndpoint produceSubscriberEndpoint( ProviderContext context ) throws InterruptedException, JMSException {
		Subscriber subscriber = context.getAnnotation( Subscriber.class );
		if ( subscriber == null )
			throw new IllegalStateException( "Could not produce endpoint: missing @Subscriber annotation." );
		return produceSubscriberEndpoint( subscriber );
	}

	SubscriberEndpoint produceSubscriberEndpoint( Subscriber worker ) throws InterruptedException, JMSException {
		SubscriberEndpoint endpoint = new SubscriberEndpoint( worker.value(), mqProvider );
		endpoint.start();
		return endpoint;
	}

	@Producer
	public Endpoint produceEndpoint( ProviderContext context ) throws InterruptedException, JMSException {
		Worker worker = context.getAnnotation( Worker.class );
		if ( worker != null )
			return produceWorkerEndpoint( worker );
		Subscriber subscriber = context.getAnnotation( Subscriber.class );
		if ( subscriber != null )
			return produceSubscriberEndpoint( subscriber );
		throw new IllegalStateException( "Could not produce endpoint: missing @Subscriber/@Worker annotation." );
	}
}
