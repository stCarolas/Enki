package com.github.stcarolas.enki.bitbucket.provider;

import com.github.stcarolas.enki.core.Repo;
import com.github.stcarolas.enki.core.provider.DefaultRepoProviderStrategiesFactory;

import static io.vavr.control.Option.none;
import io.vavr.control.Option;
import lombok.Builder;

@Builder
public class BitbucketSshRepoProvider extends BitbucketRepoProvider {

	@Override
	public Option<BitbucketRepo> download(BitbucketRepo repo) {
		return DefaultRepoProviderStrategiesFactory
			.gitSshClone(repo, repo.getSshUrl())
			.get()
			.map( success -> repo );
	}

	@Override
	public Option<BitbucketRepo> upload(Repo repo) {
		if (repo instanceof BitbucketRepo){
			return DefaultRepoProviderStrategiesFactory
				.gitSshPush(repo, ((BitbucketRepo)repo).getSshUrl())
				.get()
				.map(success -> (BitbucketRepo)repo);
		}
		return none();
	}

}
