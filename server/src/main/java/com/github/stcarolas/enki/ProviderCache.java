package com.github.stcarolas.enki;

import java.util.ArrayList;
import java.util.List;

import com.github.stcarolas.enki.core.EnkiRunner;
import com.github.stcarolas.enki.core.Repo;
import com.github.stcarolas.enki.core.RepoHandler;
import com.github.stcarolas.enki.core.RepoProvider;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProviderCache<T extends Repo> implements RepoHandler<T>, RepoProvider<T> {
	List<T> repos = new ArrayList<>();

	public static <T extends Repo> ProviderCache<T> cache(
		List<RepoProvider<T>> providers
	) {
		val cache = new ProviderCache<T>();
		EnkiRunner.<T>builder().providers(providers).handler(cache).build().handle();
		return cache;
	}

	@Override
	public void handle(T repo) {
		repos.add(repo);
	}

	@Override
	public List<T> getRepos() {
		return repos;
	}
}
