package com.github.stcarolas.enki.core;

import static io.vavr.API.*;

import com.github.stcarolas.enki.core.hosting.RepoHosting;
import com.github.stcarolas.enki.core.repo.remote.RemoteRepoFactory;

import io.micronaut.context.ApplicationContext;
import io.vavr.collection.Seq;
import io.vavr.control.Try;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import static io.vavr.API.*;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Log4j2
@Getter(AccessLevel.PRIVATE)
public class EnkiRunner {

	private final Seq<RepoHosting> providers;

	public static void main(String args[]) {
		Try(ApplicationContext::run)
			.map(context -> context.getBean(RemoteRepoFactory.class))
			.peek(context -> println(context))
			.map( factory -> factory.remote(null, "git@github.com:stCarolas/nvim-configs.git"))
			.onFailure( error -> log.error(error) )
			.forEach(remote -> remote.toLocal());
	}

	//public Seq<Try<T>> run() {
		//return providers
			//.onEmpty(() -> log.error("missing any RepoProvider"))
			//.peek(first -> log.info("calling repository providers"))
			//.map(this::loadRepositories)
			//.flatMap(list -> list.getOrElse(Seq()))
			//.peek(first -> log.info("calling repository handlers"))
			//.flatMap(this::runHandlersOnRepo);
	//}

	//private Seq<Try<T>> runHandlersOnRepo(T repository) {
		//return handlers
			//.onEmpty(() -> log.error("missing any RepoHandler"))
			//.map(handler -> Option(handler)
				//.map($ -> $.handle(repository))
				//.getOrElse(Failure(new RuntimeException("Nullable RepoHandler was provided")))
			//);
	//}

	//private Try<Seq<T>> loadRepositories(RepoProvider<T> repoProvider){
		//return Option(repoProvider)
			//.map(RepoProvider::repositories)
			//.map( 
				//list -> list.onFailure( 
					//error -> log.error(
						//"Exception while getting list of repositories: {}",
						//error.getMessage()
					//)
				//)
			//)
			//.getOrElse(Failure(new RuntimeException("Nullable repoProvider was provided")));
	//}

	//public static <T extends Repo> EnkiRunner<T> Enki() {
		//return new EnkiRunner<>(Seq(), Seq());
	//}

	//public EnkiRunner<T> withProvider(RepoProvider<T> provider) {
		//return Option(provider)
			//.map(providers::append)
			//.map(changedProviders -> new EnkiRunner<T>(changedProviders, handlers))
			//.getOrElse(this);
	//}

	//public EnkiRunner<T> withHandler(RepoHandler<T> handler) {
		//return Option(handler)
			//.map(handlers::append)
			//.map(changedHandlers -> new EnkiRunner<T>(providers, changedHandlers))
			//.getOrElse(this);
	//}
}
