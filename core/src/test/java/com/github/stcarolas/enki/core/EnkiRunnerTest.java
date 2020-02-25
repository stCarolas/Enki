package com.github.stcarolas.enki.core;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class EnkiRunnerTest {

	//@Test
	public void testEmptyProviders() {
		EnkiRunner.enki().run();
	}

	@Test
	public void testEmptyHandlers() {
		EnkiRunner.enki()
			.withProvider(Mockito.mock(RepoProvider.class))
			.run();
	}
}
