package uworkers.api;

import uworkers.core.endpoint.ParametrizedResponse;

public interface Consumer<T> extends Runnable {

	void handle(final ParametrizedResponse<T> received) throws Exception;
	
	Consumer<T> endpointName( String endpointName );

	String endpointName();
}