package com.github.stcarolas.enki;

import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.github.stcarolas.enki.core.CloneURLType;
import com.github.stcarolas.enki.core.EnkiRunner;
import com.github.stcarolas.enki.core.Repo;
import com.github.stcarolas.enki.core.RepoHandler;
import com.github.stcarolas.enki.core.RepoProvider;
import com.github.stcarolas.enki.core.functions.FilterFunction;
import com.google.gson.Gson;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import lombok.Builder;
import lombok.Singular;
import lombok.val;
import lombok.extern.log4j.Log4j2;

@Builder
@Log4j2
public class GithubWebhookHandler<T extends Repo> implements HttpHandler {
    private RepoProvider<T> provider;
    @Singular
    private List<RepoHandler<T>> handlers;

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        exchange.startBlocking();
        exchange.dispatch(
            new HttpHandler() {

                @Override
                public void handleRequest(HttpServerExchange exchange) throws Exception {
                    val request = new Gson()
                        .fromJson(
                            new InputStreamReader(exchange.getInputStream()),
                            Map.class
                        );
                    String url = (String) ((Map) request.get("repository"))
                        .get("ssh_url");
                    val selectByUrl = FilterFunction.<T>filter(
                        repo -> Objects.equals(
                            repo.getCloneUrls().get(CloneURLType.SSH),
                            url
                        )
                    );
                    EnkiRunner.<T>builder()
                        .provider(selectByUrl.from(provider))
                        .handlers(handlers)
                        .build()
                        .handle();
                }
            }
        );
    }
}
