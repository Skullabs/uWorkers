package uworkers.utils;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;

public class CollectionsTest {

	final List<Integer> ints = Collections.list( 1, 2, 3, 4, 5, 6, 7, 8 );
	final Matcher<Integer> divisibleByTwo = new NumbersDivisibleByTwo();

	@Test
	public void grantThatIntListHasEightMembers() {
		assertThat( ints.size(), is( 8 ) );
	}

	@Test
	public void grantThatCanFilterTheIntegerList() {
		List<Integer> filteredIntegers = Collections.filter( ints, divisibleByTwo );
		assertThat( filteredIntegers.size(), is( 4 ) );
		Integer first = Collections.first( ints, divisibleByTwo );
		assertThat( first, is( 2 ) );
	}

	static class NumbersDivisibleByTwo implements Matcher<Integer> {

		@Override
		public boolean matches( Integer value ) {
			return value % 2 == 0;
		}
	}
}
