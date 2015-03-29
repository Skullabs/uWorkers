package uworkers.tests;

import java.io.IOException;
import java.io.Serializable;

import javax.jms.JMSException;

import lombok.Getter;
import lombok.extern.java.Log;
import trip.spi.Provided;
import trip.spi.Singleton;
import uworkers.api.EndpointConnection;
import uworkers.api.Subscriber;
import uworkers.api.Worker;

@Log
@Singleton
public class PingPongConsumers {

	@Provided
	@Subscriber( name = "pingpong.pong", topic = "pingpong.pong" )
	EndpointConnection pong;

	@Provided
	@Subscriber( name = "pingpong.pong.responses", topic = "pingpong.pong.responses" )
	EndpointConnection pongResp;

	@Worker( queue = "ping", name = "ping" )
	public void receivePing( final Ping ping ) throws JMSException, IOException {
		log.info( "Received ping." );
		log.fine( ping.toString() );
		pong.send( new Pong() );
	}

	@Subscriber( topic = "pong", name = "pong" )
	public void receivePong( final Pong pong ) throws JMSException, IOException {
		log.info( "Received pong." );
		log.fine( pong.toString() );
		pongResp.send( pong );
	}

	@Getter
	public static class Ping implements Serializable {
		private static final long serialVersionUID = 361031655746891547l;
		String something;
	}

	@Getter
	public static class Pong implements Serializable {
		private static final long serialVersionUID = 733280201722704996L;
		String something;
	}
}
