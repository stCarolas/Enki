package com.github.stcarolas.enki.core.provider.strategies.download;

import static io.vavr.control.Option.some;

import java.io.File;
import java.util.function.Supplier;

import com.github.stcarolas.enki.core.Repo;
import com.github.stcarolas.enki.core.transport.DefaultTransportConfigCallback;

import org.eclipse.jgit.api.Git;

import static io.vavr.Function0.lift;
import io.vavr.Function0;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GitCloneDownloadStrategy<T extends Repo> implements Supplier<Option<File>> {

	private final Option<String> sshUrl;
	private final Option<T> repository;

	@Override
	public Option<File> get(){
		return sshUrl
			.onEmpty(() -> log.error("missing ssh url to use for cloning"))
			.flatMap(
				url -> repository
					.onEmpty(() -> log.error("missing repository to clone for url {}", url))
					.flatMap(repo -> lift(repo::directory).apply()
						.onEmpty(() -> log.error("missing any directory to clone into"))
					)
					.peek(
						dir -> Try.of(
							() -> Git.cloneRepository()
								.setURI(url)
								.setDirectory(dir)
								.setTransportConfigCallback(new DefaultTransportConfigCallback())
								.call()
						)
						.onFailure(error -> log.error("error while cloning {}: {}", url, error))
						.onSuccess(git -> log.info("repository with url {} was cloned", url))
					)
			);
	}

	public static <T extends Repo>Supplier<Option<File>> gitSshClone(T repo, String sshUrl){
		return new GitCloneDownloadStrategy<>(some(sshUrl), some(repo));
	}

}