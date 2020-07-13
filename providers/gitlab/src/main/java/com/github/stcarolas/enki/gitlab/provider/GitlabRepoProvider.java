package com.github.stcarolas.enki.gitlab.provider;

import static com.github.stcarolas.enki.core.repo.DefaultRepoStrategiesFactory.commit;
import static com.github.stcarolas.enki.core.repo.DefaultRepoStrategiesFactory.directory;
import static com.github.stcarolas.enki.core.repo.DefaultRepoStrategiesFactory.identity;
import static com.github.stcarolas.enki.core.repo.DefaultRepoStrategiesFactory.name;
import static com.github.stcarolas.enki.core.repo.DefaultRepoStrategiesFactory.providers;
import static io.vavr.API.Option;
import static io.vavr.collection.List.empty;
import static io.vavr.collection.List.ofAll;

import java.util.Arrays;

import com.github.stcarolas.enki.core.Repo;
import com.github.stcarolas.enki.core.RepoProvider;
import com.github.stcarolas.enki.core.provider.DefaultRepoProviderStrategiesFactory;

import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.models.Project;

import io.vavr.collection.Seq;
import io.vavr.control.Try;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Builder
public class GitlabRepoProvider implements RepoProvider<GitlabRepo> {
	private String endpoint;
	private String token;

	@Override
	public Seq<GitlabRepo> repositories() {
		return Try.of(
			() -> {
				return new GitLabApi(endpoint, token).getProjectApi().getProjects();
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

	private GitlabRepo convert(Project repo) {
		var gitlabRepo = new GitlabRepo();
		gitlabRepo
			.setNameStrategy(name(repo.getName()))
			.setDirectoryStrategy(directory(gitlabRepo))
			.setIdentityStrategy(identity())
			.setCommitStrategy(commit(gitlabRepo))
			.setProvidersStrategy(providers(Arrays.asList(this)));
		gitlabRepo.setSshUrl(repo.getSshUrlToRepo());
		gitlabRepo.setHttpUrl(repo.getHttpUrlToRepo());
		gitlabRepo.setPath(repo.getPath());
		gitlabRepo.setNamespace(repo.getNamespace().getName());
		return gitlabRepo;
	}

	@Override
	public GitlabRepo download(GitlabRepo repo) {
		return Option(
				DefaultRepoProviderStrategiesFactory
					.gitHttpClone(repo, repo.getHttpUrl())
					.get()
			)
			.onEmpty(() -> log.error("download failed"))
			.map( success -> repo )
			.getOrNull();
	}

	@Override
	public GitlabRepo upload(Repo repo) {
		return null;
	}
}
