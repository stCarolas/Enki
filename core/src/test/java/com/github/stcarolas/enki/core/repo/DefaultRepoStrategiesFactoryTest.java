package com.github.stcarolas.enki.core.repo;

import java.util.Collections;

import com.github.stcarolas.enki.core.TestRepo;
import com.github.stcarolas.enki.core.repo.strategies.commit.GitCommitStrategy;
import com.github.stcarolas.enki.core.repo.strategies.directory.TemporaryFileDirectoryStrategy;
import com.github.stcarolas.enki.core.repo.strategies.identity.UUIDIdentityStrategy;
import com.github.stcarolas.enki.core.repo.strategies.name.ProvidedNameStrategy;
import com.github.stcarolas.enki.core.repo.strategies.provider.ProvidedProvidersStrategy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DefaultRepoStrategiesFactoryTest {

	@Test
	public void test_defaut_git_strategy() {
		var strategy = DefaultRepoStrategiesFactory.commit(new TestRepo());
		Assertions.assertTrue(strategy instanceof GitCommitStrategy);
	}

	@Test
	public void test_defaut_directory_strategy() {
		var strategy = DefaultRepoStrategiesFactory.directory(new TestRepo());
		Assertions.assertTrue(strategy instanceof TemporaryFileDirectoryStrategy);
	}

	@Test
	public void test_defaut_identity_strategy() {
		var strategy = DefaultRepoStrategiesFactory.identity();
		Assertions.assertTrue(strategy instanceof UUIDIdentityStrategy);
	}

	@Test
	public void test_defaut_name_strategy() {
		var strategy = DefaultRepoStrategiesFactory.name("some name");
		Assertions.assertTrue(strategy instanceof ProvidedNameStrategy);
	}

	@Test
	public void test_defaut_providers_strategy() {
		var strategy = DefaultRepoStrategiesFactory.providers(Collections.emptyList());
		Assertions.assertTrue(strategy instanceof ProvidedProvidersStrategy);
	}
	
}

