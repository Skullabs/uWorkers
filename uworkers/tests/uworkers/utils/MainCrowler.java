package uworkers.utils;

import javax.jms.JMSException;

import trip.spi.Provided;
import uworkers.api.EndpointConnection;
import uworkers.api.Worker;

public class MainCrowler {
	
	@Provided
	@Worker("crowler.wiki")
	EndpointConnection wikipedia;

	@Worker("crowler.main")
	public void receiveHello( Hello hello ) throws JMSException{
		System.out.println(hello.getWorld());
		wikipedia.send(hello);
	}
}
