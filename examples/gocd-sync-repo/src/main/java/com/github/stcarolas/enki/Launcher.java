package com.github.stcarolas.enki;

import com.github.stcarolas.enki.gitea.provider.GiteaRepoProvider;
import com.github.stcarolas.enki.gocd.analyzers.GocdConfigRepoSync;
import com.github.stcarolas.enki.logginganalyzers.LoggingAnalyzers;
import com.typesafe.config.ConfigFactory;
import lombok.Builder;
import lombok.val;

@Builder
public class Launcher {

    public static void main(String[] args) {
        val config = ConfigFactory.load();
        EnkiServer.builder()
            .provider(
                new GiteaRepoProvider(
                    config.getString("gitea.url"),
                    config.getString("gitea.organization"),
                    config.getString("gitea.username"),
                    config.getString("gitea.password")
                )
            )
            .analyzer(new LoggingAnalyzers())
            .analyzer(
                new GocdConfigRepoSync(
                    config.getString("gocd.url"),
                    config.getString("gocd.username"),
                    config.getString("gocd.password")
                )
            )
            .serverHost("0.0.0.0")
            .port(8080)
            .build()
            .start();
    }
}
