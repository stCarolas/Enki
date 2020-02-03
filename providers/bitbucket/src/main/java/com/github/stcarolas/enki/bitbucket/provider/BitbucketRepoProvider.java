package com.github.stcarolas.enki.bitbucket.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import com.cdancy.bitbucket.rest.BitbucketClient;
import com.cdancy.bitbucket.rest.domain.repository.Repository;
import com.github.stcarolas.enki.core.CloneURLType;
import com.github.stcarolas.enki.core.Repo;
import com.github.stcarolas.enki.core.RepoProvider;
import com.github.stcarolas.enki.core.impl.GitRepo;
import io.vavr.control.Try;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Builder
public class BitbucketRepoProvider implements RepoProvider {
    private String endpoint;
    private String token;

    @Override
    public List<Repo> getRepos() {
        return Try.of(
            () -> {
                BitbucketClient client = BitbucketClient.builder()
                    .endPoint(endpoint)
                    .token(token)
                    .build();

                return client.api()
                    .repositoryApi()
                    .listAll(null, null, null, null, null, null)
                    .values();
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
                        .filter(repo -> repo.isPresent())
                        .map(repo -> repo.get())
                        .collect(Collectors.toList());
                }
            )
            .getOrElse(new ArrayList<>());
    }

    private Optional<Repo> convert(Repository repo) {
        return repo.links()
            .clone()
            .stream()
            .filter(href -> "ssh".equals(href.get("name")))
            .map(
                sshUrlHolder -> {
                    return (Repo)GitRepo.builder()
                        .name(repo.name())
                        .cloneUrl(CloneURLType.SSH, sshUrlHolder.get("href"))
                        .repoProvider(this)
                        .build();
                }
            )
            .findFirst();
    }
}
