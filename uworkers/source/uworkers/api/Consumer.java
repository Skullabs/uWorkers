package uworkers.api;

public interface Consumer<T> extends Runnable {

	void handle( T receivedMessage ) throws UWorkerException, InterruptedException;
	
	Consumer<T> endpointName( String endpointName );

	String endpointName();
}