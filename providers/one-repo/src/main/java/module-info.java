module com.github.stcarolas.enki.onerepo {
	requires static lombok;
	requires com.github.stcarolas.enki.core;
	requires org.apache.logging.log4j;
	requires vavr;

	exports com.github.stcarolas.enki.onerepo.provider;
}
