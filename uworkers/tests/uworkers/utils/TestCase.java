package uworkers.utils;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

import org.junit.After;
import org.junit.Before;

import trip.spi.ServiceProvider;

@Getter
@Accessors( fluent = true )
public class TestCase {

	final ServiceProvider provider = new ServiceProvider();

	@Before
	public void provideMyDependencies() throws Exception {
		provider.provideOn( this );
		setup();
	}

	public void setup() throws Exception {
	}

	@After
	public void runTearDown() throws Exception {
		tearDown();
	}

	public void tearDown() throws Exception {
	}

	@SneakyThrows
	public <T> T provideOn( T target ) {
		provider.provideOn( target );
		return target;
	}
}
