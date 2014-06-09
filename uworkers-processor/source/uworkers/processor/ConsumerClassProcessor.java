package uworkers.processor;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import trip.spi.helpers.filter.Filter;
import uworkers.api.Subscriber;
import uworkers.api.Worker;

@SupportedAnnotationTypes( "uworkers.*" )
public class ConsumerClassProcessor extends AbstractProcessor {

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		try {
			generateWorkerClasses( roundEnv );
			generateSubscriberClasses( roundEnv );
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		return false;
	}

	void generateWorkerClasses(RoundEnvironment roundEnv) throws IOException {
		for ( Element methodElement : retrieveMethodsAnnotatedWith( roundEnv, Worker.class ) )
			createAWorkerConsumerClassFrom( ConsumerClassData.from(methodElement) );
	}

	void createAWorkerConsumerClassFrom( ConsumerClassData clazz ) throws IOException {
		String templateName = "META-INF/worker-class.mustache";
		generateClassFrom(clazz, templateName);
	}

	void generateSubscriberClasses(RoundEnvironment roundEnv) throws IOException {
		for ( Element methodElement : retrieveMethodsAnnotatedWith( roundEnv, Subscriber.class ) )
			createAWorkerConsumerClassFrom( ConsumerClassData.from(methodElement) );
	}

	void createASubscriberConsumerClassFrom( ConsumerClassData clazz ) throws IOException {
		String templateName = "META-INF/subscriber-class.mustache";
		generateClassFrom(clazz, templateName);
	}

	@SuppressWarnings("unchecked")
	Iterable<Element> retrieveMethodsAnnotatedWith( RoundEnvironment roundEnv, Class<? extends Annotation> annotation )
			throws IOException {
		return (Iterable<Element>)Filter.filter(
				roundEnv.getElementsAnnotatedWith( annotation ),
				new MethodsOnlyCondition() );
	}

	void generateClassFrom(ConsumerClassData clazz, String templateName) throws IOException {
		ConsumerClassGenerator generator = new ConsumerClassGenerator(templateName, filer());
		generator.generate(clazz);
	}

	Filer filer() {
		return this.processingEnv.getFiler();
	}

	/**
	 * We just return the latest version of whatever JDK we run on. Stupid?
	 * Yeah, but it's either that or warnings on all versions but 1. Blame Joe.
	 * 
	 * PS: this method was copied from Project Lombok. ;)
	 */
	@Override
	public SourceVersion getSupportedSourceVersion() {
		return SourceVersion.values()[SourceVersion.values().length - 1];
	}
}
