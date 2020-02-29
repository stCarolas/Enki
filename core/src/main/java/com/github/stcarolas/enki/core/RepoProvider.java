package com.github.stcarolas.enki.core;

import static io.vavr.API.*;
import io.vavr.collection.Seq;

// TODO add javadoc to RepoProvider
public interface RepoProvider<T extends Repo> {
	Seq<T> repositories();

	T download(T repo);

	T upload(Repo repo);

	default String describe(){
		return Option(this.getClass().getSimpleName())
			.getOrElse("Mock");
	}
}
