package com.github.stcarolas.enki.core;

import static com.github.stcarolas.enki.core.EnkiRunner.enki;

import java.io.File;

import org.junit.jupiter.api.Test;

import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

public class EnkiRunnerTest {

	@Test
	@SuppressWarnings({ "unchecked" })
	public void should_accept_two_different_providers_generics() {
		RepoProvider<AnotherTestRepo> anotherProvider = new AnotherTestProvider();
		enki()
			.withProvider((RepoProvider<Repo>) (RepoProvider<?>) anotherProvider)
			.withProvider((RepoProvider<Repo>) (RepoProvider<?>) anotherProvider)
			.run();
	}

	@RequiredArgsConstructor
	public class AnotherTestRepo implements Repo {

		@Override
		public Option<String> id() {
			return Option.none();
		}

		@Override
		public Option<String> name() {
			return Option.none();
		}

		@Override
		public Option<File> directory() {
			return Option.none();
		}

		@Override
		public Seq<RepoProvider<? extends Repo>> providers() {
			return List.empty();
		}

		@Override
		public Option<? extends Repo> commit(String commitMessage) {
			return Option.none();
		}
	}

	@AllArgsConstructor
	public static class AnotherTestProvider implements RepoProvider<AnotherTestRepo> {

		@Override
		public Seq<AnotherTestRepo> repositories() {
			return List.empty();
		}

		@Override
		public Option<AnotherTestRepo> download(AnotherTestRepo repo) {
			return Option.none();
		}

		@Override
		public Option<AnotherTestRepo> upload(Repo repo) {
			return Option.none();
		}
	}
}
