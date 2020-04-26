package com.github.stcarolas.enki.core.repo.remote;

import java.io.File;
import java.util.function.Function;

import javax.inject.Named;

import com.github.stcarolas.enki.core.hosting.RepoHosting;
import com.github.stcarolas.enrichedbeans.annotations.Enriched;

import org.eclipse.jgit.api.Git;

import io.vavr.Function2;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;

import static io.vavr.API.*;

@RequiredArgsConstructor
public class RemoteRepo {

	@Enriched @Named("GeneratedId")
	private final String id;

	private final String url;

	private final RepoHosting provider;

	@Enriched @Named("HttpCloneCommand") 
	private final Function2<String, File, Try<Git>> cloneCommand;

	@Enriched @Named("EnsuredFileProvider") 
	private final Function<String, Try<File>> directoryProvider;

	public void toLocal(){
		directoryProvider.apply(id).flatMap(dir -> cloneCommand.apply(url, dir));
	}

}
