package com.github.stcarolas.enki.core.git;


import java.io.File;
import java.util.function.Function;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.revwalk.RevCommit;

import io.vavr.Function2;
import io.vavr.control.Try;
import lombok.Getter;
import lombok.Setter;

@Named("GitCommit")
@Getter
@Setter
public class GitCommit implements Function2<File, String, Try<RevCommit>> {

	@Inject @Named("GitRepo") private Function<File, Try<Git>> git;
	@Inject @Named("GitStage") private Function2<Git, String, Try<Git>> stage;
	@Inject @Named("GitCommit") private Function2<String, Git, Try<RevCommit>> commit;

	public Try<RevCommit> apply(File directory, String message){
		return git.apply(directory)
			.flatMap(git -> stage.apply(git, "."))
			.flatMap(commit.apply(message));
	}

}
