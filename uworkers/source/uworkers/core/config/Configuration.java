package uworkers.core.config;

import java.util.List;

import com.typesafe.config.Config;

public interface Configuration {

	String getString( String name );

	String getString( String name, String defaultValue );
	
	int getInt( String name );

	Integer getInt( String name, Integer defaultValue );

	Configuration getConfig( String name );

	List<Configuration> getConfigList( String name );

	Configuration withFallback( Configuration mergeableConfig );

	Config getMergeableConfig();
}
