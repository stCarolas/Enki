package com.github.stcarolas.enki;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.github.stcarolas.enki.core.CloneURLType;
import com.github.stcarolas.enki.core.EnkiRunner;
import com.github.stcarolas.enki.core.Repo;
import com.github.stcarolas.enki.core.RepoHandler;
import com.github.stcarolas.enki.core.RepoProvider;

public class RepoRegistry implements RepoHandler, RepoProvider {
    Map<CloneURLType, Map<String, Repo>> repos;

    public RepoRegistry(List<RepoProvider> providers) {
        repos = new HashMap<>();
        repos.put(CloneURLType.SSH, new ConcurrentHashMap<>());
        EnkiRunner.builder().providers(providers).handler(this).build().handle();
    }

    public Repo get(CloneURLType urlType, String cloneUrl) {
        return repos.get(urlType).get(cloneUrl);
    }

    @Override
    public void handle(Repo repo) {
        repo.getCloneUrls()
            .forEach(
                (key, value) -> {
                    repos.get(key).putIfAbsent(value, repo);
                }
            );
    }

	@Override
	public List<Repo> getRepos() {
        List<Repo> allRepos = new ArrayList<>();
        repos.values().forEach( listByUrlType -> allRepos.addAll(listByUrlType.values()) );
        return allRepos;
	}
}
