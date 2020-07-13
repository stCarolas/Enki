package com.github.stcarolas.enki.shell;

import java.io.File;
import com.github.stcarolas.enki.core.repo.local.LocalRepoFileHandler;

import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import static io.vavr.API.*;

@Log4j2
@RequiredArgsConstructor
public class ShellRunner implements LocalRepoFileHandler {
	private final String command;

	public Try<Void> handle(File directory) {
		return Try(
			() -> Runtime.getRuntime()
				.exec(
					new String[] { "/bin/sh", "-c", command },
					new String[] {},
					directory
				)
		)
			.onFailure(error -> log.error(error))
			.filter(result -> result.exitValue() == 0)
			.map(it -> null);
	}

}
