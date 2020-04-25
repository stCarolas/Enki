package com.github.stcarolas.enki.core.repo.remote;

import java.io.File;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.inject.Named;
import javax.inject.Singleton;

import com.github.stcarolas.enki.core.hosting.RepoHosting;

import org.eclipse.jgit.api.Git;

import io.vavr.Function2;
import io.vavr.control.Try;

@Singleton
public class RemoteRepoFactory {
	 
	private final Function2<String, File, Try<Git>> cloneCommand;
	private final Function<String, Try<File>> directoryProvider;
	private final Supplier<String> identityProvider;

	public RemoteRepo remote(RepoHosting host, String url){
		return new RemoteRepo(
			identityProvider.get(), url, host, cloneCommand, directoryProvider
		);
	}

	public RemoteRepoFactory(
		@Named("HttpCloneCommand") Function2<String, File, Try<Git>> cloneCommand,
		@Named("EnsuredFileProvider") Function<String, Try<File>> directoryProvider, 
		Supplier<String> identityProvider
	){
		this.cloneCommand = cloneCommand;
		this.directoryProvider = directoryProvider;
		this.identityProvider = identityProvider;
	}
}
