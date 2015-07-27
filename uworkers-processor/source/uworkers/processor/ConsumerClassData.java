package uworkers.processor;

import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

import trip.spi.processor.SingletonImplementation;
import uworkers.api.Subscriber;
import uworkers.api.Worker;

public class ConsumerClassData {

	final String packageName;

	final String name;

	final String consumer;

	final String consumerName;

	final String consumerMethod;

	final String type;

	final String typeName;

	final String endpointName;

	final String exposedAs;

	final boolean shouldSerializeBeforeSendOrReceiveObjects;

	public ConsumerClassData(final String packageName, final String name, final String consumer,
			final String consumerName, final String consumerMethod, final String type,
			final String typeName, final String endpointName, final String exposedAs,
			final boolean shouldSerializeBeforeSendOrReceiveObjects) {
		this.packageName = stripGenericsFrom(packageName);
		this.name = stripGenericsFrom(name);
		this.consumer = stripGenericsFrom(consumer);
		this.consumerName = stripGenericsFrom(consumerName);
		this.consumerMethod = stripGenericsFrom(consumerMethod);
		this.type = stripGenericsFrom(type);
		this.typeName = stripGenericsFrom(typeName);
		this.endpointName = endpointName;
		this.exposedAs = exposedAs;
		this.shouldSerializeBeforeSendOrReceiveObjects = shouldSerializeBeforeSendOrReceiveObjects;
	}

	public static ConsumerClassData from(final Element element) {
		final ExecutableElement method = assertElementIsMethod(element);
		final TypeElement typeElement = (TypeElement) method.getEnclosingElement();
		final String providerName = typeElement.getSimpleName().toString();
		final String provider = typeElement.asType().toString();
		final VariableElement variableElement = retrieveExpectedMessageTypeParameter(method);
		final String type = variableElement.asType().toString();
		return new ConsumerClassData(
				provider.replace("." + providerName, ""),
				extractNameFrom(element),
				provider,
				provider.replace(".", ""),
				method.getSimpleName().toString(),
				type, stripPackageName(type),
				extractEndpointNameFrom(method),
				SingletonImplementation.getProvidedServiceClassAsString(typeElement),
				extractSerializedAttributeFrom(element));
	}

	static String extractEndpointNameFrom(final ExecutableElement method) {
		String endpointName = extractWorkerEndpointNameFrom(method);
		if (endpointName == null)
			endpointName = extractSubscriberEndpointNameFrom(method);
		if (endpointName == null) {
			final String type = method.getEnclosingElement().asType().toString();
			throw new IllegalStateException("Unknown endpoint name for " + type + "." + method.getSimpleName().toString());
		}
		return endpointName;
	}

	static String extractWorkerEndpointNameFrom(final ExecutableElement method) {
		final Worker worker = method.getAnnotation(Worker.class);
		if (worker != null)
			return worker.name();
		return null;
	}

	static String extractSubscriberEndpointNameFrom(final ExecutableElement method) {
		final Subscriber subscriber = method.getAnnotation(Subscriber.class);
		if (subscriber != null)
			return subscriber.name();
		return null;
	}

	static VariableElement retrieveExpectedMessageTypeParameter(final ExecutableElement method) {
		final List<? extends VariableElement> parameters = method.getParameters();
		if (parameters.size() != 1)
			throw new IllegalStateException("Consumer method should have exactly one parameter.");
		return parameters.get(0);
	}

	static ExecutableElement assertElementIsMethod(final Element element) {
		return (ExecutableElement) element;
	}

	static String extractNameFrom(final Element element) {
		final Worker worker = element.getAnnotation(Worker.class);
		if (worker != null && !worker.name().isEmpty())
			return worker.name();
		final Subscriber subscriber = element.getAnnotation(Subscriber.class);
		if (subscriber != null && !subscriber.name().isEmpty())
			return subscriber.name();
		return null;
	}

	static String stripGenericsFrom(final String name) {
		if (name == null)
			return "";
		return name.replaceAll("<[^>]*>", "");
	}

	static String stripPackageName(final String canonicalName) {
		return canonicalName.replaceAll("^.*\\.([^\\.]+)", "$1");
	}

	static boolean extractSerializedAttributeFrom(final Element element) {
		final Worker worker = element.getAnnotation(Worker.class);
		if (worker != null)
			return worker.serialized();
		final Subscriber subscriber = element.getAnnotation(Subscriber.class);
		return subscriber.serialized();
	}
}
