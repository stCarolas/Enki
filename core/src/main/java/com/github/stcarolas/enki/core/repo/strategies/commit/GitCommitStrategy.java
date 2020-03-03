package com.github.stcarolas.enki.core.repo.strategies.commit;

import static io.vavr.API.Function;
import static io.vavr.API.Option;
import static io.vavr.API.Try;

import java.io.File;
import java.util.function.Function;
import java.util.function.Supplier;

import com.github.stcarolas.enki.core.util.Lifting;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.revwalk.RevCommit;

import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
@AllArgsConstructor(access=AccessLevel.PRIVATE)
@Builder
@Getter
public class GitCommitStrategy implements Function<String, Try<RevCommit>> {

	private static final Function1<File, Try<Git>> gitFn = 
		Function( dir ->
			Try(() -> Git.open(dir))
				.onFailure( 
					error -> log.error("error while getting git status: {}",error)
				)
		);

	private static final Function1<Git, Boolean> isCleanFn = 
		Function( git ->
			Try(() -> git.status().call())
				.onFailure( 
					error -> log.error("error while getting git status: {}", error)
				)
				.map(Status::isClean)
				.getOrElse(true)
		);

	private static final Function1<Git, Try<Git>> stageFn = 
		Function( git ->
			Try(git.add().addFilepattern(".")::call)
				.onFailure( 
					error -> log.error("error while getting git staging: {}",error)
				)
				.map( cache -> git )
		);

	private static final Function2<String, Git, Try<RevCommit>> commitFn = 
		Function( (commitMessage, git) ->
			Option.of(commitMessage)
				.filterNot(String::isBlank)
				.onEmpty(() -> log.error("missing commit message"))
				.toTry()
				.mapTry( message -> git.commit().setMessage(message).call() )
				.onFailure( 
					error -> log.error("error while getting git staging: {}",error)
				)
		);

	private Option<Supplier<File>> directory;
	private Function1<File, Try<Git>> git;
	private Function1<Git, Boolean> isClean;
	private Function1<Git, Try<Git>> stage;
	private Function2<String, Git, Try<RevCommit>> commit;

	@Override
	public Try<RevCommit> apply(String message) {
		return directory
			.flatMap( Lifting::call )
			.onEmpty( () -> log.error("try to download repo before commiting into that") )
			.peek( dir -> log.info("using directory {}", dir) )
			.toTry()
			.flatMap(
				dir -> git.apply(dir)
					.filterNot( $ -> isClean.apply($) )
					.flatMap(stage)
					.onFailure( error -> log.error("some error: ", error) )
					.flatMap(commit.apply(message))
			)
			.peek( revision -> log.info("Successful commit: {}", revision.getId()) );
	}

	public static Function<String, Try<RevCommit>> GitCommit(Supplier<File> directory){
		return GitCommitStrategy.builder()
			.directory(Option(directory))
			.git(gitFn)
			.isClean(isCleanFn)
			.stage(stageFn)
			.commit(commitFn)
			.build();
	}

}
