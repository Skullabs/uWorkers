package uworkers.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target( { ElementType.METHOD, ElementType.FIELD, ElementType.TYPE } )
@Retention( RetentionPolicy.RUNTIME )
public @interface Subscriber {
	boolean serialized() default true;
	String topic() default "";
	String name();
}
