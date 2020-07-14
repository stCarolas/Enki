package com.github.stcarolas.enki.core.repo.local;

import java.io.File;
import java.util.function.Function;

import javax.inject.Inject;
import javax.inject.Named;

import com.github.stcarolas.enrichedbeans.annotations.Enrich;

import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.PushResult;

import io.vavr.Function2;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LocalRepo {
	private final String id;
	private final File directory;
	@Enrich @Named("GitCommit")
	private final Function2<File, String, Try<RevCommit>> commit;
	@Enrich @Named("GitPush")
	private final Function<File, Try<Iterable<PushResult>>> push;

	public Try<LocalRepo> commit(String message) {
		return commit.apply(directory, message).map(result -> this);
	}

	public Try<LocalRepo> push() {
		return push.apply(directory).map(result -> this);
	}

	public Try<LocalRepo> apply(Function<File, Try<Void>> fileHandler) {
		return Try.run(() -> fileHandler.apply(directory)).map(result -> this);
	}
}
