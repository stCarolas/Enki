package com.github.stcarolas.enki.gitea.provider;

import static com.github.stcarolas.enki.core.repo.DefaultRepoStrategiesFactory.commit;
import static com.github.stcarolas.enki.core.repo.DefaultRepoStrategiesFactory.directory;
import static com.github.stcarolas.enki.core.repo.DefaultRepoStrategiesFactory.identity;
import static com.github.stcarolas.enki.core.repo.DefaultRepoStrategiesFactory.name;
import static com.github.stcarolas.enki.core.repo.DefaultRepoStrategiesFactory.providers;
import static io.vavr.Function4.lift;
import static io.vavr.collection.List.empty;
import static io.vavr.collection.List.ofAll;
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
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Log4j2
public final class GiteaRepoProvider implements RepoProvider<GiteaRepo> {
	private final Option<GiteaProviderSettings> settings;

	@Override
	public Seq<GiteaRepo> repositories() {
		return baseUrl().map( url -> listRepositories().apply(url) )
			.flatMap( api -> username().map( username -> api.apply(username) ) )
			.flatMap( api -> password().map( password -> api.apply(password) ) )
			.flatMap( 
				api -> organization()
					.flatMap( org -> api.apply(org)
						.onEmpty( () -> log.error("cant get list of repositories"))
					)
			)
			.getOrElse(empty());
	}

	private Function4<String, String, String, String, Option<Seq<GiteaRepo>>> listRepositories(){
		return lift( (url, username, password, organization) -> 
			ofAll(Feign.builder()
					.requestInterceptor(new BasicAuthRequestInterceptor(username, password))
					.decoder(new JacksonDecoder(Arrays.asList(new JavaTimeModule())))
					.target(OrganizationApi.class, url)
					.orgListRepos(organization, new HashMap<>())
			)
			.map( repo -> convert(repo) )
		);
	}

	@Override
	public GiteaRepo download(GiteaRepo repo) {
		return null;
	}

	@Override
	public GiteaRepo upload(Repo repo) {
		return null;
	}

	public GiteaRepo convert(Repository repo) {
		var giteaRepo = new GiteaRepo();
		giteaRepo.setDirectoryStrategy(directory(giteaRepo));
		giteaRepo.setCommitStrategy(commit(giteaRepo));
		giteaRepo.setProvidersStrategy(providers(Arrays.asList(this)));
		giteaRepo.setIdentityStrategy(identity());
		some(repo)
			.peek( it -> giteaRepo.setNameStrategy(name(it.getName())) );
		return giteaRepo;
	}

	public static RepoProvider<GiteaRepo> usingSsh(GiteaProviderSettings settings){
		return GiteaSshRepoProvider.create(new GiteaRepoProvider(some(settings)));
	}

	public Option<String> username(){
		return settings.flatMap( it -> it.getUsername() )
			.onEmpty(() -> log.error("missing username for Gitea"));
	}

	public Option<String> password(){
		return settings.flatMap( it -> it.getPassword() )
			.onEmpty(() -> log.error("missing password for Gitea"));
	}

	public Option<String> baseUrl(){
		return settings.flatMap( it -> it.getBaseUrl() )
			.onEmpty(() -> log.error("missing gitea url"));
	}

	public Option<String> organization(){
		return settings.flatMap( it -> it.getBaseUrl() )
			.onEmpty(() -> log.error("missing organization to login into Gitea"));
	}

}
