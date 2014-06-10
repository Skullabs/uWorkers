package uworkers.tests;

import java.io.Serializable;

import javax.jms.JMSException;

import lombok.extern.java.Log;
import trip.spi.Name;
import trip.spi.Provided;
import uworkers.api.EndpointConnection;
import uworkers.api.Subscriber;
import uworkers.api.Worker;

@Log
public class PingPongConsumers {

	@Provided
	@Subscriber( "pingpong.pong" )
	EndpointConnection pong;

	@Provided
	@Subscriber( "pingpong.pong.responses" )
	EndpointConnection pongResp;

	@Name( "ping" )
	@Worker( "ping" )
	public void receivePing( Ping ping ) throws JMSException {
		log.info( "Received ping." );
		log.fine( ping.toString() );
		pong.send( new Pong() );
	}

	@Name( "pong" )
	@Subscriber( "pong" )
	public void receivePong( Pong pong ) throws JMSException {
		log.info( "Received pong." );
		log.fine( pong.toString() );
		pongResp.send( pong );
	}

	public static class Ping implements Serializable {
		private static final long serialVersionUID = 361031655746891547L;
	}

	public static class Pong implements Serializable {
		private static final long serialVersionUID = 733280201722704996L;
	}
}
