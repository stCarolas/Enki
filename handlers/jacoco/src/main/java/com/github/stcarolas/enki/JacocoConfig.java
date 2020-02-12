package com.github.stcarolas.enki;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class JacocoConfig {
	private Map<String, Object> config;

	public JacocoConfig() {
		this.config = new HashMap();
		this.config.put("groupId", "org.jacoco");
		this.config.put("artifactId", "jacoco-maven-plugin");
		this.config.put("version", "0.8.4");
		this.config.put("executions", executions());
	}

	private List<MavenPluginExecution> executions() {
		return Arrays.asList(
			prepareAgent(), preparePackage(), check()
		);
	}

	private MavenPluginExecution check() {
		return MavenPluginExecution.builder()
			.goals(Arrays.asList("report"))
			.phase("test")
			.id("default-check")
			.inherited(true)
			.priority(0)
			.build();
	}

	private MavenPluginExecution preparePackage() {
		return MavenPluginExecution.builder()
			.goals(Arrays.asList("report"))
			.phase("prepare-package")
			.id("default-report")
			.inherited(true)
			.priority(0)
			.build();
	}

	private MavenPluginExecution prepareAgent() {
		return MavenPluginExecution.builder()
			.goals(Arrays.asList("prepare-agent"))
			.id("default-prepare-agent")
			.inherited(true)
			.priority(0)
			.build();
	}
}
