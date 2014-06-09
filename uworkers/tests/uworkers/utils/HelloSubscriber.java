package uworkers.utils;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.concurrent.CountDownLatch;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import trip.spi.Service;
import uworkers.api.Consumer;
import uworkers.api.WorkerException;
import uworkers.core.AbstractSubscriber;
import uworkers.core.endpoint.MQProvider;

@Getter
@Setter
@Accessors( fluent = true )
@RequiredArgsConstructor
@Service( Consumer.class )
public class HelloSubscriber extends AbstractSubscriber<Hello> {

	@NonNull String endpointName;
	final MQProvider mqProvider;
	final CountDownLatch counter;

	@Override
	public void handle( Hello receivedMessage ) throws WorkerException, InterruptedException {
		assertThat( receivedMessage.getWorld(), is( "WORLD" ) );
		counter.countDown();
		throw new InterruptedException( "Done!" );
	}
}
