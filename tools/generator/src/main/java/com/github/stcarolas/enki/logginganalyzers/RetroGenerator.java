package com.github.stcarolas.enki.logginganalyzers;

import java.io.File;
import java.io.FileReader;
import com.github.stcarolas.enki.core.Repo;
import com.github.stcarolas.enki.core.RepoHandler;
import com.google.gson.Gson;
import io.vavr.control.Try;
import lombok.val;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class RetroGenerator implements RepoHandler {

	@Override
	public void analyze(Repo repo) {
		regenerate(repo);
	}

	private void regenerate(Repo repo) {
		log.info("Start to regenerate repo");
		repo.getDirectory()
			.ifPresent(
				dir -> {
					val enkiDir = new File(dir.toString() + "/.enki");
					log.info("Analyze {}", enkiDir.toString());
					if (enkiDir.exists()) {
						for (val generation : enkiDir.listFiles()) {
							log.info("Load settings from {}", generation.toString());
							val gson = new Gson();
							Try.of(
								() -> {
									return gson.fromJson(
										new FileReader(generation),
										GenerationParameters.class
									);
								}
							)
								.onSuccess(
									parameters -> {
										log.info("Loaded parameters: {}", parameters);
										new HandlebarsGenerator(parameters).analyze(repo);
									}
								)
								.onFailure(error -> log.error("Error in RetroGenerator: {}", error));
						}
					}
				}
			);
	}
}
