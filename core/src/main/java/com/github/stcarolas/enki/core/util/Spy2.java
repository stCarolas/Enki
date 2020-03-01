package com.github.stcarolas.enki.core.util;

import static io.vavr.API.*;
import io.vavr.Function2;

public class Spy2<T1,T2,T3> {
	private Function2<T1,T2,T3> fn;
	private Boolean isCalled = false;

	public static <T1,T2,T3>Spy2<T1,T2,T3> spy(Function2<T1,T2,T3> fn){
		var spy = new Spy2<T1,T2,T3>();
		spy.fn = fn;
		return spy;
	}

	public Function2<T1,T2,T3>  fn(){
		return Function((arg1, arg2) -> {
			isCalled = true;
			return fn.apply(arg1,arg2);
		});
	}

	public Boolean isCalled(){
		return isCalled;
	}

}
