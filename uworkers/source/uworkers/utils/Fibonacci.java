package uworkers.utils;

import java.util.Iterator;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "until")
public class Fibonacci implements Iterable<Integer>, Iterator<Integer> {

	int last = 0;

	int current = 1;

	final int limit;

	@Override
	public Iterator<Integer> iterator() {
		reset();
		return this;
	}

	public void reset() {
		last = 0;
		current = 1;
	}
	
	public int current() {
		return current;
	}

	@Override
	public boolean hasNext() {
		return current <= limit;
	}

	@Override
	public Integer next() {
		final int sum = last + current;
		last = current;
		current = sum;
		return sum;
	}

	@Override
	public void remove() {
	}
}
