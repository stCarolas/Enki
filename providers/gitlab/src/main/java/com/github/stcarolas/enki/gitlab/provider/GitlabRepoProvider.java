package com.github.stcarolas.enki.gitlab.provider;

import static io.vavr.API.Option;
import static io.vavr.collection.List.empty;
import static io.vavr.collection.List.ofAll;
import java.util.Arrays;
import com.github.stcarolas.enki.core.RepoProvider;
import com.github.stcarolas.enki.core.repo.remote.RemoteRepo;
import com.github.stcarolas.enki.core.repo.remote.RemoteRepoFactory;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.models.Project;
import io.vavr.collection.Seq;
import io.vavr.control.Try;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Builder
public class GitlabRepoProvider implements RepoProvider {
	private String endpoint;
	private String token;

	public Try<Seq<String>> get() {
		return Try.of(() -> new GitLabApi(endpoint, token).getProjectApi().getProjects())
			.map(repos -> ofAll(repos).map(repo -> repo.getHttpUrlToRepo()));
	}
}
