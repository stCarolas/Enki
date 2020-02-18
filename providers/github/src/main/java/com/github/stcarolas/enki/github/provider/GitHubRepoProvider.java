package com.github.stcarolas.enki.github.provider;

import static com.github.stcarolas.enki.core.repo.DefaultRepoStrategiesFactory.commit;
import static com.github.stcarolas.enki.core.repo.DefaultRepoStrategiesFactory.directory;
import static com.github.stcarolas.enki.core.repo.DefaultRepoStrategiesFactory.identity;
import static com.github.stcarolas.enki.core.repo.DefaultRepoStrategiesFactory.name;
import static io.vavr.collection.List.empty;
import static io.vavr.collection.List.ofAll;

import com.github.stcarolas.enki.core.Repo;
import com.github.stcarolas.enki.core.RepoProvider;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.service.RepositoryService;

import io.vavr.collection.Seq;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.Builder;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;


@Log4j2
@Builder
@ToString
public class GitHubRepoProvider implements RepoProvider<GitHubRepo> {
	private String username;
	private String password;
	private String organization;

	@Override
	public Seq<GitHubRepo> repositories() {
		return Try.of(
			() -> {
				var repoService = new RepositoryService();
				repoService.getClient().setCredentials(username, password);
				if (organization == null || organization.isEmpty()){
					return repoService.getRepositories();
				}
				return repoService.getOrgRepositories(organization);
			}
		)
			.onFailure(
				error -> {
					log.error(error);
				}
			)
			.map(
				repos -> ofAll(repos)
						.map(repo -> convert(repo))
			)
			.getOrElse(empty());
	}

	private GitHubRepo convert(Repository repo) {
		var githubRepo = new GitHubRepo();
		githubRepo
			.setNameStrategy(name(repo.getName()))
			.setDirectoryStrategy(directory(githubRepo))
			.setIdentityStrategy(identity())
			.setCommitStrategy(commit(githubRepo));
		return githubRepo;
	}

	@Override
	public Option<GitHubRepo> download(GitHubRepo repo) {
		return Option.none();
	}

	@Override
	public Option<GitHubRepo> upload(Repo repo) {
		return Option.none();
	}
}
