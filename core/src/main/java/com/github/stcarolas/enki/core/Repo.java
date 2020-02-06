package com.github.stcarolas.enki.core;

import java.io.File;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface Repo {

    UUID getId();

    String getName();

    Map<CloneURLType, String> getCloneUrls();

    Optional<File> getDirectory();

    RepoProvider getRepoProvider();

    @Deprecated void commitAndPush(String commitMessage);
}
