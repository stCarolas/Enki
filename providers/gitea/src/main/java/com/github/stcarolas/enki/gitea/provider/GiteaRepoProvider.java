package com.github.stcarolas.enki.gitea.provider;

import static com.github.stcarolas.enki.core.repo.DefaultRepoStrategiesFactory.commit;
import static com.github.stcarolas.enki.core.repo.DefaultRepoStrategiesFactory.directory;
import static com.github.stcarolas.enki.core.repo.DefaultRepoStrategiesFactory.identity;
import static com.github.stcarolas.enki.core.repo.DefaultRepoStrategiesFactory.name;
import static com.github.stcarolas.enki.core.repo.DefaultRepoStrategiesFactory.providers;
import static io.vavr.collection.List.ofAll;
import static io.vavr.collection.List.empty;
import static io.vavr.control.Option.some;

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
import io.vavr.Function4;
import io.vavr.collection.Seq;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Log4j2
public class GiteaRepoProvider implements RepoProvider<GiteaRepo> {
	private final Option<GiteaProviderSettings> settings;

	@Override
	public Seq<GiteaRepo> repositories() {
		return some( Function4.of(this::listRepositories) )

		.flatMap( fn -> settings.flatMap( it -> it.getBaseUrl() ).map( url -> fn.apply(url) )
				.onEmpty(() -> log.error("missing gitea url provider")))

		.flatMap( fn -> settings.flatMap( it -> it.getUsername() ).map( username -> fn.apply(username) )
				.onEmpty(() -> log.error("missing username to login into Gitea")))

		.flatMap( fn -> settings.flatMap( it -> it.getPassword() ).map( password -> fn.apply(password) )
				.onEmpty(() -> log.error("missing password to login into Gitea")))

		.flatMap( fn -> settings.flatMap( it -> it.getOrganization() ).map( org -> fn.apply(org) )
				.onEmpty(() -> log.error("missing organization to login into Gitea")))

		.getOrElse(empty());
	}

	private Seq<GiteaRepo> listRepositories(String url, String user, String password, String org){
		val authInterceptor = new BasicAuthRequestInterceptor(user, password);
		val organizations = Feign.builder()
			.decoder(new JacksonDecoder(Arrays.asList(new JavaTimeModule())))
			.requestInterceptor(authInterceptor)
			.target(OrganizationApi.class, url);
		return ofAll(organizations.orgListRepos(org, new HashMap<>()))
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

	public static RepoProvider<GiteaRepo> usingSsh(GiteaProviderSettings settings){
		return GiteaSshRepoProvider.create(new GiteaRepoProvider(settings));
	}

}
