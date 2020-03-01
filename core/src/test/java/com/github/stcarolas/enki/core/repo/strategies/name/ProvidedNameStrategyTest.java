package com.github.stcarolas.enki.core.repo.strategies.name;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ProvidedNameStrategyTest {
	
	@Test
	public void test_Return_same_name() {
		var name = "some name";
		Assertions.assertEquals(name, ProvidedNameStrategy.providedName(name).get());
	}
	
	@Test
	public void test_Null_for_null_name() {
		Assertions.assertNull(ProvidedNameStrategy.providedName(null).get());
	}
}
