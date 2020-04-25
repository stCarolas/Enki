package com.github.stcarolas.enki.core.identity;

import java.util.UUID;
import java.util.function.Supplier;

import lombok.RequiredArgsConstructor;
import javax.inject.Inject;
import javax.inject.Singleton;

@RequiredArgsConstructor
@Singleton
public class IdentifyByUUID implements Supplier<String> {

	@Override
	public String get() {
		return UUID.randomUUID().toString();
	}

}
