package uworkers.api;

public interface Consumer<T> extends Runnable {

	void handle( T receivedMessage ) throws WorkerException, InterruptedException;

}