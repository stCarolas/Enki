package com.github.stcarolas.enki.core.directory;

import java.io.File;
import java.util.function.Function;

import javax.inject.Named;
import javax.inject.Singleton;

import io.vavr.control.Try;
import static io.vavr.API.*;

@Singleton @Named("RawFileProvider")
public class LocateDirectory implements Function<String, Try<File>> {
	public static final String TEMPORARY_LOCATION = "/tmp/enki/";

	public Try<File> apply(String filename){
		return Success(new File(TEMPORARY_LOCATION + filename));
	}

}
