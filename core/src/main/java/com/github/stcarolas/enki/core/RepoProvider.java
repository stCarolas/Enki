package com.github.stcarolas.enki.core;

import java.util.List;

public interface RepoProvider<T extends Repo> {
    List<T> getRepos();
}
