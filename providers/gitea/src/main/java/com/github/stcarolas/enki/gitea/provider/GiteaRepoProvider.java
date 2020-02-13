package com.github.stcarolas.enki.gitea.provider;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.stcarolas.enki.core.CloneURLType;
import com.github.stcarolas.enki.core.Repo;
import com.github.stcarolas.enki.core.RepoProvider;
import com.github.stcarolas.enki.core.impl.GitRepo;
import com.github.stcarolas.gitea.api.OrganizationApi;
import com.github.stcarolas.gitea.api.Repository;

import feign.Feign;
import feign.auth.BasicAuthRequestInterceptor;
import feign.jackson.JacksonDecoder;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor
public class GiteaRepoProvider implements RepoProvider {
	private final String baseUrl;
	private final String organization;
	private final String username;
	private final String password;

	@Override
	public List<Repo> getRepos() {
		val authInterceptor = new BasicAuthRequestInterceptor(username, password);
		val organizations = Feign.builder()
			.decoder(new JacksonDecoder(Arrays.asList(new JavaTimeModule())))
			.requestInterceptor(authInterceptor)
			.target(OrganizationApi.class, baseUrl);
		return organizations.orgListRepos(organization,new HashMap<>())
			.stream()
			.map(repo -> convert(repo))
			.collect(Collectors.toList());
	}

	public Repo convert(Repository repo) {
		return GitRepo.builder()
			.name(repo.getName())
			.cloneUrl(CloneURLType.SSH, repo.getSshUrl())
			.build();
	}
}
