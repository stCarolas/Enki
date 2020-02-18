package com.github.stcarolas.enki.bitbucket.provider;

import static com.github.stcarolas.enki.core.repo.DefaultRepoStrategiesFactory.commit;
import static com.github.stcarolas.enki.core.repo.DefaultRepoStrategiesFactory.directory;
import static com.github.stcarolas.enki.core.repo.DefaultRepoStrategiesFactory.identity;
import static com.github.stcarolas.enki.core.repo.DefaultRepoStrategiesFactory.name;
import static com.github.stcarolas.enki.core.repo.DefaultRepoStrategiesFactory.providers;
import static io.vavr.collection.List.empty;
import static io.vavr.collection.List.ofAll;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.cdancy.bitbucket.rest.BitbucketClient;
import com.cdancy.bitbucket.rest.domain.repository.Repository;
import com.cdancy.bitbucket.rest.domain.repository.RepositoryPage;
import com.github.stcarolas.enki.core.Repo;
import com.github.stcarolas.enki.core.RepoProvider;

import io.vavr.collection.Seq;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.Builder;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Builder
@ToString
public class BitbucketRepoProvider implements RepoProvider<BitbucketRepo> {

	private String endpoint;
    
	private String token;

	@Builder.Default
	private BitbucketRepoQueryOptions options = BitbucketRepoQueryOptions.builder().build();

	@Override
	public Option<BitbucketRepo> download(BitbucketRepo repo) {
		return Option.none();
	}

	@Override
	public Option<BitbucketRepo> upload(Repo repo) {
		return Option.none();
	}

	@Override
	public Seq<BitbucketRepo> repositories() {
		return Try.of(this::listRepos)
			.onFailure( error -> log.error(error) )
			.map(
				repos -> ofAll(repos)
						.flatMap(repo -> convert(repo))
			)
			.getOrElse(empty());
	}

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
		return Option.ofOptional(repo.links()
			.clone()
			.stream()
			.filter(href -> "ssh".equals(href.get("name")))
			.map(
				sshUrlHolder -> {
					var bitbucketRepo = new BitbucketRepo();
					bitbucketRepo
						.setNameStrategy(name(repo.name()))
						.setDirectoryStrategy(directory(bitbucketRepo))
						.setCommitStrategy(commit(bitbucketRepo))
						.setProvidersStrategy(providers(Arrays.asList(this)))
						.setIdentityStrategy(identity());
					return bitbucketRepo;
				}
			)
			.findFirst());
	}
}
