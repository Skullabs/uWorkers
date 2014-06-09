package uworkers.core.config;

import lombok.Delegate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EndpointConsumerConfiguration {

	@Delegate
	final Configuration rootConfig;
	final ProviderConfiguration defaultProviderConfig;

	@Getter( lazy=true )
	private final String name = rootConfig.getString( "name", null );

	@Getter( lazy=true )
	private final String endpoint = rootConfig.getString( "endpoint", null );
	
	@Getter( lazy=true )
	private final ProviderConfiguration provider = readProviderConfiguration();
	
	public ProviderConfiguration readProviderConfiguration() {
		final Configuration config = rootConfig.getConfig("provider")
				.withFallback( defaultProviderConfig );
		return new ProviderConfiguration( config );
	}
}
