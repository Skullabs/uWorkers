package uworkers.utils;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import lombok.val;

import org.junit.Test;

//@Checkstyle:OFF ignore numbers constants
public class FibonacciTest {

	final Fibonacci fibonacci = Fibonacci.until( 21 );

	@Test
	public void grantThatGenerateTheFirstValuesAsExpected(){
		val iterator = fibonacci.iterator();
		assertThat( iterator.next() , is( 1 ) );
		assertThat( iterator.next() , is( 2 ) );
		assertThat( iterator.next() , is( 3 ) );
		assertThat( iterator.next() , is( 5 ) );
		assertThat( iterator.next() , is( 8 ) );
		assertThat( iterator.next() , is( 13 ) );
		assertThat( iterator.next() , is( 21 ) );
	}

	@Test
	public void grantThatStressedFibonacciCodeStillsProducingSameResults(){
		for ( int i=0; i<100000; i++ )
			grantThatGenerateTheFirstValuesAsExpected();
	}
	
	@Test
	public void grantThatGenerateFibonacciInfiniteSequence(){
		assertThat( fibonacci.next() , is( 1 ) );
		assertThat( fibonacci.next() , is( 2 ) );
		assertThat( fibonacci.next() , is( 3 ) );
		assertThat( fibonacci.next() , is( 5 ) );
		assertThat( fibonacci.next() , is( 8 ) );
		assertThat( fibonacci.next() , is( 13 ) );
		assertThat( fibonacci.next() , is( 21 ) );
		assertThat( fibonacci.next() , is( 34 ) );
		assertThat( fibonacci.next() , is( 55 ) );
	}
	
}
