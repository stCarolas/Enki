package com.github.stcarolas.enki.core;

public interface RepoHandler<T extends Repo> {
	void handle(T repo);
}
