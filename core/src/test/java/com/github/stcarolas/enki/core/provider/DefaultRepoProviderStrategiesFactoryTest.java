package com.github.stcarolas.enki.core.provider;

import com.github.stcarolas.enki.core.provider.strategies.download.GitCloneDownloadStrategy;
import com.github.stcarolas.enki.core.provider.strategies.upload.GitPushUploadStrategy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DefaultRepoProviderStrategiesFactoryTest {

	@Test
	public void test_ssh_push_strategy() {
		Assertions.assertTrue(
			DefaultRepoProviderStrategiesFactory.gitSshPush(null) instanceof GitPushUploadStrategy
		);
	}

	@Test
	public void test_ssh_pull_strategy() {
		Assertions.assertTrue(
			DefaultRepoProviderStrategiesFactory.gitSshClone(null, null) instanceof GitCloneDownloadStrategy
		);
	}

}
