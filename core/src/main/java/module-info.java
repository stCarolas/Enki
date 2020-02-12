module com.github.stcarolas.enki.core {
	requires static lombok;

	requires org.eclipse.jgit;
	requires org.apache.logging.log4j;
	requires io.vavr;
	requires jsch;

	exports com.github.stcarolas.enki.core;
	exports com.github.stcarolas.enki.core.functions;
	exports com.github.stcarolas.enki.core.impl;
}
