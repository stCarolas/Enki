package com.github.stcarolas.enki.core.repo.strategies.identity;

import java.util.UUID;
import java.util.function.Supplier;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UUIDIdentityStrategy implements Supplier<String> {
	private String id = UUID.randomUUID().toString();

	@Override
	public String get() {
		return id;
	}

	public static Supplier<String> uuidIdentity(){
		return new UUIDIdentityStrategy();
	}

}
