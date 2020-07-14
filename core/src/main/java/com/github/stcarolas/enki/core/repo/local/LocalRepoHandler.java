package com.github.stcarolas.enki.core.repo.local;

import java.io.File;

import io.vavr.control.Try;

public interface LocalRepoHandler {
	Try<LocalRepo> handle(LocalRepo directory);
}
