package uworkers.utils;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import lombok.val;

import org.junit.Test;

import trip.spi.DefaultServiceProvider;
import trip.spi.Singleton;
import uworkers.api.Consumer;
import uworkers.api.Name;
import uworkers.api.Subscriber;
import uworkers.api.Worker;
import uworkers.core.config.EndpointConsumerConfiguration;
import uworkers.core.config.Endpoints;
import uworkers.core.config.FixedEndpointConsumerConfiguration;


public class ExtractInformationFromClassTest extends TestCase {

	DefaultServiceProvider provider = new DefaultServiceProvider();

	@Test( expected=IllegalStateException.class )
	public void ensureThatCannotExtractNameFromHelloWorker(){
		Endpoints.extractNameFromConsumer( ClassNoHaveWorkerOrSubscriber.class );
	}

	@Test( expected=IllegalStateException.class )
	public void ensureThatCannotExtractNameFromEmptyNamedWorkerConsumer(){
		Endpoints.extractNameFromConsumer( EmptyNamedWorkerConsumer.class );
	}

	@Test( expected=IllegalStateException.class )
	public void ensureThatCannotExtractNameFromEmptyNamedSubscriberConsumer(){
		Endpoints.extractNameFromConsumer( EmptyNamedSubscriberConsumer.class );
	}

	@Test
	public void ensureThatCanExtractNameFromNamedWorkerConsumer(){
		val name = Endpoints.extractNameFromConsumer( NamedWorkerConsumer.class );
		assertThat( name, is( "NamedWorkerConsumer" ) );
	}

	@Test
	public void ensureThatCanExtractNameFromNamedSubscriberConsumer(){
		val name = Endpoints.extractNameFromConsumer( NamedSubscriberConsumer.class );
		assertThat( name, is( "NamedSubscriberConsumer" ) );
	}

	@Test
	public void ensureThatCanGetEndpointHelloWorkerLikeWorker(){
		final List<EndpointConsumerConfiguration> consumerConfigs = workerConfiguration.getEndpointConsumers();
		val classWorker = HelloWorker.class.getAnnotation(Worker.class);
		val endpoint = Endpoints.retrieveConfigForConsumer( classWorker, consumerConfigs);
		assertThat(endpoint.getEndpoint(), is("test.worker"));
	}

	@Test
	public void ensureThatCanGetEndpointHelloSubscriberLikeSubscriber(){
		final List<EndpointConsumerConfiguration> consumerConfigs = workerConfiguration.getEndpointConsumers();
		val classWorker = HelloSubscriber.class.getAnnotation(Subscriber.class);
		val endpoint = Endpoints.retrieveConfigForConsumer( classWorker, consumerConfigs);
		assertThat(endpoint.getName(), is("test.subscriber"));
	}

	@Test
	public void ensureYouCanSubscribeConfigDefault(){
		val configuration = new FixedEndpointConsumerConfiguration( "test.worker", "test.worker" );
		val consumerConfigs = workerConfiguration.getEndpointConsumers();

		Endpoints.retrieveConfigForConsumer(consumerConfigs, configuration);


	}
}

class ClassNoHaveWorkerOrSubscriber {
}

@Worker( name="NamedWorkerConsumer")
@Name("NamedWorkerConsumer")
@Singleton( exposedAs = Consumer.class)
class NamedWorkerConsumer {
}

@Subscriber( name="NamedSubscriberConsumer")
class NamedSubscriberConsumer {
}

@Worker( name="" )
class EmptyNamedWorkerConsumer {
}

@Subscriber( name="" )
class EmptyNamedSubscriberConsumer {
}