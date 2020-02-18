module com.github.stcarolas.enki.bitbucket {
	requires static lombok;
	requires bitbucket.rest;
	requires org.apache.logging.log4j;
	requires vavr;
	
	requires transitive com.github.stcarolas.enki.core;
	exports com.github.stcarolas.enki.bitbucket.provider;
}
