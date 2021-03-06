package uworkers.core.config;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public class FixedEndpointConsumerConfiguration implements EndpointConsumerConfiguration {

	final Integer numberOfInstances = 1;
	final String name;
	
	@Setter
	@NonNull
	String endpoint;

	public static EndpointConsumerConfiguration from( final Class<?> targetConsumer ) {
		final String name = Endpoints.extractNameFromConsumer(targetConsumer);
		return new FixedEndpointConsumerConfiguration(name, name);
	}
}
