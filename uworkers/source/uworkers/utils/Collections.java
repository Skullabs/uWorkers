package uworkers.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Collections {

	public static <T> T first( final Collection<T> self, final Matcher<T> matcher ) {
		for ( final T object : self )
			if ( matcher.matches( object ) )
				return object;
		return null;
	}

	public static <T> List<T> filter( final List<T> self, final Matcher<T> matcher ) {
		final ArrayList<T> newList = new ArrayList<>();
		for ( final T object : self )
			if ( matcher.matches( object ) )
				newList.add( object );
		return newList;
	}

	@SafeVarargs
	public static <T> List<T> list( final T... objects ) {
		final ArrayList<T> newList = new ArrayList<>();
		for ( final T object : objects )
			newList.add( object );
		return newList;
	}
}
