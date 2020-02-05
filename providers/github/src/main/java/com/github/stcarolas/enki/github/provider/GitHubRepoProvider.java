package com.github.stcarolas.enki.github.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.github.stcarolas.enki.core.CloneURLType;
import com.github.stcarolas.enki.core.Repo;
import com.github.stcarolas.enki.core.RepoProvider;
import com.github.stcarolas.enki.core.impl.GitRepo;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.eclipse.jgit.util.StringUtils;

import io.vavr.control.Try;
import lombok.Builder;
import lombok.val;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Builder
public class GitHubRepoProvider implements RepoProvider {
    private String username;
    private String password;
    private String organization;

    @Override
    public List<Repo> getRepos() {
        return Try.of(
            () -> {
                val repoService = new RepositoryService();
                repoService.getClient().setCredentials(username, password);
                if (StringUtils.isEmptyOrNull(organization)){
                    return repoService.getRepositories();
                }
                return repoService.getOrgRepositories(organization);
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

    private Repo convert(Repository repo) {
        return GitRepo.builder()
            .name(repo.getName())
            .cloneUrl(CloneURLType.SSH, repo.getSshUrl())
            .repoProvider(this)
            .build();
    }
}
