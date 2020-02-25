package com.github.stcarolas.enki.core;

import io.vavr.collection.Seq;
import io.vavr.control.Option;

// TODO add javadoc to RepoProvider
public interface RepoProvider<T extends Repo> {
	Seq<T> repositories();
	T download(T repo);
	T upload(Repo repo);
}
