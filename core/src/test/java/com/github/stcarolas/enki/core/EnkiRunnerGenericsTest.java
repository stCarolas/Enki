package com.github.stcarolas.enki.core;

import static com.github.stcarolas.enki.core.EnkiRunner.enki;

import java.io.File;

import org.junit.jupiter.api.Test;

import io.vavr.collection.List;
import io.vavr.collection.Seq;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

public class EnkiRunnerGenericsTest {

	@Test
	@SuppressWarnings({ "unchecked" })
	public void should_accept_two_different_providers_generics() {
		enki()
			.withProvider((RepoProvider<Repo>) (RepoProvider<?>) new TestProvider())
			.withProvider((RepoProvider<Repo>) (RepoProvider<?>) new AnotherTestProvider())
			.run();
	}

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

	@RequiredArgsConstructor
	public class AnotherTestRepo implements Repo {

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

	@AllArgsConstructor
	public static class TestProvider implements RepoProvider<TestRepo> {

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

	@AllArgsConstructor
	public static class AnotherTestProvider implements RepoProvider<AnotherTestRepo> {

		@Override
		public Seq<AnotherTestRepo> repositories() {
			return List.empty();
		}

		@Override
		public AnotherTestRepo download(AnotherTestRepo repo) {
			return null;
		}

		@Override
		public AnotherTestRepo upload(Repo repo) {
			return null;
		}
	}
}
