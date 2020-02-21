module com.github.stcarolas.enki {
	requires static lombok;

	requires undertow.core;
	requires info.picocli;
	requires org.apache.logging.log4j;
	requires jcl.core;
	requires gson;
	requires vavr;

	requires com.github.stcarolas.enki.core;
	requires com.github.stcarolas.enki.github;
	requires com.github.stcarolas.enki.onerepo;
	requires com.github.stcarolas.enki.bitbucket;

}
