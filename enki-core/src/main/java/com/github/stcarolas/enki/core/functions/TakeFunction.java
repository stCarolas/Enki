package com.github.stcarolas.enki.core.functions;

import java.util.List;
import com.github.stcarolas.enki.core.Function;
import com.github.stcarolas.enki.core.Repo;
import com.github.stcarolas.enki.core.RepoProvider;
import lombok.RequiredArgsConstructor;

public class TakeFunction implements Function {
    private final int count;

    private TakeFunction(int count) {
        this.count = count;
    }

    public static TakeFunction take(int count) {
        return new TakeFunction(count);
    }

    @Override
    public RepoProvider from(RepoProvider source) {
        return new InternalRepoProvider(source, count);
    }

    @RequiredArgsConstructor
    public class InternalRepoProvider implements RepoProvider {
        private final RepoProvider source;
        private final int count;

        @Override
        public List<Repo> getRepos() {
            return source.getRepos().subList(0, count);
        }
    }
}
