package com.github.stcarolas.enki.logginganalyzers;

import com.github.stcarolas.enki.core.Repo;
import com.github.stcarolas.enki.core.RepoHandler;

public class HandlebarsGenerator implements RepoHandler {
	private final Generator generator;

	public HandlebarsGenerator(GenerationParameters parameters) {
		generator =
			Generator.builder()
				.cloneUrl(parameters.getCloneUrl())
				.data(parameters.getData())
				.mappings(parameters.getMapping())
				.saveParameters(false)
				.build();
	}

	@Override
	public void analyze(Repo repo) {
		generator.analyze(repo);
	}
}
