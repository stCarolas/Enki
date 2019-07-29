package com.github.stcarolas;

import java.util.List;

import com.github.stcarolas.model.RepoProvider;

import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

public class EnkiServer {

    public EnkiServer(
        List<RepoProvider> providers, List<Analyzer> analyzers, 
        String serverHost, int port
    ){
        Undertow.builder()
            .addHttpListener(port, serverHost)
            .setHandler(new HttpHandler() {
                @Override
                public void handleRequest(final HttpServerExchange exchange) throws Exception {
                    analyze(providers, analyzers);
                }
            })
            .build()
            .start();
    }

    private void analyze(List<RepoProvider> providers, List<Analyzer> analyzers){
        providers.stream()
            .flatMap( provider -> {
                return provider.getRepos().stream();
            }).forEach( repo -> 
                analyzers.forEach(analyzer -> analyzer.analyze(repo))
            );
    }
    
}
