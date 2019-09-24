package com.github.stcarolas.enki.core.impl;

import java.io.File;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import com.github.stcarolas.enki.core.CloneURLType;
import com.github.stcarolas.enki.core.Repo;
import com.github.stcarolas.enki.core.RepoProvider;
import org.eclipse.jgit.api.Git;
import io.vavr.control.Try;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import lombok.val;
import lombok.extern.log4j.Log4j2;

@Getter
@Builder
@Log4j2
public class GitRepo implements Repo {
    private final RepoProvider repoProvider;

    @Singular
    private final Map<CloneURLType, String> cloneUrls;

    private final String name;
    private final UUID id = UUID.randomUUID();

    public Optional<File> getDirectory() {
        val directory = new File("/tmp/enki/" + id.toString());
        if (directory.exists()) {
            log.info("Directory {} already exists", directory);
            return Optional.of(directory);
        }
        directory.mkdirs();
        return Try.of(
            () -> {
                Git.cloneRepository()
                    .setURI(cloneUrls.get(CloneURLType.SSH))
                    .setDirectory(directory)
                    .setTransportConfigCallback(new DefaultTransportConfigCallback())
                    .call();
                return directory;
            }
        )
            .onFailure(error -> log.error("error: {}", error))
            .toOptional();
    }

    @Override
    public void commitAndPush(String commitMessage) {
        val directory = new File("/tmp/enki/" + id.toString());
        if (!directory.exists()) {
            return;
        }
        Try.of(
            () -> {
                val repo = Git.open(directory);
                repo.add().addFilepattern(".").call();
                repo.commit().setMessage(commitMessage).call();
                return repo.push().call();
            }
        )
            .onFailure(error -> log.error("error: {}", error));
    }
}
