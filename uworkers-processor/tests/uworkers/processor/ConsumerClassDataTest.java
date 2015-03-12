package uworkers.processor;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class ConsumerClassDataTest {

	@Test
	public void ensureThatStripThePackageName() {
		String simpleName = ConsumerClassData.stripPackageName(String.class.getCanonicalName());
		assertThat( simpleName, is( String.class.getSimpleName() ));
	}
}
