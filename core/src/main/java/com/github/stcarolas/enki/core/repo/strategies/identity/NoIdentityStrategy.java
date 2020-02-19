package com.github.stcarolas.enki.core.repo.strategies.identity;

import java.util.function.Supplier;

import static io.vavr.control.Option.none;
import io.vavr.control.Option;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Log4j2
public class NoIdentityStrategy implements Supplier<Option<String>> {

	@Override
	public Option<String> get() {
		log.info("Somebody wants to identify repo with no identity");
		return none();
	}

	public static Supplier<Option<String>> noIdentity() {
		return new NoIdentityStrategy();
	}
}
