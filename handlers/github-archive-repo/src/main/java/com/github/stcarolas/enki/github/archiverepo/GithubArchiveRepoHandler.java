package com.github.stcarolas.enki.github.archiverepo;

import java.util.HashMap;
import java.util.Map;
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
    public void handle(Repo repo) {
        Try.of(
            () -> {
                val repoService = new RepositoryService();
                repoService.getClient().setCredentials(username, password);
                log.info("Getting repo {} in org {}", repo.getName(), organization);
                return repoService.editRepository(
                    organization,
                    repo.getName(),
                    archiveRepoOptions()
                );
            }
        )
            .onFailure(error -> log.error(error));
    }

    public Map<String, Object> archiveRepoOptions() {
        val options = new HashMap<String, Object>();
        options.put("archived", true);
        return options;
    }
}
