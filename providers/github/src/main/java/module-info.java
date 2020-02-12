module com.github.stcarolas.enki.github {
	requires static lombok;
	requires com.github.stcarolas.enki.core;
	requires org.apache.logging.log4j;
	requires org.eclipse.egit.github.core;

	exports com.github.stcarolas.enki.github.provider;
}
