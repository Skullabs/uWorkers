package uworkers.api;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import trip.spi.helpers.filter.Condition;
import trip.spi.helpers.filter.Filter;

@RequiredArgsConstructor
public class NamedConsumerCondition<T> implements Condition<T> {

	@NonNull
	final String expectedName;

	@Override
	public boolean check(T object) {
		final Name name = object.getClass().getAnnotation(Name.class);
		return name != null && expectedName.equals( name.value() );
	}

	public static <T> Iterable<T> filterByName( Iterable<T> data, String name ) {
		return Filter.filter(data, new NamedConsumerCondition<>(name));
	}
}
