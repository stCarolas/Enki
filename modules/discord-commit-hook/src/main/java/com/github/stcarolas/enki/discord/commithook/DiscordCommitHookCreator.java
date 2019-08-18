package com.github.stcarolas.enki.discord.commithook;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.stcarolas.enki.core.Repo;
import com.github.stcarolas.enki.core.RepoHandler;
import feign.Feign;
import feign.auth.BasicAuthRequestInterceptor;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import io.vavr.control.Try;
import lombok.Builder;
import lombok.val;
import lombok.extern.log4j.Log4j2;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Category;
import rocks.mango.gitea.CreateHookOption;
import rocks.mango.gitea.CreateHookOption.TypeEnum;
import rocks.mango.gitea.RepositoryApi;

@Builder
@Log4j2
public class DiscordCommitHookCreator implements RepoHandler {
    private final String giteaUsername;
    private final String giteaPassword;
    private final String giteaBaseUrl;
    private final String giteaOrganization;
    private final String discordToken;
    private final String discordServerName;
    private JDA discord;
    private final String discordCategoryId;

    public DiscordCommitHookCreator init() {
        discord =
            Try.of(
                () -> {
                    return new JDABuilder(discordToken).build().awaitReady();
                }
            )
                .get();
        return this;
    }

    @Override
    public void analyze(Repo repo) {
        val authInterceptor = new BasicAuthRequestInterceptor(giteaUsername, giteaPassword);
        val gitea = Feign.builder()
            .decoder(new JacksonDecoder(Arrays.asList((Module) new JavaTimeModule())))
            .encoder(new JacksonEncoder())
            .requestInterceptor(authInterceptor)
            .target(RepositoryApi.class, giteaBaseUrl);
        discord.getGuildsByName(discordServerName, false)
            .forEach(
                guild -> {
                    val channelName = constructChannelName(repo.getName());
                    guild.getCategoryById(discordCategoryId)
                        .createTextChannel(channelName)
                        .submit()
                        .thenAccept(
                            channel -> {
                                channel.createWebhook(constructWebhookName(channelName))
                                    .submit()
                                    .thenAccept(
                                        webhook -> {
                                            gitea.repoCreateHook(
                                                giteaOrganization,
                                                repo.getName(),
                                                new CreateHookOption()
                                                    .active(true)
                                                    .type(TypeEnum.DISCORD)
                                                    .config(giteaHookConfig(webhook.getUrl()))
                                                    .events(giteaWebhookEvents())
                                            );
                                        }
                                    );
                            }
                        );
                }
            );
    }

    private String constructChannelName(String repoName) {
        return repoName;
    }

    private List<String> giteaWebhookEvents() {
        return Arrays.asList("push");
    }

    private Map<String, String> giteaHookConfig(String webhook) {
        val config = new HashMap<String, String>();
        config.put("content-type", "json");
        config.put("url", webhook);
        return config;
    }

    private String constructWebhookName(String channelName) {
        return channelName + "-enki-webhook";
    }
}
