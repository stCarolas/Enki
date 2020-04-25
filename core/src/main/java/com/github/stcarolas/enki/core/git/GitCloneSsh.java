package com.github.stcarolas.enki.core.git;

import java.io.File;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;

import io.vavr.Function2;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import javax.inject.Inject;
import javax.inject.Singleton;

import static io.vavr.API.*;

@Log4j2
@RequiredArgsConstructor
@Singleton
public class GitCloneSsh implements Function2<String, File, Try<Git>> {

	private final Function2<String, File, CloneCommand> cloneCommand;

	public Try<Git> apply(String sshUrl, File directory){
		return For(Option(sshUrl), Option(directory))
			.yield(cloneCommand::apply)
			.toTry().orElse(Failure(new RuntimeException("Missing url or repository")))
			.flatMap(command -> Try(command::call)
				.onFailure(
					error -> log.error("Exception while cloning: {}", error.getMessage())
				)
			);
	}

}
