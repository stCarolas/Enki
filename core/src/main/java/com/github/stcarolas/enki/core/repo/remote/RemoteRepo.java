package com.github.stcarolas.enki.core.repo.remote;

import java.io.File;
import java.util.function.Function;

import javax.inject.Named;

import com.github.stcarolas.enki.core.hosting.RepoHosting;
import com.github.stcarolas.enki.core.repo.local.LocalRepo;
import com.github.stcarolas.enrichedbeans.annotations.Enrich;

import org.eclipse.jgit.api.Git;

import io.vavr.Function2;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;

import static io.vavr.API.*;

@RequiredArgsConstructor
public class RemoteRepo {

	private final String url;

	private final RepoHosting provider;

	@Enrich @Named("GeneratedId")
	private final String id;

	@Enrich @Named("HttpCloneCommand")
	private final Function2<String, File, Try<Git>> cloneCommand;

	@Enrich @Named("EnsuredFileProvider")
	private final Function<String, Try<File>> directoryProvider;

	@Enrich @Named("LocalRepoFactory")
	private final Function2<String, File, LocalRepo> localRepoFactory;

	public void toLocal(){
		directoryProvider.apply(id).flatMap(dir -> cloneCommand.apply(url, dir));
	}

} 
