package com.github.stcarolas.enki.core;

import static io.vavr.API.Try;
import static io.vavr.API.Seq;
import static io.vavr.API.For;
import static io.vavr.API.println;

import java.util.function.Function;

import com.github.stcarolas.enki.core.hosting.RepoHosting;
import com.github.stcarolas.enki.core.repo.remote.RemoteRepo;
import com.github.stcarolas.enki.core.repo.remote.factories.RemoteRepoFactory;

import io.micronaut.context.ApplicationContext;
import io.micronaut.context.env.PropertySource;
import io.micronaut.core.util.CollectionUtils;
import io.vavr.collection.Seq;
import io.vavr.control.Try;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Log4j2
public class EnkiRunner {

	private final Seq<Function<RemoteRepoFactory, Try<Seq<RemoteRepo>>>> providers;

	public static void main(String args[]) {
		var enki = new EnkiRunner(Seq());
		//factory
			//.map(factory -> factory.remote(null, "git@github.com:stCarolas/nvim-configs.git"))
			//.onFailure(error -> log.error(error))
			//.forEach(remote -> remote.toLocal());
	}

	public void run(){
		loadFactory()
			.map(factory -> providers.map( fn -> new RepoHosting(factory, fn) ));
	}


	private Try<RemoteRepoFactory> loadFactory(){
		return Try(
				() -> ApplicationContext.builder()
					.deduceEnvironment(false)
					.properties(CollectionUtils.mapOf("protocol", "ssh"))
					.start()
			)
			.map(context -> context.getBean(RemoteRepoFactory.class))
			.onFailure(
				error -> log.error("Cant create RepoFactory: {}", error.getMessage())
			);
	}
}
