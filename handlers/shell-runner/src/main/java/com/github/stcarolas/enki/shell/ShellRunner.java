package com.github.stcarolas.enki.shell;

import java.io.File;
import java.util.function.Function;
import com.github.stcarolas.enki.core.repo.local.LocalRepo;
import com.github.stcarolas.enki.core.repo.local.LocalRepoHandler;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import static io.vavr.API.*;

@Log4j2
@RequiredArgsConstructor
public class ShellRunner implements LocalRepoHandler, Function<File, Try<Void>> {
	private final String command;

	public Try<Void> apply(File directory) {
		return Try(
			() -> Runtime.getRuntime()
				.exec(
					new String[] { "/bin/sh", "-c", command },
					new String[] {},
					directory
				)
		)
			.andThen( result -> log.info("shell command executed"))
			.onFailure(error -> log.error(error))
			.andThenTry(process -> log.error(new String(process.getErrorStream().readAllBytes())))
			.filter(result -> result.exitValue() == 0)
			.map(it -> null);
	}

	@Override
	public Try<LocalRepo> handle(LocalRepo repo) {
		return repo.apply(this).map(result -> repo);
	}
}
