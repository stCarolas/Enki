package com.github.stcarolas.enki.gitea.provider;

import static io.vavr.collection.List.empty;
import static io.vavr.control.Option.some;

import com.github.stcarolas.enki.core.Repo;
import com.github.stcarolas.enki.core.RepoProvider;
import com.github.stcarolas.enki.core.provider.DefaultRepoProviderStrategiesFactory;

import io.vavr.collection.Seq;
import io.vavr.control.Option;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
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
		return DefaultRepoProviderStrategiesFactory
			.gitSshClone(repo, repo.getSshUrl())
			.get()
			.map( success -> repo )
			.getOrNull();
	}

	@Override
	public GiteaRepo upload(Repo repo) {
		if (repo instanceof GiteaRepo){
			return DefaultRepoProviderStrategiesFactory
				.gitSshPush(repo, ((GiteaRepo)repo).getSshUrl())
				.get()
				.map(success -> (GiteaRepo)repo)
				.getOrNull();
		}
		return null;
	}

	public static RepoProvider<GiteaRepo> create(GiteaRepoProvider provider){
		return new GiteaSshRepoProvider(some(provider));
	}
	
}
