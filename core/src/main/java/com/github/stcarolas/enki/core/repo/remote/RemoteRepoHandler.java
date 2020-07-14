package com.github.stcarolas.enki.core.repo.remote;

import io.vavr.control.Try;

public interface RemoteRepoHandler {
	Try<RemoteRepo> handle(RemoteRepo repo);
}
