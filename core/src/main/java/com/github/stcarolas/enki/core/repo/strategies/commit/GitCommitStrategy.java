package com.github.stcarolas.enki.core.repo.strategies.commit;

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
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor(access=AccessLevel.PROTECTED)
public class GitCommitStrategy implements Function<String, Try<RevCommit>> {

	private final Option<Supplier<File>> directory;

	protected Function1<File, Try<Git>> git = dir ->
		Try(() -> Git.open(dir))
			.onFailure( 
				error -> log.error("error while getting git status: {}",error)
			);

	protected Function1<Git, Boolean> isClean = git ->
		Try(git.status()::call)
			.onFailure( 
				error -> log.error("error while getting git status: {}", error)
			)
			.map(Status::isClean)
			.getOrElse(true);

	protected Function1<Git, Try<Git>> stage = git ->
		Try(git.add().addFilepattern(".")::call)
			.onFailure( 
				error -> log.error("error while getting git staging: {}",error)
			)
			.map( cache -> git );

	protected Function2<String, Git, Try<RevCommit>> commit = (commitMessage, git) ->
		Option.of(commitMessage)
			.filterNot(String::isBlank)
			.onEmpty(() -> log.error("missing commit message"))
			.toTry()
			.mapTry( message -> git.commit().setMessage(message).call() )
			.onFailure( 
				error -> log.error("error while getting git staging: {}",error)
			);

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
		return new GitCommitStrategy(Option(directory));
	}

}
