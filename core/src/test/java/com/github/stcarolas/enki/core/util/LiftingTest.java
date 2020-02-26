package com.github.stcarolas.enki.core.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

public class LiftingTest {

	@Test
	public void test_return_value_as_option() {
		var value = "value";
		var result = Lifting.call(() -> value );
		assertTrue(!result.isEmpty());
		assertEquals(value, result.get());
	}

	@Test
	public void test_return_null_as_none() {
		String value = null;
		var result = Lifting.call(() -> value );
		assertTrue(result.isEmpty());
	}

	@Test
	public void test_return_exception_as_none() {
		var result = Lifting.call(
			(Supplier<String>) () -> {throw new RuntimeException("some error");} 
		);
		assertTrue(result.isEmpty());
	}
	
}
