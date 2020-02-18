package com.github.stcarolas.enki.gitea.provider;

import static com.github.stcarolas.enki.core.repo.DefaultRepoStrategiesFactory.commit;
import static com.github.stcarolas.enki.core.repo.DefaultRepoStrategiesFactory.directory;
import static com.github.stcarolas.enki.core.repo.DefaultRepoStrategiesFactory.identity;
import static com.github.stcarolas.enki.core.repo.DefaultRepoStrategiesFactory.name;
import static com.github.stcarolas.enki.core.repo.DefaultRepoStrategiesFactory.providers;
import static io.vavr.collection.List.ofAll;
import java.util.Arrays;
import java.util.HashMap;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.stcarolas.enki.core.Repo;
import com.github.stcarolas.enki.core.RepoProvider;
import com.github.stcarolas.gitea.api.OrganizationApi;
import com.github.stcarolas.gitea.api.Repository;
import feign.Feign;
import feign.auth.BasicAuthRequestInterceptor;
import feign.jackson.JacksonDecoder;
import io.vavr.collection.Seq;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor
public class GiteaRepoProvider implements RepoProvider<GiteaRepo> {
	private final String baseUrl;
	private final String organization;
	private final String username;
	private final String password;

	@Override
	public Seq<GiteaRepo> repositories() {
		val authInterceptor = new BasicAuthRequestInterceptor(username, password);
		val organizations = Feign.builder()
			.decoder(new JacksonDecoder(Arrays.asList(new JavaTimeModule())))
			.requestInterceptor(authInterceptor)
			.target(OrganizationApi.class, baseUrl);
		return ofAll(organizations.orgListRepos(organization, new HashMap<>()))
			.map(repo -> convert(repo));
	}

	@Override
	public Option<GiteaRepo> download(GiteaRepo repo) {
		return Option.none();
	}

	@Override
	public Option<GiteaRepo> upload(Repo repo) {
		return Option.none();
	}

	public GiteaRepo convert(Repository repo) {
		var giteaRepo = new GiteaRepo();
		giteaRepo.setNameStrategy(name(repo.getName()))
			.setDirectoryStrategy(directory(giteaRepo))
			.setCommitStrategy(commit(giteaRepo))
			.setProvidersStrategy(providers(Arrays.asList(this)))
			.setIdentityStrategy(identity());
		return giteaRepo;
	}
}
