package com.github.stcarolas.enki.core.hosting;

import static io.vavr.API.*;

import com.github.stcarolas.enki.core.repo.remote.RemoteRepo;

import io.vavr.collection.Seq;
import io.vavr.control.Try;

public interface RepoHosting {
	Try<Seq<RemoteRepo>> repositories();
}
