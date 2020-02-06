package com.github.stcarolas.enki;

import java.io.InputStreamReader;
import java.util.Map;
import com.github.stcarolas.enki.core.CloneURLType;
import com.github.stcarolas.enki.core.EnkiRunner;
import com.github.stcarolas.enki.provider.OneRepoProvider;
import com.google.gson.Gson;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import lombok.Builder;
import lombok.val;
import lombok.extern.log4j.Log4j2;

@Builder
@Log4j2
public class GithubWebhookHandler implements HttpHandler {

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        exchange.startBlocking();
        exchange.dispatch(
            new HttpHandler() {

                @Override
                public void handleRequest(HttpServerExchange exchange) throws Exception {
                    val request = new Gson()
                        .fromJson(new InputStreamReader(exchange.getInputStream()), Map.class);
                    String url = (String) ((Map) request.get("repository")).get("ssh_url");
                    EnkiRunner.builder()
                        .provider(new OneRepoProvider(url, CloneURLType.SSH))
                        .build()
                        .handle();
                }
            }
        );
    }
}
