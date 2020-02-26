package com.github.stcarolas.enki.core.provider.strategies.upload;

import static com.github.stcarolas.enki.core.util.Lifting.call;
import static io.vavr.control.Option.of;
import static io.vavr.collection.List.empty;

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
public class GitPushUploadStrategy<T extends Repo> implements Supplier<Iterable<PushResult>> {
	private final Option<String> sshUrl;
	private final Option<T> repository;

	@Override
	public Iterable<PushResult> get() {
		return sshUrl
			.onEmpty(() -> log.error("missing ssh url to push, aborting"))
			.flatMap(
				url -> repository
					.onEmpty( () -> log.error("missing repository for uploading to {}, aborting push", url) )
					.peek( repo -> log.info("pushing {} to {}", repository, url) )
					.flatMap(
						repo -> call(repo::directory)
							.onEmpty(()-> log.error("missing directory for repository {}, aborting push", repository))
					)
					.flatMap(
						directory -> Try.of(() -> Git.open(directory))
							.onFailure( 
								error -> log.error("error while acessing repository {}, aborting push: {}", repository, error)
							)

							.mapTry(git -> git.push().call())
							.onFailure(
								error -> log.error("error while pushing repo: {}", error)
							)
							.onSuccess( result -> log.info("git push result: {}", result))
							.toOption()
					)
			).getOrElse(empty());
	}

	public static <T extends Repo>Supplier<Iterable<PushResult>> gitSshPush(T repo, String sshUrl){
		return new GitPushUploadStrategy<>(of(sshUrl), of(repo));
	}
}
