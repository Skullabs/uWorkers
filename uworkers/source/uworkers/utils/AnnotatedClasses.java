package uworkers.utils;

import java.lang.annotation.Annotation;

import lombok.RequiredArgsConstructor;
import trip.spi.helpers.filter.Condition;

@RequiredArgsConstructor( staticName="with" )
public class AnnotatedClasses<T> implements Condition<Class<T>> {

	final Class<? extends Annotation> annotation;

	@Override
	public boolean check( final Class<T> object ) {
		return object.isAnnotationPresent( annotation );
	}
}
