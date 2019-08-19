package com.github.stcarolas.enki;

import com.github.stcarolas.enki.discord.commithook.DiscordCommitHookCreator;
import com.github.stcarolas.enki.logginganalyzers.LoggingAnalyzers;
import com.github.stcarolas.enki.gitea.provider.GiteaRepoProvider;
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
                    config.getString("gitea.organization")
                )
            )
            .analyzer(new LoggingAnalyzers())
            .analyzer(
                DiscordCommitHookCreator.builder()
                    .giteaBaseUrl(config.getString("gitea.url"))
                    .giteaUsername(config.getString("gitea.username"))
                    .giteaPassword(config.getString("gitea.password"))
                    .giteaOrganization(config.getString("gitea.organization"))
                    .discordToken(config.getString("discord.token"))
                    .discordServerName(config.getString("discord.server"))
                    .discordCategoryId(config.getString("discord.categoryId"))
                    .build()
                    .init()
            )
            .serverHost("0.0.0.0")
            .port(8080)
            .build()
            .start();
    }
}
