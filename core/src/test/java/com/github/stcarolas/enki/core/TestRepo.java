package com.github.stcarolas.enki.core;

import java.io.File;

import io.vavr.collection.List;
import io.vavr.collection.Seq;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TestRepo implements Repo {

	@Override
	public String id() {
		return null;
	}

	@Override
	public String name() {
		return null;
	}

	@Override
	public File directory() {
		return null;
	}

	@Override
	public Seq<RepoProvider<? extends Repo>> providers() {
		return List.empty();
	}

	@Override
	public Repo commit(String commitMessage) {
		return null;
	}
}
