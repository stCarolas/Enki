package com.github.stcarolas.enki.logginghandlers;

import com.github.stcarolas.enki.core.Repo;
import com.github.stcarolas.enki.core.RepoHandler;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class LoggingAnalyzers implements RepoHandler {

	@Override
	public void handle(Repo repo) {
		log.info("Repo for analysis:{}", repo);
	}
}
