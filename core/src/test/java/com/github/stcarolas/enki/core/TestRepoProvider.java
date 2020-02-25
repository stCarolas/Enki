package com.github.stcarolas.enki.core;

import io.vavr.collection.List;
import io.vavr.collection.Seq;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TestRepoProvider implements RepoProvider<TestRepo> {

	@Override
	public Seq<TestRepo> repositories() {
		return List.empty();
	}

	@Override
	public TestRepo download(TestRepo repo) {
		return null;
	}

	@Override
	public TestRepo upload(Repo repo) {
		return null;
	}
}
