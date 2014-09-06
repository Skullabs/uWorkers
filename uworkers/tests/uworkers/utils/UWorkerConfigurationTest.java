package uworkers.utils;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import uworkers.core.config.EndpointConsumerConfiguration;
import uworkers.core.config.UWorkerConfiguration;

public class UWorkerConfigurationTest {

	@Test
	public void ensureThatUWorkerConfigurationWorksAsExpected(){
		UWorkerConfiguration configuration = UWorkerConfiguration.load();
		List<EndpointConsumerConfiguration> consumers = configuration.getEndpointConsumers();
		assertThat( consumers.size(), is( 3 ) );
		EndpointConsumerConfiguration first = consumers.get(0);
		assertTrue( first.getEndpoint().startsWith("pingpong.") );
		assertNotNull( first.getName() );
		assertFalse( first.getName().isEmpty() );
	}
}
