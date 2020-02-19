package com.github.stcarolas.enki.core.provider.strategies.upload;

import static com.github.stcarolas.enki.core.util.FunctionCaller.option;
import static io.vavr.control.Option.some;

import java.util.function.Supplier;

import com.github.stcarolas.enki.core.Repo;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.PushResult;

import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GitPushUploadStrategy<T extends Repo> implements Supplier<Option<Iterable<PushResult>>> {
	private final Option<String> sshUrl;
	private final Option<T> repository;

	@Override
	public Option<Iterable<PushResult>> get() {
		return sshUrl
			.onEmpty(() -> log.error("missing ssh url for uploading"))
			.flatMap(
				url -> repository
					.onEmpty(
						() -> log.error("missing repository for uploading to {}", url)
					)
					.flatMap(
						repo -> option(repo::directory)
							.onEmpty(()-> log.error("missing directory from repository for {}", url))
					)
					.flatMap(
						dir -> Try.of(() -> Git.open(dir))
							.mapTry(git -> git.push().call())
							.onSuccess(result -> log.info("git push result: {}", result))
							.onFailure(
								error -> log.error(
									"error while git pushing repo: {}",
									error
								)
							)
							.toOption()
					)
			);
	}

	public static <T extends Repo>Supplier<Option<Iterable<PushResult>>> gitSshPush(T repo, String sshUrl){
		return new GitPushUploadStrategy<>(some(sshUrl), some(repo));
	}
}
