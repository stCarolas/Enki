package com.github.stcarolas.enki.core;

import static io.vavr.API.Seq;
import static io.vavr.API.Try;

import java.util.function.Function;

import com.github.stcarolas.enki.core.hosting.RepoHosting;
import com.github.stcarolas.enki.core.repo.remote.RemoteRepo;
import com.github.stcarolas.enki.core.repo.remote.RemoteRepoFactory;

import io.micronaut.context.ApplicationContext;
import io.micronaut.core.util.CollectionUtils;
import io.vavr.collection.Seq;
import io.vavr.control.Try;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Log4j2
public class EnkiRunner {

	private final Seq<Function<RemoteRepoFactory, Try<Seq<RemoteRepo>>>> providers;

	public EnkiRunner withProvider(Function<RemoteRepoFactory, Try<Seq<RemoteRepo>>> provider){
		return new EnkiRunner(providers.append(provider));
	}

	public void run(){
		loadFactory()
			.map(factory -> providers.map(provider -> new RepoHosting(factory, provider)));
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

	public static EnkiRunner Enki(){
		return new EnkiRunner(Seq());
	}

}
