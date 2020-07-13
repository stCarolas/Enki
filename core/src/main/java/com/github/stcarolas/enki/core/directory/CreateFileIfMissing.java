package com.github.stcarolas.enki.core.directory;

import static io.vavr.API.Success;
import static io.vavr.API.Try;
import java.io.File;
import java.util.function.Function;
import javax.inject.Named;
import javax.inject.Singleton;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Named("EnsuredFileProvider")
public class CreateFileIfMissing implements Function<String, Try<File>> {
	private final Function<String, Try<File>> fileProvider;

	public Try<File> apply(String path) {
		return fileProvider.apply(path)
			.flatMap(
				file -> file.exists() 
					? processExistedDirectory(file)
					: createNewDirectory(file)
			);
	}

	private Try<File> createNewDirectory(File file) {
		return Try(file::mkdir)
			.map(result -> file)
			.onFailure(
				error -> log.error("error while creating directory {}: {}", file, error)
			)
			.onSuccess(it -> log.info("directory {} was created", it.getPath()));
	}

	private Try<File> processExistedDirectory(File file) {
		return Success(file)
			.peek(it -> log.info("directory {} was existed", it.getPath()));
	}

	public CreateFileIfMissing(
		@Named("RawFileProvider") Function<String, Try<File>> fileProvider
	) {
		this.fileProvider = fileProvider;
	}
}
