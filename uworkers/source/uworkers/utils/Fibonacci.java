package uworkers.utils;

import java.util.Iterator;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor( staticName = "until" )
public class Fibonacci implements Iterable<Integer>, Iterator<Integer> {

	int last;
	int current;

	final int limit;

	@Override
	public Iterator<Integer> iterator() {
		last = 0;
		current = 1;
		return this;
	}

	@Override
	public boolean hasNext() {
		return current <= limit;
	}

	@Override
	public Integer next() {
		int sum = last + current;
		last = current;
		current = sum;
		return sum;
	}

	@Override
	public void remove() {
	}
}
