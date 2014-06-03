package uworkers.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Collections {

	public static <T> T first( Collection<T> self, Matcher<T> matcher ) {
		for ( T object : self )
			if ( matcher.matches( object ) )
				return object;
		return null;
	}

	public static <T> List<T> filter( List<T> self, Matcher<T> matcher ) {
		ArrayList<T> newList = new ArrayList<>();
		for ( T object : self )
			if ( matcher.matches( object ) )
				newList.add( object );
		return newList;
	}

	@SafeVarargs
	public static <T> List<T> list( T... objects ) {
		ArrayList<T> newList = new ArrayList<>();
		for ( T object : objects )
			newList.add( object );
		return newList;
	}
}
