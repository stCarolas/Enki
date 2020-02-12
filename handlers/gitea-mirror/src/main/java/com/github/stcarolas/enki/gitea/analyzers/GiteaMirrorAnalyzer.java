package com.github.stcarolas.enki.gitea.handlers;

import java.util.Arrays;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.stcarolas.enki.core.Repo;
import com.github.stcarolas.enki.core.RepoHandler;
import com.github.stcarolas.enki.core.impl.DefaultTransportConfigCallback;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.URIish;
import feign.Feign;
import feign.auth.BasicAuthRequestInterceptor;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import io.vavr.control.Try;
import lombok.Builder;
import lombok.val;
import lombok.extern.log4j.Log4j2;
import rocks.mango.gitea.CreateRepoOption;
import rocks.mango.gitea.OrganizationApi;

@Builder
@Log4j2
public class GiteaMirrorAnalyzer implements RepoHandler {
	private final String username;
	private final String password;
	private final String baseUrl;
	private final String organization;

	@Override
	public void handle(Repo repo) {
		val authInterceptor = new BasicAuthRequestInterceptor(username, password);
		val organizations = Feign.builder()
			.decoder(new JacksonDecoder(Arrays.asList((Module) new JavaTimeModule())))
			.encoder(new JacksonEncoder())
			.requestInterceptor(authInterceptor)
			.target(OrganizationApi.class, baseUrl);
		val newRepo = organizations.createOrgRepo(
			organization,
			new CreateRepoOption().name(repo.getName())
		);
		repo.getDirectory()
			.ifPresent(
				dir -> Try.of(
					() -> {
						val git = Git.open(dir);
						git.remoteSetUrl()
							.setRemoteName("origin")
							.setRemoteUri(new URIish(newRepo.getSshUrl()))
							.call();
						return git.push()
							.setTransportConfigCallback(new DefaultTransportConfigCallback())
							.call();
					}
				)
					.onFailure(error -> log.error(error))
			);
	}
}
