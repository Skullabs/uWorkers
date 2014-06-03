package uworkers.utils;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.concurrent.CountDownLatch;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import uworkers.api.WorkerException;
import uworkers.core.AbstractSubscriber;
import uworkers.core.endpoint.MQProvider;

@Getter
@Accessors( fluent = true )
@RequiredArgsConstructor
public class HelloSubscriber extends AbstractSubscriber<Hello> {

	final String endpointName;
	final MQProvider mqProvider;
	final CountDownLatch counter;

	@Override
	public void handle( Hello receivedMessage ) throws WorkerException, InterruptedException {
		assertThat( receivedMessage.getWorld(), is( "WORLD" ) );
		counter.countDown();
		throw new InterruptedException( "Done!" );
	}
}
