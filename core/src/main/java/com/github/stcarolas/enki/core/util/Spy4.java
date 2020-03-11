package com.github.stcarolas.enki.core.util;

import static io.vavr.API.*;

import java.util.concurrent.atomic.AtomicInteger;

import io.vavr.Function4;

public class Spy4<T1,T2,T3, T4, RETURN> {
	private Function4<T1,T2,T3, T4, RETURN> fn;
	private Boolean isCalled = false;
	private AtomicInteger counter = new AtomicInteger(0);
	private T1 arg1;
	private T2 arg2;
	private T3 arg3;
	private T4 arg4;

	public static <T1,T2,T3,T4,RETURN>Spy4<T1,T2,T3,T4,RETURN> spy(Function4<T1,T2,T3,T4,RETURN> fn){
		var spy = new Spy4<T1,T2,T3,T4,RETURN>();
		spy.fn = fn;
		return spy;
	}

	public Function4<T1,T2,T3,T4,RETURN>  fn(){
		return Function((arg1, arg2, arg3, arg4) -> {
			isCalled = true;
			this.arg1 = arg1;
			this.arg2 = arg2;
			this.arg3 = arg3;
			this.arg4 = arg4;
			counter.incrementAndGet();
			return fn.apply(arg1,arg2,arg3,arg4);
		});
	}

	public Boolean isCalled(){
		return isCalled;
	}

	public T1 arg1(){
		return arg1;
	}

	public T2 arg2(){
		return arg2;
	}

	public T3 arg3(){
		return arg3;
	}

	public T4 arg4(){
		return arg4;
	}

}
