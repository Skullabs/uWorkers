package uworkers.utils;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.concurrent.CountDownLatch;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import trip.spi.Singleton;
import uworkers.api.Consumer;
import uworkers.api.Name;
import uworkers.api.UWorkerException;
import uworkers.api.Worker;
import uworkers.core.AbstractWorker;
import uworkers.core.endpoint.MQProvider;

@Getter
@Setter
@Accessors( fluent = true )
@RequiredArgsConstructor
@Name("helloWorker")
@Singleton( exposedAs = Consumer.class )
@Worker( name = "test.worker" )
public class HelloWorker extends AbstractWorker<Hello> {

	@NonNull String endpointName;
	final MQProvider mqProvider;
	final CountDownLatch counter;

	@Override
	public void handle( Hello receivedMessage ) throws UWorkerException {
		assertThat( receivedMessage.getWorld(), is( "WORLD" ) );
		counter.countDown();
	}

	@Override
	public Class<Hello> getExpectedObjectClass() {
		return Hello.class;
	}
}
