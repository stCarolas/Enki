package com.github.stcarolas.enki.core.git;

import java.io.File;
import java.util.function.Function;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.PushResult;

import io.vavr.Function1;
import io.vavr.control.Try;
import lombok.Getter;
import lombok.Setter;

@Named("GitPush")
@Getter
@Setter
public class GitPush implements Function<File, Try<Iterable<PushResult>>> {

	@Inject @Named("GitRepo") private Function1<File, Try<Git>> gitOpen;
	@Inject @Named("GitRawPush") private Function1<Git, Try<Iterable<PushResult>>> gitPush;

	public Try<Iterable<PushResult>> apply(File directory){
		return gitOpen.apply(directory).flatMap(gitPush);
	}

}
