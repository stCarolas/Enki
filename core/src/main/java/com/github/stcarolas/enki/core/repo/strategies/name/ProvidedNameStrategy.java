package com.github.stcarolas.enki.core.repo.strategies.name;

import java.util.function.Supplier;

import io.vavr.control.Option;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProvidedNameStrategy implements Supplier<Option<String>> {
	@Getter
	private final Option<String> name;

	@Override
	public Option<String> get() {
		return name;
	}

	public static Supplier<Option<String>> providedName(String name) {
		return new ProvidedNameStrategy(Option.of(name));
	}
}
