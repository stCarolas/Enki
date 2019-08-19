package com.github.stcarolas.enki.discord.commithook;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.Webhook;
import rocks.mango.gitea.CreateHookOption;
import rocks.mango.gitea.RepositoryApi;
import rocks.mango.gitea.CreateHookOption.TypeEnum;

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
        val authInterceptor = new BasicAuthRequestInterceptor(
            giteaUsername,
            giteaPassword
        );
        val gitea = Feign.builder()
            .decoder(new JacksonDecoder(Arrays.asList((Module) new JavaTimeModule())))
            .encoder(new JacksonEncoder())
            .requestInterceptor(authInterceptor)
            .target(RepositoryApi.class, giteaBaseUrl);
        discord.getGuildsByName(discordServerName, false)
            .forEach(
                guild -> {
                    val channelName = constructChannelName(repo.getName());
                    val category = guild.getCategoryById(discordCategoryId);
                    category.getTextChannels()
                        .stream()
                        .filter(
                            item -> {
                                return item.getName().equals(channelName);
                            }
                        )
                        .findAny()
                        // .map(
                    // findedChannel -> {
                    // return Optional.of(findedChannel);
                    // }
                    // )
                    // .orElse(createTextChannel(category, channelName))
                    .ifPresent(
                            channel -> {
                                log.info("working in channel:{}", channel.getName());
                                val webhookName = constructWebhookName(channelName);
                                log.info("construct webhook:{}", webhookName);
                                val webhookUrl = channel.retrieveWebhooks()
                                    .complete()
                                    .stream()
                                    .filter(
                                        hook -> {
                                            return hook.getName().equals(webhookName);
                                        }
                                    )
                                    .findFirst()
                                    .orElse(createWebhook(channel, webhookName))
                                    .getUrl();
                                log.info("webhook url: {}", webhookUrl);
                                gitea.repoListHooks(giteaOrganization, repo.getName())
                                    .forEach(
                                        hook -> {
                                            log.info(
                                                "Delete hook {} in repo {}",
                                                hook.getId(),
                                                repo.getName()
                                            );
                                            gitea.repoDeleteHook(
                                                giteaOrganization,
                                                repo.getName(),
                                                hook.getId()
                                            );
                                        }
                                    );
                                gitea.repoCreateHook(
                                    giteaOrganization,
                                    repo.getName(),
                                    new CreateHookOption()
                                        .active(true)
                                        .type(TypeEnum.DISCORD)
                                        .putConfigItem("content_type", "json")
                                        .putConfigItem("content-type", "json")
                                        .putConfigItem("url", webhookUrl)
                                        .events(giteaWebhookEvents())
                                );
                            }
                        );
                }
            );
    }

    private Webhook createWebhook(TextChannel channel, String webhookName) {
        log.info("create webhook:{}", webhookName);
        return channel.createWebhook(webhookName).complete();
    }

    private Optional<TextChannel> createTextChannel(Category category, String name) {
        log.info("Create text channel: {}", name);
        return Try.of(
            () -> {
                return category.createTextChannel(name).submit().get();
            }
        )
            .toOptional();
    }

    private String constructChannelName(String repoName) {
        return repoName;
    }

    private List<String> giteaWebhookEvents() {
        return Arrays.asList("push");
    }

    private String constructWebhookName(String channelName) {
        return channelName + "-enki-webhook";
    }
}
