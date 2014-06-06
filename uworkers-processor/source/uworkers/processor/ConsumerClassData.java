package uworkers.processor;

import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

import trip.spi.Name;

public class ConsumerClassData {

	final String packageName;
	final String name;
	final String consumer;
	final String consumerName;
	final String consumerMethod;
	final String type;
	final String typeName;

	public ConsumerClassData(String packageName, String name, String consumer,
			String consumerName, String consumerMethod, String type,
			String typeName) {
		this.packageName = stripGenericsFrom( packageName );
		this.name = stripGenericsFrom( name );
		this.consumer = stripGenericsFrom( consumer );
		this.consumerName = stripGenericsFrom( consumerName );
		this.consumerMethod = stripGenericsFrom( consumerMethod );
		this.type = stripGenericsFrom( type );
		this.typeName = stripGenericsFrom( typeName );
	}

	public static ConsumerClassData from( Element element ) {
		ExecutableElement method = assertElementIsMethod( element );
		String providerName = method.getEnclosingElement().getSimpleName().toString();
		String provider = method.getEnclosingElement().asType().toString();
		VariableElement variableElement = retrieveExpectedMessageTypeParameter(method);
		String type = variableElement.asType().toString();
		String typeName = variableElement.getSimpleName().toString();
		return new ConsumerClassData(
				provider.replace( "." + providerName, "" ),
				extractNameFrom( element ),
				provider,
				provider.replace( ".", "Dot" ),
				method.getSimpleName().toString(),
				type, typeName);
	}

	static VariableElement retrieveExpectedMessageTypeParameter(ExecutableElement method) {
		List<? extends VariableElement> parameters = method.getParameters();
		if ( parameters.size() != 1 )
			throw new IllegalStateException("Consumer method should have exactly one parameter.");
		return parameters.get(0);
	}

	static ExecutableElement assertElementIsMethod( Element element ) {
		return (ExecutableElement)element;
	}

	static String extractNameFrom( Element element ) {
		Name name = element.getAnnotation( Name.class );
		if ( name != null )
			return name.value();
		return null;
	}

	static String stripGenericsFrom( String name ) {
		if ( name == null )
			return "";
		return name.replaceAll("<[^>]*>", "");
	}
}
