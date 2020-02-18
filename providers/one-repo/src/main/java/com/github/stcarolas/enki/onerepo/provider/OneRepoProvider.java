package com.github.stcarolas.enki.onerepo.provider;

import static io.vavr.collection.List.empty;

import com.github.stcarolas.enki.core.Repo;
import com.github.stcarolas.enki.core.RepoProvider;

import io.vavr.collection.Seq;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OneRepoProvider implements RepoProvider<Repo> {

	@Override
	public Seq<Repo> repositories() {
		return empty();
	}

	@Override
	public Option<Repo> download(Repo repo) {
		return Option.none();
	}

	@Override
	public Option<Repo> upload(Repo repo) {
		return Option.none();
	}
}
