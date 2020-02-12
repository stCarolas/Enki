package com.github.stcarolas.enki.core.functions;

import java.util.List;
import com.github.stcarolas.enki.core.Function;
import com.github.stcarolas.enki.core.Repo;
import com.github.stcarolas.enki.core.RepoProvider;
import lombok.RequiredArgsConstructor;

public class TakeFunction<T extends Repo> implements Function<T> {
	private final int count;

	private TakeFunction(int count) {
		this.count = count;
	}

	public static <T extends Repo>TakeFunction<T> take(int count) {
		return new TakeFunction<>(count);
	}

	@Override
	public RepoProvider<T> from(RepoProvider<T> source) {
		return new InternalRepoProvider<>(source, count);
	}

	@RequiredArgsConstructor
	public static class InternalRepoProvider<T extends Repo> implements RepoProvider<T> {
		private final RepoProvider<T> source;
		private final int count;

		@Override
		public List<T> getRepos() {
			return source.getRepos().subList(0, count);
		}
	}
}
