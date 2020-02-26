package com.github.stcarolas.enki.core.repo.strategies.identity;

import java.util.function.Supplier;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Log4j2
public class NoIdentityStrategy implements Supplier<String> {

	@Override
	public String get() {
		log.info("Somebody wants to identify repo with no identity");
		return null;
	}

	public static Supplier<String> noIdentity() {
		return new NoIdentityStrategy();
	}
}
