package com.github.stcarolas.enki.core;

import java.io.File;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface Repo {
    public UUID getId();
    public String getName();
    public Map<CloneURLType, String> getCloneUrls();
    public Optional<File> getDirectory();
    public RepoProvider getRepoProvider();
}
