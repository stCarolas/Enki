package com.github.stcarolas.enki.github.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.github.stcarolas.enki.core.CloneURLType;
import com.github.stcarolas.enki.core.Repo;
import com.github.stcarolas.enki.core.RepoProvider;
import com.github.stcarolas.enki.core.impl.GitRepo;

import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.models.Project;

import io.vavr.control.Try;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Builder
public class GitlabRepoProvider implements RepoProvider {
    private String endpoint;
    private String token;

    @Override
    public List<Repo> getRepos() {
        return Try.of(
            () -> {
                return new GitLabApi(endpoint, token).getProjectApi().getProjects();
            }
        )
            .onFailure(
                error -> {
                    log.error(error);
                }
            )
            .map(
                repos -> {
                    return repos.stream()
                        .map(repo -> convert(repo))
                        .collect(Collectors.toList());
                }
            )
            .getOrElse(new ArrayList<>());
    }

    private Repo convert(Project repo) {
        return GitRepo.builder()
            .name(repo.getName())
            .cloneUrl(CloneURLType.SSH, repo.getSshUrlToRepo())
            .repoProvider(this)
            .build();
    }
}
