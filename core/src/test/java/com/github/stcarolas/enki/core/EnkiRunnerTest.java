package com.github.stcarolas.enki.core;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.github.stcarolas.enki.core.RepoProvider;
import com.github.stcarolas.enki.core.impl.test.TestProviderMother;

import org.junit.jupiter.api.Test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

public class EnkiRunnerTest {

	@Test
	@SuppressWarnings({"unchecked"})
	public void should_accept_two_different_providers_generics() {
		//expect:
		RepoProvider<AnotherTestRepo> anotherProvider = AnotherTestProvider.builder()
			.testRepos(Arrays.asList(new AnotherTestRepo("first")))
			.build();
		EnkiRunner.<Repo>builder()
			.provider(
				(RepoProvider<Repo>)(RepoProvider<?>)TestProviderMother.testProvider()
			)
			.provider((RepoProvider<Repo>)(RepoProvider<?>)anotherProvider)
			.build();
	}

	@RequiredArgsConstructor
	public class AnotherTestRepo implements Repo {

		private final String name;

		@Override public UUID getId() {
			return UUID.randomUUID();
		}

		@Override public String getName() {
			return name;
		}

		@Override public Map<CloneURLType, String> getCloneUrls() {
			return Collections.emptyMap();
		}

		@Override public Optional<File> getDirectory() {
			return Optional.empty();
		}

		@Override public RepoProvider getRepoProvider() {
			return null;
		}

		@Override public void commitAndPush(String commitMessage) {

		}
	}

	@Builder
	@AllArgsConstructor
	public static class AnotherTestProvider implements RepoProvider<AnotherTestRepo> {

		private List<AnotherTestRepo> testRepos;

		@Override public List<AnotherTestRepo> getRepos() {
			return testRepos;
		}
	}
    
}
