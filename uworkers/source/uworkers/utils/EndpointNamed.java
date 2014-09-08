package uworkers.utils;

import lombok.RequiredArgsConstructor;
import trip.spi.helpers.filter.Condition;
import uworkers.core.config.EndpointConsumerConfiguration;

@RequiredArgsConstructor(staticName="with")
public class EndpointNamed implements Condition<EndpointConsumerConfiguration> {

	final String name;

	@Override
	public boolean check( final EndpointConsumerConfiguration object ) {
		return name.equals( object.getName() );
	}
}
