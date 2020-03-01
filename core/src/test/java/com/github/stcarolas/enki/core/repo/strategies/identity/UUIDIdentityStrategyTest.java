package com.github.stcarolas.enki.core.repo.strategies.identity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UUIDIdentityStrategyTest {

	@Test
	public void test_Return_not_uniq_string() {
		var strategy1 = UUIDIdentityStrategy.uuidIdentity();
		var strategy2 = UUIDIdentityStrategy.uuidIdentity();
		Assertions.assertNotNull(strategy1.get());
		Assertions.assertNotNull(strategy2.get());
		Assertions.assertNotEquals(strategy1.get(),strategy2.get());
	}
}
