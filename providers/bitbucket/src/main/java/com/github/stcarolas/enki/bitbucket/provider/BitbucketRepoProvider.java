package com.github.stcarolas.enki.bitbucket.provider;

import static com.github.stcarolas.enki.core.repo.DefaultRepoStrategiesFactory.commit;
import static com.github.stcarolas.enki.core.repo.DefaultRepoStrategiesFactory.directory;
import static com.github.stcarolas.enki.core.repo.DefaultRepoStrategiesFactory.identity;
import static com.github.stcarolas.enki.core.repo.DefaultRepoStrategiesFactory.name;
import static com.github.stcarolas.enki.core.repo.DefaultRepoStrategiesFactory.providers;
import static io.vavr.collection.List.empty;
import static io.vavr.collection.List.ofAll;
import static io.vavr.control.Option.some;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.cdancy.bitbucket.rest.BitbucketClient;
import com.cdancy.bitbucket.rest.domain.repository.Repository;
import com.cdancy.bitbucket.rest.domain.repository.RepositoryPage;
import com.github.stcarolas.enki.core.RepoProvider;

import io.vavr.collection.Seq;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

// TODO add http implementation for bitbucket
@Log4j2
@ToString
public abstract class BitbucketRepoProvider implements RepoProvider<BitbucketRepo> {

	private String endpoint;
    
	private String token;

	private BitbucketRepoQueryOptions options = BitbucketRepoQueryOptions.builder().build();

	@Override
	public Seq<BitbucketRepo> repositories() {
		return Try.of(this::listRepos)
			.onFailure(error -> log.error(error))
			.map(
				repos -> ofAll(repos)
						.flatMap(repo -> convert(repo))
			)
			.getOrElse(empty());
	}

	// TODO make code more functional
	private List<Repository> listRepos(){
			BitbucketClient client = BitbucketClient.builder()
				.endPoint(endpoint)
				.token(token)
				.build();

			List<Repository> repos = new ArrayList<>();
			boolean lastPage = false;
			int offset = 0;
			while (!lastPage) {
				RepositoryPage page = client.api()
					.repositoryApi()
					.listAll(
						options.getProjectname(),
						options.getName(),
						options.getPermission(),
						options.getVisibility(),
						offset,
						null
					);
				repos.addAll(page.values());
				offset = page.nextPageStart();
				lastPage = page.isLastPage();
			}
			return repos;
	}

	private Option<BitbucketRepo> convert(Repository repo) {
		var bitbucketRepo = new BitbucketRepo();
		bitbucketRepo
			.setNameStrategy(name(repo.name()))
			.setDirectoryStrategy(directory(bitbucketRepo))
			.setCommitStrategy(commit(bitbucketRepo))
			.setProvidersStrategy(providers(Arrays.asList(this)))
			.setIdentityStrategy(identity());
		repo.links()
			.clone()
			.stream()
			.forEach( href -> {
				if ("ssh".equals(href.get("name"))) {
					bitbucketRepo.setSshUrl(href.get("href"));
				}
			});
		return some(bitbucketRepo);
	}
}
