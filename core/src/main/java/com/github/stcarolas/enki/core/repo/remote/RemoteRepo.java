package com.github.stcarolas.enki.core.repo.remote;

import java.io.File;
import java.util.function.Function;

import com.github.stcarolas.enki.core.hosting.RepoHosting;

import org.eclipse.jgit.api.Git;

import io.vavr.Function2;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;

import static io.vavr.API.*;

@RequiredArgsConstructor
public class RemoteRepo {
	private final String id;
	private final String url;
	private final RepoHosting provider;
	private final Function2<String, File, Try<Git>> cloneCommand;
	private final Function<String, Try<File>> directoryProvider;

	public void toLocal(){
		directoryProvider.apply(id).flatMap(dir -> cloneCommand.apply(url, dir));
	}

}
