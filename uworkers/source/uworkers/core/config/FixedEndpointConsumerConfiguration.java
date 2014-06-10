package uworkers.core.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import trip.spi.Name;

@Getter
@RequiredArgsConstructor
public class FixedEndpointConsumerConfiguration implements EndpointConsumerConfiguration {

	final String name;
	final String endpoint = null;
	final Integer numberOfInstances = 1;

	public static EndpointConsumerConfiguration from( Class<?> targetConsumer ) {
		final String name = extractNameFromConsumer(targetConsumer);
		return new FixedEndpointConsumerConfiguration(name);
	}

	private static String extractNameFromConsumer(Class<?> targetConsumer) {
		String name = null;
		Name nameAnnotation = targetConsumer.getAnnotation( Name.class );
		if ( nameAnnotation != null )
			name = nameAnnotation.value();
		if ( name == null )
			name = targetConsumer.getCanonicalName();
		return name;
	}
}
