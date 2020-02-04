package com.github.stcarolas.enki.discordbot.providers;

import java.util.Arrays;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import dagger.Provides;
import feign.Feign;
import feign.auth.BasicAuthRequestInterceptor;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import rocks.mango.gitea.OrganizationApi;

@Log4j2
@dagger.Module
public class OrganizationApiProvider {
    private final BasicAuthRequestInterceptor auth;
    private final Configuration configuration;

    public OrganizationApiProvider(Configuration configuration) {
        log.info("Loaded configuration: {}", configuration);
        this.configuration = configuration;
        this.auth =
            new BasicAuthRequestInterceptor(
                configuration.getUsername(),
                configuration.getPassword()
            );
    }

    @Provides
    public OrganizationApi provide() {
        return Feign.builder()
            .decoder(new JacksonDecoder(Arrays.asList((Module) new JavaTimeModule())))
            .encoder(new JacksonEncoder())
            .requestInterceptor(auth)
            .target(OrganizationApi.class, configuration.getGiteaUrl());
    }

    @Builder
    @Getter
    public static class Configuration {
        private final String username;
        private final String password;
        private final String organization;
        private final String giteaUrl;
    }
}
