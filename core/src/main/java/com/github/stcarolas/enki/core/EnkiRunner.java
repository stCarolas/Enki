package com.github.stcarolas.enki.core;

import static com.github.stcarolas.enki.core.util.FunctionCaller.option;
import static com.github.stcarolas.enki.core.util.FunctionCaller.seq;
import static io.vavr.collection.List.empty;
import static io.vavr.control.Option.some;

import io.vavr.collection.Seq;
import io.vavr.control.Option;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Log4j2
@Getter(AccessLevel.PRIVATE)
public class EnkiRunner<T extends Repo> {

	private Seq<RepoProvider<T>> providers;
	private Seq<RepoHandler<T>> handlers;

	public void run() {
		providers
			.onEmpty(() -> log.error("missing any RepoProvider"))
			.peek( provider -> log.info("calling repository providers") )

			.flatMap(
				provider -> seq(provider::repositories)
					.onEmpty(() -> log.error("{} provide empty list of repositories", provider))
			)

			.onEmpty(() -> log.error("no repository provided at all"))
			.peek(repo -> log.info("calling repository handlers") )

			.flatMap( 
				repo -> Option.sequence(this.runHandlersOnRepo(repo))
					.peek( results -> log.info("repo {} was successfully handled", repo))
					.onEmpty(() -> log.error("repository {} was handled with one or more error", repo))
			)

			.onEmpty(() -> log.error("no successfull handled repository"));
	}

	private Seq<Option<T>> runHandlersOnRepo(T repository) {
		return handlers
			.onEmpty(() -> log.error("missing any RepoHandler"))
			.map(
				handler -> option(() -> handler.handle(repository))
					.onEmpty(() -> log.error("{} was unable to handle repo", handler.getClass()))
			);
	}

	public static <T extends Repo> EnkiRunner<T> enki() {
		return new EnkiRunner<>(empty(), empty());
	}

	public EnkiRunner<T> withProvider(RepoProvider<T> provider) {
		return some(provider)
			.map(providers::append)
			.map(changedProviders -> new EnkiRunner<T>(changedProviders, handlers))
			.getOrElse(this);
	}

	public EnkiRunner<T> withHandler(RepoHandler<T> handler) {
		return some(handler)
			.map(handlers::append)
			.map(changedHandlers -> new EnkiRunner<T>(providers, changedHandlers))
			.getOrElse(this);
	}
}
