package com.github.stcarolas.enki.gocd.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ConfigRepo {
	@JsonProperty("_links")
	Links links;

	@JsonProperty("id")
	private String id;

	@JsonProperty("plugin_id")
	private String pluginId;

	@JsonProperty("material")
	private Material material;

	@JsonProperty("configuration")
	private java.util.List<Object> configuration;

	@Data
	public static class Links {
		private Link self;
		private Link doc;
		private Link find;
	}
    
	@Data
	public static class List {
		@JsonProperty("config_repos")
		private java.util.List<ConfigRepo> repos;
	}
}
