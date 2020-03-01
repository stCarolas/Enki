package com.github.stcarolas.enki.core.repo.strategies.commit;

import java.io.File;
import java.util.function.Supplier;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.revwalk.RevCommit;

import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.control.Try;
import static io.vavr.API.*;
import static org.mockito.Mockito.mock;

public class GitCommitStrategyTestHelpers {

	public static Function1<Git, Try<Git>> successfullStage(){
		return Function( git -> Success(git) );
	}

	public static Function2<String, Git, Try<RevCommit>> successfullCommit(){
		return successfullCommit(mock(RevCommit.class));
	}

	public static Function2<String, Git, Try<RevCommit>> successfullCommit(RevCommit commit){
		return Function((message, git) -> Success(commit));
	}

	public static Supplier<File> tmpDir(){
		return () -> new File("/tmp/test");
	}

}
