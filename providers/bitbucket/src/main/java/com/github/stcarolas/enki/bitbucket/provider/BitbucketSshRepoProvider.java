package com.github.stcarolas.enki.bitbucket.provider;

import com.github.stcarolas.enki.core.Repo;
import com.github.stcarolas.enki.core.provider.DefaultRepoProviderStrategiesFactory;

import lombok.Builder;

@Builder
public class BitbucketSshRepoProvider extends BitbucketRepoProvider {

	@Override
	public BitbucketRepo download(BitbucketRepo repo) {
		return DefaultRepoProviderStrategiesFactory
			.gitSshClone(repo, repo.getSshUrl())
			.get()
			.map( success -> repo )
			.getOrNull();
	}

	@Override
	public BitbucketRepo upload(Repo repo) {
		if (repo instanceof BitbucketRepo){
			return DefaultRepoProviderStrategiesFactory
				.gitSshPush(repo, ((BitbucketRepo)repo).getSshUrl())
				.get()
				.map(success -> (BitbucketRepo)repo)
				.getOrNull();
		}
		return null;
	}

}
