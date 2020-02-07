package com.github.stcarolas.enki.runner;

import java.util.List;
import java.util.concurrent.Callable;

import com.github.stcarolas.enki.bitbucket.provider.BitbucketRepoProvider;
import com.github.stcarolas.enki.core.EnkiRunner;
import com.github.stcarolas.enki.core.EnkiRunner.EnkiRunnerBuilder;
import com.github.stcarolas.enki.core.Repo;
import com.github.stcarolas.enki.core.RepoHandler;
import com.github.stcarolas.enki.github.provider.GitHubRepoProvider;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(
    name = "enki",
    mixinStandardHelpOptions = true,
    version = "0.2",
    description = "run enki handlers from cli"
)
public class ConsoleRunner implements Callable<Integer> {

    @Option(names = { "--github" }, description = "use GitHub RepoProvider")
    private boolean useGithubProvider = false;

    @Option(names = { "--github-username" }, description = "GitHub Username")
    private String githubUser = "";

    @Option(names = { "--github-organization" }, description = "GitHub Organization")
    private String githubOrg = "";

    @Option(
        names = { "--github-password" },
        description = "GitHub Password or Personal Access Token"
    )
    private String githubPassword = "";

    @Option(
        names = { "--bitbucket" }, 
        description = "use Bitbucket RepoProvider"
    )
    private boolean useBitbucketProvider = false;

    @Option(
        names = { "--bitbucket-endpoint" },
        description = "Bitbucket REST API Endpoint"
    )
    private String bitbucketEndpoint = "";

    @Option(
        names = { "--bitbucket-token" }, 
        description = "Bitbucket Access Token"
    )
    private String bitbucketToken = "";

    @Option(names = { "--gitlab" }, description = "use GitLab RepoProvider")
    private boolean useGitlabProvider = false;

    @Option(names = { "--gitea" }, description = "use Gitea RepoProvider")
    private boolean useGiteaProvider = false;

    @Parameters(index = "0", description = "jar with handlers")
    private String jar;

    public static void main(String[] args) {
        new CommandLine(new ConsoleRunner()).execute(args);
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Integer call() throws Exception {
        List<RepoHandler<Repo>> handlers = ClasspathScanner.scan(jar);
        EnkiRunnerBuilder enki = EnkiRunner.builder();
        if (useGithubProvider) {
            enki.provider(
                GitHubRepoProvider.builder()
                    .username(githubUser)
                    .password(githubPassword)
                    .organization(githubOrg)
                    .build()
            );
        }
        if (useBitbucketProvider) {
            enki.provider(
                BitbucketRepoProvider.builder()
                    .token(bitbucketToken)
                    .endpoint(bitbucketEndpoint)
                    .build()
            );
        }
        enki.handlers(handlers).build().handle();

        return 0;
    }
}
