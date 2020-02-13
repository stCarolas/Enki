module com.github.stcarolas.enki.gitea {
	requires static lombok;
	requires feign.core;
	requires feign.jackson;
	requires jackson.databind;
	requires jackson.datatype.jsr310;

	requires gitea.api;
	requires com.github.stcarolas.enki.core;
	exports com.github.stcarolas.enki.gitea.provider;
}
