package com.github.stcarolas.enki.provider;

import java.util.Arrays;
import java.util.List;
import com.github.stcarolas.enki.core.CloneURLType;
import com.github.stcarolas.enki.core.Repo;
import com.github.stcarolas.enki.core.RepoProvider;
import com.github.stcarolas.enki.core.impl.GitRepo;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OneRepoProvider implements RepoProvider {
	private final String cloneUrl;
	private final CloneURLType urlType;

	@Override
	public List<Repo> getRepos() {
		return Arrays.asList(GitRepo.builder().cloneUrl(urlType, cloneUrl).build());
	}
}
