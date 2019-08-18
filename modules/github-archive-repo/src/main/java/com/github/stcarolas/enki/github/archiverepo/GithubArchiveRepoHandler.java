package com.github.stcarolas.enki.github.archiverepo;

import com.github.stcarolas.enki.core.Repo;
import com.github.stcarolas.enki.core.RepoHandler;
import org.eclipse.egit.github.core.service.RepositoryService;
import io.vavr.control.Try;
import lombok.Builder;
import lombok.val;
import lombok.extern.log4j.Log4j2;

@Builder
@Log4j2
public class GithubArchiveRepoHandler implements RepoHandler {
    private final String username;
    private final String password;
    private final String organization;

    @Override
    public void analyze(Repo repo) {
        return Try.of(
            () -> {
                val repoService = new RepositoryService();
                repoService.getClient().setCredentials(username, password);
                log.info("Getting repos for org:{}", organization);
                repoService.editRepository(organization, repo.getName());
            }
        );
    }
}
