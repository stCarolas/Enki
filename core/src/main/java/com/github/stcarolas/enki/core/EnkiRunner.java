package com.github.stcarolas.enki.core;

import java.util.List;
import java.util.Objects;

import lombok.Builder;
import lombok.Singular;
import lombok.extern.log4j.Log4j2;

@Builder
@Log4j2
public class EnkiRunner<T extends Repo> {
	@Singular
	private List<RepoProvider<T>> providers;

	@Singular
	private List<RepoHandler<T>> handlers;

	public void handle() {
		if (Objects.isNull(providers) || Objects.isNull(handlers)){
			log.info("no providers or handlers, exiting");
			return;
		}
		providers.stream()
			.flatMap(provider -> provider.getRepos().stream())
			.forEach(repo -> handlers.forEach(handler -> handler.handle(repo)));
	}
}
