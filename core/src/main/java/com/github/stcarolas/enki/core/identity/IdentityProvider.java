package com.github.stcarolas.enki.core.identity;

import java.util.UUID;

import javax.inject.Named;

import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Prototype;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Factory
public class IdentityProvider {

	@Prototype @Named("GeneratedId")
	public String newId() {
		return UUID.randomUUID().toString();
	}

}
