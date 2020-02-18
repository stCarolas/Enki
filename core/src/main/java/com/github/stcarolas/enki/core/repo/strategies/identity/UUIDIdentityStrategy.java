package com.github.stcarolas.enki.core.repo.strategies.identity;

import java.util.UUID;
import java.util.function.Supplier;

import io.vavr.control.Option;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UUIDIdentityStrategy implements Supplier<Option<String>> {

	@Override
	public Option<String> get() {
		return Option.of(UUID.randomUUID().toString());
	}

	public static Supplier<Option<String>> uuidIdentity(){
		return new UUIDIdentityStrategy();
	}

}
