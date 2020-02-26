package com.github.stcarolas.enki.core;

import static com.github.stcarolas.enki.core.util.Lifting.call;
import static io.vavr.API.*;

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

	public Seq<Option<T>> run() {
		log.info("\t==== Enki ===");
		var run = providers
			.onEmpty(() -> log.error("missing any RepoProvider"))
			.peek(first -> log.info("calling repository providers"))

			.flatMap(
				provider -> call(provider::repositories)
					.onEmpty(() -> log.error("{} cant provide list of repositories", 
							provider.describe())
					)
					.peek( list -> log.info("{} provide {} repositories", 
							provider.describe(), 
							list.size()) 
					)
					.getOrElse(Seq())
			)

			.onEmpty(() -> log.error("no repository provided at all"))
			.peek( first -> log.info("calling repository handlers") )

			.flatMap(this::runHandlersOnRepo)
			.onEmpty(() -> log.error("no successfull handled repository"));
		log.info("\t-------------\n");
		return run;
	}

	private Seq<Option<T>> runHandlersOnRepo(T repository) {
		return handlers
			.onEmpty(() -> log.error("missing any RepoHandler"))
			.map(
				handler -> call(() -> handler.handle(repository))
					.peek(it -> log.info("repository {} was successfully handled by {}", repository, handler))
					.onEmpty(() -> log.error("{} was unable to handle repository", handler.describe()))
			);
	}

	public static <T extends Repo> EnkiRunner<T> enki() {
		log.info("### construct Enki");
		return new EnkiRunner<>(Seq(), Seq());
	}

	public EnkiRunner<T> withProvider(RepoProvider<T> provider) {
		return Option(provider)
			.onEmpty(() -> log.info("### null provider was defined, ignore it"))
			.peek( it -> log.info("### adding {}", 
				Option(it.describe()).getOrElse("Unknown provider, maybe Mock"))
			)
			.map(providers::append)
			.map(changedProviders -> new EnkiRunner<T>(changedProviders, handlers))
			.getOrElse(this);
	}

	public EnkiRunner<T> withHandler(RepoHandler<T> handler) {
		return Option(handler)
			.onEmpty(() -> log.info("### null handler was defined, ignore it"))
			.peek( it -> log.info("### adding {}", 
				Option(it.describe()).getOrElse("Unknown handler, maybe Mock"))
			)
			.map(handlers::append)
			.map(changedHandlers -> new EnkiRunner<T>(providers, changedHandlers))
			.getOrElse(this);
	}
}
