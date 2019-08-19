package com.github.stcarolas.enki;

import static com.github.stcarolas.enki.core.functions.TakeFunction.take;
import static com.github.stcarolas.enki.core.functions.FilterFunction.filter;
import java.util.List;
import java.util.stream.Collectors;
import com.github.stcarolas.enki.gitea.provider.GiteaRepoProvider;
import com.github.stcarolas.enki.github.archiverepo.GithubArchiveRepoHandler;
import com.github.stcarolas.enki.github.provider.GitHubRepoProvider;
import com.github.stcarolas.enki.logginganalyzers.LoggingAnalyzers;
import com.typesafe.config.ConfigFactory;
import lombok.Builder;
import lombok.val;
import lombok.extern.log4j.Log4j2;

@Builder
@Log4j2
public class Launcher {

    public static void main(String[] args) {
        val config = ConfigFactory.load();
        List<String> repos = new GiteaRepoProvider(
            config.getString("gitea.url"),
            config.getString("gitea.organization")
        )
            .getRepos()
            .stream()
            .map(
                item -> {
                    return item.getName();
                }
            )
            .collect(Collectors.toList());
        repos.forEach(
            item -> {
                log.info(item);
            }
        );
        EnkiServer.builder()
            .provider(
                filter(repo -> repos.contains(repo.getName()))
                    .from(
                        GitHubRepoProvider.builder()
                            .username(config.getString("github.username"))
                            .password(config.getString("github.password"))
                            .organization(config.getString("github.organization"))
                            .build()
                    )
            )
            .analyzer(new LoggingAnalyzers())
            .analyzer(
                GithubArchiveRepoHandler.builder()
                    .username(config.getString("github.username"))
                    .password(config.getString("github.password"))
                    .organization(config.getString("github.organization"))
                    .build()
            )
            .serverHost("0.0.0.0")
            .port(8080)
            .build()
            .start();
    }
}
