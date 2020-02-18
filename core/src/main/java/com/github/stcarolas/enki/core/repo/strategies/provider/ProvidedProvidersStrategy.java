package com.github.stcarolas.enki.core.repo.strategies.provider;

import static io.vavr.collection.List.empty;
import static io.vavr.collection.List.ofAll;

import java.util.List;
import java.util.function.Supplier;

import com.github.stcarolas.enki.core.Repo;
import com.github.stcarolas.enki.core.RepoProvider;

import io.vavr.collection.Seq;
import io.vavr.control.Option;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProvidedProvidersStrategy implements Supplier<Seq<RepoProvider<? extends Repo>>> {
	private final Seq<RepoProvider<? extends Repo>> providers;

	@Override
	public Seq<RepoProvider<? extends Repo>> get() {
		return providers;
	}

	public static Supplier<Seq<RepoProvider<? extends Repo>>> providers(
		List<RepoProvider<? extends Repo>> providers
	) {
		return new ProvidedProvidersStrategy(
			Option.of(providers)
				.map(it -> ofAll(it))
				.getOrElse(empty())
		);
	}

}
