package com.github.stcarolas.enki.gocd.handlers;

import java.util.Arrays;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.stcarolas.enki.core.CloneURLType;
import com.github.stcarolas.enki.core.Repo;
import com.github.stcarolas.enki.core.RepoHandler;
import com.github.stcarolas.enki.gocd.api.GoCD;
import com.github.stcarolas.enki.gocd.model.NotifyMaterialRequest;
import feign.Feign;
import feign.auth.BasicAuthRequestInterceptor;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.val;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class GocdWebhookCaller implements RepoHandler {
    private final GoCD gocd;

    public GocdWebhookCaller(String gocdUrl, String gocdUsername, String gocdPassword) {
        val interceptor = new BasicAuthRequestInterceptor(gocdUsername, gocdPassword);
        gocd =
            Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder(Arrays.asList((Module) new JavaTimeModule())))
                .requestInterceptor(interceptor)
                .target(GoCD.class, gocdUrl);
    }

    @Override
    public void handle(Repo repo) {
        gocd.notifyGitMaterials(
            NotifyMaterialRequest.builder()
                .repositoryUrl(repo.getCloneUrls().get(CloneURLType.SSH))
                .build()
        );
    }
}
