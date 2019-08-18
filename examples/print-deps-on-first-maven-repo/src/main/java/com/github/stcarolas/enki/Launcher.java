package com.github.stcarolas.enki;

import static com.github.stcarolas.enki.core.functions.TakeFunction.take;
import static com.github.stcarolas.enki.core.functions.FilterFunction.filter;
import com.github.stcarolas.enki.github.provider.GitHubRepoProvider;
import com.github.stcarolas.enki.mavendependencycollector.MavenDependencyCollector;
import com.typesafe.config.ConfigFactory;
import lombok.Builder;
import lombok.val;

@Builder
public class Launcher {

    public static void main(String[] args) {
        val config = ConfigFactory.load();
        EnkiServer.builder()
            .provider(
                take(1)
                    .from(
                        filter(repo -> repo.getName().contains("receipt-1c-service"))
                            .from(
                                GitHubRepoProvider.builder()
                                    .username(config.getString("github.username"))
                                    .password(config.getString("github.password"))
                                    .organization(config.getString("github.organization"))
                                    .build()
                            )
                    )
            )
            .analyzer(new MavenDependencyCollector())
            .serverHost("0.0.0.0")
            .port(8080)
            .build()
            .start();
    }
}
