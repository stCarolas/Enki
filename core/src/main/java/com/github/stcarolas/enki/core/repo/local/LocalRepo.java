package com.github.stcarolas.enki.core.repo.local;

import java.io.File;
import java.util.function.Function;

import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.PushResult;

import io.vavr.Function2;
import io.vavr.control.Try;

import lombok.RequiredArgsConstructor;
import javax.inject.Inject;
import static io.vavr.API.*;

@RequiredArgsConstructor(onConstructor=@__(@Inject))
public class LocalRepo {
	private final String id;
	private final File directory;
	private final Function2<File,String, Try<RevCommit>> commit;
	private final Function<File, Try<Iterable<PushResult>>> push;

	public Try<LocalRepo> commit(String message){
		return commit.apply(directory,message).map(result -> this);
	}

	public Try<LocalRepo> push(){
		return push.apply(directory).map(result -> this);
	}

	public Try<LocalRepo> apply(LocalRepoFileHandler fileHandler){
		return Try.run(() -> fileHandler.handle(directory)).map(result -> this);
	}
}
