package com.github.stcarolas.enki.gitea.provider;

import static io.vavr.collection.List.empty;
import static io.vavr.control.Option.of;

import com.github.stcarolas.enki.core.Repo;
import com.github.stcarolas.enki.core.RepoProvider;
import com.github.stcarolas.enki.core.provider.DefaultRepoProviderStrategiesFactory;

import io.vavr.collection.Seq;
import io.vavr.control.Option;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Log4j2
public class GiteaSshRepoProvider implements RepoProvider<GiteaRepo> {

	private final Option<GiteaRepoProvider> provider;

	@Override
	public Seq<GiteaRepo> repositories() {
		return provider
			.map(GiteaRepoProvider::repositories)
			.getOrElse(empty());
	}

	@Override
	public GiteaRepo download(GiteaRepo repo) {
		return of(
				DefaultRepoProviderStrategiesFactory
				.gitSshClone(repo, repo.getSshUrl())
				.get()
			)
			.onEmpty(() -> log.error("download failed"))
			.map( success -> repo )
			.getOrNull()
			;
	}

	@Override
	public GiteaRepo upload(Repo repo) {
		if (repo instanceof GiteaRepo){
			return of(DefaultRepoProviderStrategiesFactory
					.gitSshPush(repo)
					.get()
				)
				.filter( results -> results.iterator().hasNext() )
				.onEmpty(() -> log.error("upload failed"))
				.map(success -> (GiteaRepo)repo)
				.getOrNull();
		}
		return null;
	}

	public static RepoProvider<GiteaRepo> create(GiteaRepoProvider provider){
		return new GiteaSshRepoProvider(of(provider));
	}
	
}
