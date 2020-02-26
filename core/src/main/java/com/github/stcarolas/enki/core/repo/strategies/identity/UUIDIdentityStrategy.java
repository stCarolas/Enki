package com.github.stcarolas.enki.core.repo.strategies.identity;

import java.util.UUID;
import java.util.function.Supplier;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UUIDIdentityStrategy implements Supplier<String> {

	@Override
	public String get() {
		return UUID.randomUUID().toString();
	}

	public static Supplier<String> uuidIdentity(){
		return new UUIDIdentityStrategy();
	}

}
