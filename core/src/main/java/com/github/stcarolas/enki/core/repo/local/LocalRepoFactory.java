package com.github.stcarolas.enki.core.repo.local;

import java.io.File;
import java.util.function.Function;

import javax.inject.Inject;

import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.PushResult;

import io.vavr.Function2;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor=@__(@Inject))
public class LocalRepoFactory {

	private final Function2<File,String, Try<RevCommit>> commit;
	private final Function<File, Try<Iterable<PushResult>>> push;

	public LocalRepo local(String id, File directory){
		return new LocalRepo(id, directory, commit, push);
	}

}
