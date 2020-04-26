module com.github.stcarolas.enki.core {
	requires static lombok;

	requires transitive org.eclipse.jgit;
	requires io.micronaut.core;
	requires io.micronaut.inject;
	requires org.apache.logging.log4j;
	requires transitive vavr;
	requires dagger;
	requires javax.inject;
	requires java.compiler;
	requires jsch;
	requires com.github.stcarolas.enrichedbeans.annotations;
}
