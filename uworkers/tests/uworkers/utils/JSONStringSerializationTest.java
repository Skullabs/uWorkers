package uworkers.utils;

import static org.junit.Assert.assertEquals;
import lombok.SneakyThrows;
import lombok.val;

import org.junit.After;
import org.junit.Test;

import trip.spi.Provided;
import uworkers.api.EndpointConnection;
import uworkers.api.Worker;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONStringSerializationTest extends TestCase {

	final static String STRING = "My dog bit me.";

	final static String SERIALIZED_STRING = "\"My dog bit me.\"";

	@Provided
	@Worker(name = "string-endpoint")
	EndpointConnection consumer;

	@Test
	@SneakyThrows
	public void ensureThatCouldSerializeAString() {
		val mapper = new ObjectMapper();
		Object o = STRING;
		assertEquals(SERIALIZED_STRING, mapper.writeValueAsString(o));
	}

	@Test(timeout = 10000)
	@SneakyThrows
	public void ensureThatCanSendStringAndReceiveSameString() {
		consumer.startAndListenMessages();
		consumer.send(STRING);
		val receivedMessage = consumer.receive(String.class);
		assertEquals(STRING, receivedMessage);
	}

	@Test(timeout = 10000)
	@SneakyThrows
	public void ensureThatCanSendStringAndReceiveUnserializedString() {
		consumer.startAndListenMessages();
		consumer.send(STRING);
		consumer.shouldSerializeBeforeSendOrReceiveObjects(false);
		val receivedMessage = consumer.receive(String.class);
		assertEquals(SERIALIZED_STRING, receivedMessage);
	}

	@After
	public void closeConsumers() {
		consumer.stop();
	}
}