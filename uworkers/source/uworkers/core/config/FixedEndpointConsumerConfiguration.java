package uworkers.core.config;

import trip.spi.Name;
import uworkers.api.Subscriber;
import uworkers.api.Worker;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FixedEndpointConsumerConfiguration implements EndpointConsumerConfiguration {

	final String name;
	final String endpoint;
	final Integer numberOfInstances = 1;

	public static EndpointConsumerConfiguration from( Class<?> targetConsumer ) {
		final String name = extractNameFromConsumer(targetConsumer);
		final String endpoint = extractEndpointFromConsumer(targetConsumer);
		return new FixedEndpointConsumerConfiguration(name, endpoint);
	}

	private static String extractNameFromConsumer(Class<?> targetConsumer) {
		String name = null;
		Name nameAnnotation = targetConsumer.getAnnotation( Name.class );
		if ( nameAnnotation != null )
			name = nameAnnotation.value();
		return name;
	}

	private static String extractEndpointFromConsumer(Class<?> targetConsumer) {
		String endpoint = extractEndpointFromWorkerAnnotation(targetConsumer);
		if ( endpoint == null )
			endpoint = extractEndpointFromSubscriberAnnotation(targetConsumer, endpoint);
		return endpoint;
	}

	private static String extractEndpointFromWorkerAnnotation(Class<?> targetConsumer) {
		String endpoint = null;
		Worker worker = targetConsumer.getAnnotation( Worker.class );
		if ( worker != null )
			endpoint = worker.value();
		return endpoint;
	}

	private static String extractEndpointFromSubscriberAnnotation(Class<?> targetConsumer, String endpoint) {
		Subscriber subscriber = targetConsumer.getAnnotation( Subscriber.class );
		if ( subscriber != null )
			endpoint = subscriber.value();
		return endpoint;
	}
}
