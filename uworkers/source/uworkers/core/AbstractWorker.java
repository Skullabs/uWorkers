package uworkers.core;

import lombok.Getter;
import lombok.experimental.Accessors;
import uworkers.core.endpoint.MQProvider;
import uworkers.core.endpoint.WorkerEndpoint;

@Getter
@Accessors( fluent = true )
public abstract class AbstractWorker<T> extends AbstractConsumer<T> {

	final WorkerEndpoint endpoint = new WorkerEndpoint( endpointName(), mqProvider() );

	protected abstract String endpointName();

	protected abstract MQProvider mqProvider();
}
