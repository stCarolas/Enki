package com.github.stcarolas.enki.core.functions;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import com.github.stcarolas.enki.core.Repo;
import com.github.stcarolas.enki.core.RepoProvider;
import lombok.RequiredArgsConstructor;

public class FilterFunction implements com.github.stcarolas.enki.core.Function {
    private final Predicate<? super Repo> filter;

    private FilterFunction(Predicate<? super Repo> filter) {
        this.filter = filter;
    }

    public static FilterFunction filter(Predicate<? super Repo> filter) {
        return new FilterFunction(filter);
    }

    @Override
    public RepoProvider from(RepoProvider source) {
        return new InternalRepoProvider(source, filter);
    }

    @RequiredArgsConstructor
    public class InternalRepoProvider implements RepoProvider {
        private final RepoProvider source;
        private final Predicate<? super Repo> filter;

        @Override
        public List<Repo> getRepos() {
            return source.getRepos().stream().filter(filter).collect(Collectors.toList());
        }
    }
}
