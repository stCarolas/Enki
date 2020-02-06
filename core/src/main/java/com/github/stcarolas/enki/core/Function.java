package com.github.stcarolas.enki.core;

public interface Function<T extends Repo> {
    RepoProvider<T> from(RepoProvider<T> source);
}
