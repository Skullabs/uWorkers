package uworkers.tests;

import java.io.IOException;
import java.io.Serializable;

import javax.jms.JMSException;

import lombok.extern.java.Log;
import trip.spi.Provided;
import uworkers.api.EndpointConnection;
import uworkers.api.Subscriber;
import uworkers.api.Worker;

@Log
public class PingPongConsumers {

	@Provided
	@Subscriber( name = "pingpong.pong",topic = "pingpong.pong" )
	EndpointConnection pong;

	@Provided
	@Subscriber( name = "pingpong.pong.responses", topic = "pingpong.pong.responses" )
	EndpointConnection pongResp;

	@Worker( queue = "ping", name = "ping" )
	public void receivePing( Ping ping ) throws JMSException, IOException {
		log.info( "Received ping." );
		log.fine( ping.toString() );
		pong.send( new Pong() );
	}

	@Subscriber( topic = "pong", name = "pong" )
	public void receivePong( Pong pong ) throws JMSException, IOException {
		log.info( "Received pong." );
		log.fine( pong.toString() );
		pongResp.send( pong );
	}

	public static class Ping implements Serializable {
		private static final long serialVersionUID = 361031655746891547l;
	}

	public static class Pong implements Serializable {
		private static final long serialVersionUID = 733280201722704996L;
	}
}
