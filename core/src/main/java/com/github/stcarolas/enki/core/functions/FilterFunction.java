package com.github.stcarolas.enki.core.functions;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import com.github.stcarolas.enki.core.Repo;
import com.github.stcarolas.enki.core.RepoProvider;
import lombok.RequiredArgsConstructor;

import static java.util.stream.Collectors.*;

public class FilterFunction<T extends Repo> implements com.github.stcarolas.enki.core.Function<T> {
	private final Predicate<? super Repo> filter;

	private FilterFunction(Predicate<? super Repo> filter) {
		this.filter = filter;
	}

	public static <T extends Repo>FilterFunction<T> filter(Predicate<? super Repo> filter) {
		return new FilterFunction<>(filter);
	}

	@Override
	public RepoProvider<T> from(RepoProvider<T> source) {
		return new InternalRepoProvider<>(source, filter);
	}

	@RequiredArgsConstructor
	public static class InternalRepoProvider<T extends Repo> implements RepoProvider<T> {
		private final RepoProvider<T> source;
		private final Predicate<? super Repo> filter;

		@Override
		public List<T> getRepos() {
			return source.getRepos().stream().filter(filter).collect(toList());
		}
	}
}
