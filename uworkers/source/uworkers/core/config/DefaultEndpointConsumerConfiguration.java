package uworkers.core.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

@RequiredArgsConstructor
public class DefaultEndpointConsumerConfiguration implements EndpointConsumerConfiguration {

	@Delegate
	final Configuration rootConfig;

	@Getter( lazy=true )
	private final String name = rootConfig.getString( "name", null );

	@Getter( lazy=true )
	private final String endpoint = rootConfig.getString( "endpoint", null );

	@Getter( lazy=true )
	private final Integer numberOfInstances = rootConfig.getInt( "nr-of-instances", 1 );
}
