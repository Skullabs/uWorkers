package uworkers.utils;

import java.io.IOException;

import javax.jms.JMSException;

import trip.spi.Provided;
import uworkers.api.EndpointConnection;
import uworkers.api.Worker;

public class MainCrowler {
	
	@Provided
	@Worker( name = "crowler.wiki")
	EndpointConnection wikipedia;

	@Worker( name = "crowler.main")
	public void receiveHello( Hello hello ) throws JMSException, IOException{
		System.out.println(hello.getWorld());
		wikipedia.send(hello);
	}
}
