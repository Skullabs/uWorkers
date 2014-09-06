package uworkers.utils;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.CountDownLatch;

import javax.jms.JMSException;

import lombok.val;

import org.junit.Test;

import trip.spi.Provided;
import trip.spi.ServiceProviderException;
import uworkers.api.EndpointConnection;
import uworkers.api.Worker;
import uworkers.api.WorkerService;
import uworkers.core.endpoint.MQProvider;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


public class AsyncEndpointConsumptionTest extends TestCase {

	final static int TOTAL_OF_MSGS = 10;
	final WorkerService service = WorkerService.newInstance();
	final CountDownLatch counter = new CountDownLatch( TOTAL_OF_MSGS );

	@Provided MQProvider mqProvider;
	@Provided @Worker( name = "worker.test", queue = "worker.test" ) EndpointConnection worker;

	@Override
	public void setup() throws ServiceProviderException {
		assertThat( mqProvider, notNullValue() );
		HelloWorker helloWorker = new HelloWorker( "worker.test", mqProvider, counter );
		service.start( helloWorker );
	}

	@Test
	public void grantThatSendAndReceiveDataFromWorkerEndpoint() throws JMSException, InterruptedException, IOException {
		assertThat( worker, notNullValue() );
		for ( int i = 0; i < TOTAL_OF_MSGS; i++ )
			worker.send( new Hello( "WORLD" ) );
		counter.await();
	}
	/*
	@Test
	public void testJson() throws IOException{
		ObjectMapper mapper = new ObjectMapper();
		Hello h = new Hello("World");		
		val s =  mapper.writeValueAsString( h );
		try{
		val j = mapper.readValue( s, Hello.class);
		System.out.println(j.toString());
		}catch(Exception e){
			System.err.println(e.getMessage());
		}
		
	}*/
}
