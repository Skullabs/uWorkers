package uworkers.core.config;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

@RequiredArgsConstructor
public class UWorkerConfiguration implements Configuration {

	@Delegate
	final Configuration config;

	@Getter( lazy=true )
	private final List<EndpointConsumerConfiguration> endpointConsumers = readEndpointConsumersConfiguration();

	public List<EndpointConsumerConfiguration> readEndpointConsumersConfiguration() {
		List<EndpointConsumerConfiguration> consumers = new ArrayList<EndpointConsumerConfiguration>();
		for ( Configuration endpoint : config.getConfigList("uworker.consumers") )
			consumers.add( new DefaultEndpointConsumerConfiguration(endpoint) );
		return consumers;
	}

	public ProviderConfiguration readProvider() {
		Configuration provider = config.getConfig("uworkers.provider");
		return new ProviderConfiguration( provider );
	}

	public static UWorkerConfiguration load() {
		Configuration config = new DefaultConfiguration( loadDefaultConfig() );
		return new UWorkerConfiguration(config);
	}

	public static UWorkerConfiguration load( String root ) {
		final Config defaultConfig = loadDefaultConfig();
		final Config newRootConfig = ConfigFactory.load("META-INF/uworkers")
			.getConfig(root).withFallback(defaultConfig);
		final Configuration config = new DefaultConfiguration( newRootConfig );
		return new UWorkerConfiguration(config);
	}

	private static Config loadDefaultConfig(){
		final Config defaultConfiguration = ConfigFactory.load();
		final Config reference = ConfigFactory.load("META-INF/reference").withFallback( defaultConfiguration );
		final Config config = ConfigFactory.load("META-INF/uworkers").withFallback( reference );
		return ConfigFactory.load("conf/application").withFallback( config );
	}
}
