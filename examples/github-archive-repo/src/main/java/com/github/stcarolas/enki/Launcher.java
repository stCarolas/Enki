package com.github.stcarolas.enki;

import com.github.stcarolas.enki.gitea.analyzers.GiteaMirrorAnalyzer;
import com.github.stcarolas.enki.github.provider.GitHubRepoProvider;
import com.github.stcarolas.enki.logginganalyzers.LoggingAnalyzers;
import com.typesafe.config.ConfigFactory;
import lombok.Builder;
import lombok.val;
import static com.github.stcarolas.enki.core.functions.FilterFunction.filter;

@Builder
public class Launcher {

    public static void main(String[] args) {
        val config = ConfigFactory.load();
        EnkiServer.builder()
            .provider(
                filter(repo -> repo.getName().contains("receipt-1c-service"))
                    .from(
                        GitHubRepoProvider.builder()
                            .username(config.getString("github.username"))
                            .password(config.getString("github.password"))
                            .organization(config.getString("github.organization"))
                            .build()
                    )
            )
            .analyzer(new LoggingAnalyzers())
            .serverHost("0.0.0.0")
            .port(8080)
            .build()
            .start();
    }
}
