package com.github.stcarolas.enki.core.git.factories;

import static io.vavr.API.Option;
import static io.vavr.API.Try;

import java.io.File;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.PushResult;

import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Prototype;
import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Factory
public class GitCommonModule {

	@Prototype CloneCommand emptyCloneCommand(){
		return Git.cloneRepository();
	}

	@Prototype Function1<File, Try<Git>> gitOpenFn(){
		return directory ->
			Try(() -> Git.open(directory))
				.onFailure(
					error -> log.error( "error while acessing repository aborting push: {}", error)
				);
	} 

	@Prototype Function1<Git, Try<Boolean>> isCleanFn(){
		return git ->
			Try(() -> git.status().call())
				.onFailure( 
					error -> log.error("error while getting git status: {}", error)
				)
				.map(Status::isClean);
	}

	@Prototype Function2<Git, String, Try<Git>> gitStageFn(){
		return (git,pattern) ->
			Try(() -> git.add().addFilepattern(pattern).call())
				.onFailure( 
					error -> log.error("error while getting git staging: {}",error)
				)
				.map( result -> git );
	}

	@Prototype Function2<String, Git, Try<RevCommit>> gitCommitFn(){
		return (commitMessage, git) ->
			Option(commitMessage)
				.filterNot(String::isBlank)
				.onEmpty(() -> log.error("missing commit message"))
				.toTry()
				.mapTry( message -> git.commit().setMessage(message).call() )
				.onFailure( 
					error -> log.error("error while getting git staging: {}",error)
				);
	}

	@Prototype Function1<Git, Try<Iterable<PushResult>>> gitPushFn(){
		return git ->
			Try(() -> git.push().call())
				.onFailure(error -> log.error("error while pushing repo: {}", error))
				.onSuccess(result -> log.info("git push result: {}", result));
	}

}
