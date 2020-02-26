package com.github.stcarolas.enki.core.repo.strategies.name;

import java.util.function.Supplier;

import io.vavr.control.Option;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProvidedNameStrategy implements Supplier<String> {
	@Getter
	private final Option<String> name;

	@Override
	public String get() {
		return name.getOrNull();
	}

	public static Supplier<String> providedName(String name) {
		return new ProvidedNameStrategy(Option.of(name));
	}
}
