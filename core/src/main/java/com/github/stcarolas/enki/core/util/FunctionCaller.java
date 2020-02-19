package com.github.stcarolas.enki.core.util;

import static io.vavr.collection.List.empty;
import static io.vavr.control.Option.none;

import io.vavr.CheckedFunction0;
import io.vavr.collection.Seq;
import io.vavr.control.Option;
import io.vavr.control.Try;

public class FunctionCaller {

    public static final <T>Seq<T> seq(CheckedFunction0<Seq<T>> function) {
		return Try.of(function)
			.filter( seq -> seq != null )
			.getOrElse(empty());
	}

    public static final <T>Option<T> option(CheckedFunction0<Option<T>> function) {
		return Try.of(function)
			.filter( option -> option != null )
			.getOrElse(none())
			;
	}

}
