package com.github.stcarolas.enki.core.git;

import static io.vavr.API.Option;

import java.io.File;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;

import io.vavr.Function2;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import static io.vavr.API.*;

@Log4j2
@Singleton @Named("HttpCloneCommand") 
public class GitCloneHttp implements Function2<String, File, Try<Git>> {

	private final Function2<String, File, CloneCommand> cloneCommand;

	public Try<Git> apply(String url, File repository){
		return For(Option(url), Option(repository))
			.yield(cloneCommand::apply).toTry()
			.mapTry(CloneCommand::call)
			.onFailure(error -> log.error("Exception while cloning: {}", error.getMessage()));
	}

	public GitCloneHttp(
		@Named("FilledCloneCommand") Function2<String, File, CloneCommand> cloneCommand
	){
		this.cloneCommand = cloneCommand;
	}

}
