package com.github.stcarolas.enki;

import com.github.stcarolas.enki.github.provider.GitHubRepoProvider;
import com.github.stcarolas.enki.logginghandlers.LoggingAnalyzers;
import com.typesafe.config.ConfigFactory;
import lombok.Builder;
import lombok.val;

@Builder
public class Launcher {

    public static void main(String[] args) {
        val config = ConfigFactory.load();
        EnkiServer.builder()
            .provider(
                GitHubRepoProvider.builder()
                    .username(config.getString("github.username"))
                    .password(config.getString("github.password"))
                    .organization(config.getString("github.organization"))
                    .build()
            )
            .handler(new LoggingAnalyzers())
            .serverHost("0.0.0.0")
            .port(8080)
            .build()
            .start();
    }
}
