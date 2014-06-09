package uworkers.core;

import lombok.Getter;
import lombok.experimental.Accessors;
import uworkers.core.endpoint.MQProvider;
import uworkers.core.endpoint.SubscriberEndpointConnection;

@Getter
@Accessors( fluent = true )
public abstract class AbstractSubscriber<T> extends AbstractConsumer<T> {

	@Getter( lazy = true ) private final SubscriberEndpointConnection endpoint = new SubscriberEndpointConnection( endpointName(), mqProvider() );

	protected abstract MQProvider mqProvider();
}
