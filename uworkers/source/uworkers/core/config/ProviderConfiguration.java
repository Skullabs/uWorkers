package uworkers.core.config;

import lombok.Delegate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProviderConfiguration implements Configuration {

	@Delegate
	final Configuration rootConfig;

	@Getter( lazy=true )
	private final String url = rootConfig.getString( "url" );

	@Getter( lazy=true )
	private final String username = rootConfig.getString( "username" );

	@Getter( lazy=true )
	private final String password = rootConfig.getString( "password" );
}
