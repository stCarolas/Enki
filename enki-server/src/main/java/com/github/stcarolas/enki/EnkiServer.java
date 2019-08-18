package com.github.stcarolas.enki;

import java.util.List;
import com.github.stcarolas.enki.core.EnkiRunner;
import com.github.stcarolas.enki.core.RepoHandler;
import com.github.stcarolas.enki.core.RepoProvider;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import lombok.Builder;
import lombok.Singular;
import lombok.val;
import lombok.extern.log4j.Log4j2;

@Builder
@Log4j2
public class EnkiServer {
    @Singular
    private List<RepoProvider> providers;

    @Singular
    private List<RepoHandler> analyzers;

    private String serverHost;
    private int port;

    public void start() {
        val enkiBuilder = EnkiRunner.builder();
        providers.forEach(provider -> enkiBuilder.provider(provider));
        analyzers.forEach(analyzer -> enkiBuilder.analyzer(analyzer));
        val enki = enkiBuilder.build();
        Undertow.builder()
            .addHttpListener(port, serverHost)
            .setHandler(
                new HttpHandler() {

                    @Override
                    public void handleRequest(final HttpServerExchange exchange)
                        throws Exception {
                        log.info(
                            "Start to analyze with {} providers and {} analyzers",
                            providers.size(),
                            analyzers.size()
                        );
                        enki.analyze();
                    }
                }
            )
            .build()
            .start();
    }
}
