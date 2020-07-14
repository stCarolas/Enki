package com.github.stcarolas.enki.core.git;


import java.io.File;
import java.util.function.Function;

import javax.inject.Named;
import javax.inject.Singleton;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.revwalk.RevCommit;

import io.vavr.Function2;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Named("GitCommit")
public class GitCommit implements Function2<File, String, Try<RevCommit>> {

	private final Function<File, Try<Git>> git;
	private final Function2<String, Git, Try<Git>> stage;
	private final Function2<String, Git, Try<RevCommit>> commit;

	public Try<RevCommit> apply(File directory, String message){
		return git.apply(directory)
			.flatMap(stage.apply("."))
			.flatMap(commit.apply(message));
	}

}
