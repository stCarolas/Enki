module com.github.stcarolas.enki.core {
	requires static lombok;

	requires transitive org.eclipse.jgit;
	requires org.apache.logging.log4j;
	requires transitive vavr;
	requires jsch;

	exports com.github.stcarolas.enki.core;
	exports com.github.stcarolas.enki.core.repo;
	exports com.github.stcarolas.enki.core.provider;
	exports com.github.stcarolas.enki.core.util;
}
