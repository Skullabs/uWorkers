package uworkers.core;

import lombok.Getter;
import lombok.experimental.Accessors;
import uworkers.core.endpoint.MQProvider;
import uworkers.core.endpoint.SubscriberEndpoint;

@Getter
@Accessors( fluent = true )
public abstract class AbstractSubscriber<T> extends AbstractConsumer<T> {

	final SubscriberEndpoint endpoint = new SubscriberEndpoint( endpointName(), mqProvider() );

	protected abstract String endpointName();

	protected abstract MQProvider mqProvider();
}
