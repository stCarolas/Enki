package com.github.stcarolas.enki.core;

import static com.github.stcarolas.enki.core.util.Lifting.call;
import static io.vavr.collection.List.empty;
import static io.vavr.control.Option.of;

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

	private final Seq<RepoProvider<T>> providers;
	private final Seq<RepoHandler<T>> handlers;

	public void run() {
		providers
			.onEmpty(() -> log.error("missing any RepoProvider"))
			.peek(first -> log.info("calling repository providers"))

			.flatMap(
				provider -> call(provider::repositories)
					.onEmpty(() -> log.error("{} cant provide list of repositories", provider))
					.peek( list -> log.info("{} provide {} repositories", provider, list.size()) )
					.getOrElse(empty())

			)

			.onEmpty(() -> log.error("no repository provided at all"))
			.peek( first -> log.info("calling repository handlers") )

			.flatMap(
				repo -> Option.sequence(this.runHandlersOnRepo(repo))
					.peek(it -> log.info("repo {} was successfully handled by all handlers", repo))
					.onEmpty(() -> log.error("repository {} was handled with one or more error", repo))
			)

			.onEmpty(() -> log.error("no successfull handled repository"));
	}

	private Seq<Option<T>> runHandlersOnRepo(T repository) {
		return handlers
			.onEmpty(() -> log.error("missing any RepoHandler"))
			.map(
				handler -> call(() -> handler.handle(repository))
					.peek(it -> log.info("repository {} was successfully handled by {}", repository, handler))
					.onEmpty(() -> log.error("{} was unable to handle repository", handler.getClass()))
			);
	}

	public static <T extends Repo> EnkiRunner<T> enki() {
		return new EnkiRunner<>(empty(), empty());
	}

	public EnkiRunner<T> withProvider(RepoProvider<T> provider) {
		return of(provider)
			.map(providers::append)
			.map(changedProviders -> new EnkiRunner<T>(changedProviders, handlers))
			.getOrElse(this);
	}

	public EnkiRunner<T> withHandler(RepoHandler<T> handler) {
		return of(handler)
			.map(handlers::append)
			.map(changedHandlers -> new EnkiRunner<T>(providers, changedHandlers))
			.getOrElse(this);
	}
}
