package com.github.stcarolas.enki.core;

import io.vavr.collection.Seq;
import io.vavr.control.Option;

public interface RepoProvider<T extends Repo> {
	Seq<T> repositories();
	Option<T> download(T repo);
	Option<T> upload(Repo repo);
}
