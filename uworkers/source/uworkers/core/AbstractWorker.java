package uworkers.core;

import lombok.Getter;
import lombok.experimental.Accessors;
import uworkers.core.endpoint.MQProvider;
import uworkers.core.endpoint.WorkerEndpointConnection;

@Getter
@Accessors( fluent = true )
public abstract class AbstractWorker<T> extends AbstractConsumer<T> {

	@Getter( lazy = true ) private final WorkerEndpointConnection endpoint = new WorkerEndpointConnection( endpointName(), mqProvider() );

	protected abstract MQProvider mqProvider();
}
