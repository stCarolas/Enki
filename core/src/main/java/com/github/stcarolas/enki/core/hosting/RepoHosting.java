package com.github.stcarolas.enki.core.hosting;

import static io.vavr.API.Option;

import java.util.function.Function;

import com.github.stcarolas.enki.core.repo.remote.RemoteRepo;
import com.github.stcarolas.enki.core.repo.remote.RemoteRepoFactory;

import io.vavr.collection.Seq;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Log4j2
public class RepoHosting {

	private final RemoteRepoFactory factory;
	private final Function<RemoteRepoFactory, Try<Seq<RemoteRepo>>> list;

	public Try<Seq<RemoteRepo>> repositories(){
		return Option(factory).toTry().flatMap(list);
	}

}
