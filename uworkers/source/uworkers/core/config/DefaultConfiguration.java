package uworkers.core.config;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import lombok.extern.java.Log;
import trip.spi.Singleton;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigException.Missing;

/**
 * Wraps the {@link Config} class and provided some
 * helpful methods to deal with configuration parameters.
 */
@Log
@Singleton( exposedAs = Configuration.class)
@RequiredArgsConstructor
public class DefaultConfiguration implements Configuration {

	@Getter
	@Delegate( excludes=NotImplementedConfigMethods.class )
	final Config mergeableConfig;

	public String getString( final String name, final String defaultValue ) {
		try {
			return mergeableConfig.getString(name);
		} catch ( Missing cause ) {
			return defaultValue;
		}
	}

	public Integer getInt( final String name, final Integer defaultValue ) {
		try {
			return mergeableConfig.getInt(name);
		} catch ( Missing cause ) {
			return defaultValue;
		}
	}
	
	public Configuration getConfig( final String name ) {
		try {
			return new DefaultConfiguration( mergeableConfig.getConfig(name) );
		} catch ( Missing cause ) {
			return null;
		}
	}

	public List<Configuration> getConfigList( final String name ) {
		final List<Configuration> newConfigList = new ArrayList<Configuration>();
		try {
			for ( Config config : this.mergeableConfig.getConfigList(name) )
				newConfigList.add( new DefaultConfiguration(config) );
		} catch ( Missing cause ) {
			log.warning( "No values found for " + name + ". Returning an empty list." );
		}
		return newConfigList;
	}

	public Configuration withFallback( final Configuration mergeableConfig ) {
		final Config mergedConfig = this.mergeableConfig.withFallback( mergeableConfig.getMergeableConfig() );
		return new DefaultConfiguration( mergedConfig );
	}

	private interface NotImplementedConfigMethods {
		List<Config> getConfigList( String name );
		Config getConfig( String name );
		Config withFallback( Config mergeableConfig );
	}
}
