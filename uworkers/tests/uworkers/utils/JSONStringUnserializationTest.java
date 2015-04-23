package uworkers.utils;

import static org.junit.Assert.assertEquals;
import lombok.SneakyThrows;
import lombok.val;

import org.junit.After;
import org.junit.Test;

import trip.spi.Provided;
import uworkers.api.EndpointConnection;
import uworkers.api.Worker;

public class JSONStringUnserializationTest extends TestCase {

	final static String STRING = "My dog bit me.";
	final static String SERIALIZED_STRING = "\"My dog bit me.\"";

	@Provided
	@Worker(name = "unserialized-string-endpoint")
	EndpointConnection consumer;

	@Test(timeout = 10000)
	@SneakyThrows
	public void ensureThatCanSendStringAndReceiveTheSameUnserializedString() {
		Thread.sleep(1000l);
		consumer.startAndListenMessages();
		consumer.shouldSerializeBeforeSendOrReceiveObjects(false);
		consumer.send(STRING);
		val receivedMessage = consumer.receive(String.class);
		assertEquals(STRING, receivedMessage);
	}

	@After
	public void closeConsumers() {
		consumer.stop();
	}
}