package com.github.stcarolas.enki.core;

import static io.vavr.API.Seq;
import static io.vavr.API.Try;
import java.util.function.Function;
import java.util.function.Supplier;
import com.github.stcarolas.enki.core.hosting.RepoHosting;
import com.github.stcarolas.enki.core.repo.local.LocalRepoHandler;
import com.github.stcarolas.enki.core.repo.remote.RemoteRepo;
import com.github.stcarolas.enki.core.repo.remote.RemoteRepoFactory;
import com.github.stcarolas.enki.core.repo.remote.RemoteRepoHandler;
import io.micronaut.context.ApplicationContext;
import io.micronaut.core.util.CollectionUtils;
import io.vavr.collection.Seq;
import io.vavr.control.Try;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import static io.vavr.API.*;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Log4j2
public class EnkiRunner {
	private final Seq<Supplier<Try<Seq<String>>>> providers;
	private final Seq<RemoteRepoHandler> remoteRepoHandlers;
	private final Seq<LocalRepoHandler> localRepoHandlers;

	public EnkiRunner withProvider(Supplier<Try<Seq<String>>> provider) {
		return new EnkiRunner(
			providers.append(provider),
			remoteRepoHandlers,
			localRepoHandlers
		);
	}

	public EnkiRunner withRemoteRepoHandler(RemoteRepoHandler handler) {
		return new EnkiRunner(
			providers,
			remoteRepoHandlers.append(handler),
			localRepoHandlers
		);
	}

	public EnkiRunner withLocalRepoHandler(LocalRepoHandler handler) {
		return new EnkiRunner(
			providers,
			remoteRepoHandlers,
			localRepoHandlers.append(handler)
		);
	}

	public void run() {
		loadFactory()
			.map(
				factory -> providers.map(provider -> new RepoHosting(factory, provider))
			);
	}

	private void handle(Seq<RepoHosting> hostings) {
		hostings.flatMap(
			hosting -> hosting.repositories()
				.onFailure(error -> log.error(error))
				.getOrElse(Seq())
		)
			.map(
				repository -> remoteRepoHandlers.foldLeft(
					Success(repository),
					(repo, handler) -> repo.flatMap(handler::handle)
				)
			)
			;
			//.map(repository -> repository.map($ -> $.toLocal()))
			//.map( (repo) -> repo.map(RemoteRepo::toLocal) );
	//.map(repo -> repo.toLocal());
	}

	private Try<RemoteRepoFactory> loadFactory() {
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

	public static EnkiRunner Enki() {
		return new EnkiRunner(Seq(), Seq(), Seq());
	}
}
