package com.github.stcarolas.enki.core;

import java.util.List;
import lombok.Builder;
import lombok.Singular;

@Builder
public class EnkiRunner {
    @Singular
    private List<RepoProvider> providers;

    @Singular
    private List<RepoHandler> analyzers;

    public void analyze() {
        providers.stream()
            .flatMap(
                provider -> {
                    return provider.getRepos().stream();
                }
            )
            .forEach(repo -> analyzers.forEach(analyzer -> analyzer.analyze(repo)));
    }
}
