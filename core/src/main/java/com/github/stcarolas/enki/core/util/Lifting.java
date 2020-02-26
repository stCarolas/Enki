package com.github.stcarolas.enki.core.util;

import java.util.function.Supplier;

import io.vavr.Function0;
import io.vavr.control.Option;
import static io.vavr.API.*;

public class Lifting {
	public static <T>Option<T> call(Supplier<T> partialFunction){
		return Function0.lift(partialFunction)
			.apply()
			.transform( result -> result.isEmpty() ? None() : Option.of(result.get()) );
	}
}
