package com.github.stcarolas.enki.bitbucket.provider;

import com.github.stcarolas.enki.core.Repo;
import com.github.stcarolas.enki.core.provider.DefaultRepoProviderStrategiesFactory;
import static io.vavr.API.Option;

import io.vavr.control.Option;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;

@Builder
@Log4j2
public class BitbucketSshRepoProvider extends BitbucketRepoProvider {

	@Override
	public BitbucketRepo download(BitbucketRepo repo) {
		return Option(
				DefaultRepoProviderStrategiesFactory
					.gitSshClone(repo, repo.getSshUrl())
					.get()
			)
			.onEmpty(() -> log.error("download failed"))
			.map( success -> repo )
			.getOrNull();
	}

	@Override
	public BitbucketRepo upload(Repo repo) {
		if (repo instanceof BitbucketRepo){
			return Option(
					DefaultRepoProviderStrategiesFactory
						.gitSshPush(repo)
						.get()
				)
				.filter( results -> results.iterator().hasNext() )
				.onEmpty(() -> log.error("upload failed"))
				.map(success -> (BitbucketRepo)repo)
				.getOrNull();
		}
		return null;
	}

}
