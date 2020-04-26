package com.github.stcarolas.enki.core.git;

import static io.vavr.API.For;
import static io.vavr.API.Option;

import java.io.File;
import java.util.function.Function;

import javax.inject.Named;
import javax.inject.Singleton;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;

import io.vavr.Function2;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Singleton @Named("HttpCloneCommand") 
public class GitClone implements Function2<String, File, Try<Git>> {

	private final Function2<String, File, CloneCommand> cloneCommand;

	public Try<Git> apply(String url, File repository){
		return For(Option(url), Option(repository))
			.yield(cloneCommand::apply).toTry()
			.mapTry(CloneCommand::call)
			.onFailure(error -> log.error("Exception while cloning: {}", error.getMessage()));
	}

	public GitClone (
		@Named("FilledCloneCommand") Function2<String, File, CloneCommand> cloneCommand
	){
		this.cloneCommand = cloneCommand;
	}

}
