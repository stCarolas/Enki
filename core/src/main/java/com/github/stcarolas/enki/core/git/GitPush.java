package com.github.stcarolas.enki.core.git;

import java.io.File;
import java.util.function.Function;

import javax.inject.Named;
import javax.inject.Singleton;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.PushResult;

import io.vavr.Function1;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Named("GitPush")
public class GitPush implements Function<File, Try<Iterable<PushResult>>> {

	private final Function1<File, Try<Git>> gitOpen;
	private final Function1<Git, Try<Iterable<PushResult>>> gitPush;

	public Try<Iterable<PushResult>> apply(File directory){
		return gitOpen.apply(directory).flatMap(gitPush);
	}

}
