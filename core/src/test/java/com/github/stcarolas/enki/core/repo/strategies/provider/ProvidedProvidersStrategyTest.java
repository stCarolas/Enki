package com.github.stcarolas.enki.core.repo.strategies.provider;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static io.vavr.API.*;

import java.util.Arrays;
import java.util.Collections;

import com.github.stcarolas.enki.core.TestRepoProvider;

public class ProvidedProvidersStrategyTest {
	
	@Test
	public void test_Return_empty_list_if_no_provider_provided() {
		Assertions.assertTrue(
			ProvidedProvidersStrategy.providers(Collections.emptyList()).get().isEmpty()
		);
	}
	
	@Test
	public void test_Return_empty_list_if_null_provided() {
		Assertions.assertTrue(
			ProvidedProvidersStrategy.providers(null).get().isEmpty()
		);
	}
	
	@Test
	public void test_Return_same_list_of_providers() {
		var provider1 = new TestRepoProvider();
		var provider2 = new TestRepoProvider();
		var providers = ProvidedProvidersStrategy.providers(
			Arrays.asList(provider1, provider2)
		).get();
		Assertions.assertFalse(providers.isEmpty());
		Assertions.assertEquals(provider1, providers.apply(0));
		Assertions.assertEquals(provider2, providers.apply(1));
	}
}
