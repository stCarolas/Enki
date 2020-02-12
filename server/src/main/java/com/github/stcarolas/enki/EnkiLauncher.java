package com.github.stcarolas.enki;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import com.github.stcarolas.enki.bitbucket.provider.BitbucketRepoProvider;
import com.github.stcarolas.enki.core.EnkiRunner;
import com.github.stcarolas.enki.core.Repo;
import com.github.stcarolas.enki.core.RepoHandler;
import com.github.stcarolas.enki.core.RepoProvider;
import com.github.stcarolas.enki.github.provider.GitHubRepoProvider;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(
	name = "enki-server",
	mixinStandardHelpOptions = true,
	version = "0.2",
	description = "run enki handlers"
)
public class EnkiLauncher implements Callable<Integer> {
	@Option(names = { "--server" }, description = "run in server mode")
	private boolean isServer = false;

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

	@Option(names = { "--bitbucket" }, description = "use Bitbucket RepoProvider")
	private boolean useBitbucketProvider = false;

	@Option(
		names = { "--bitbucket-endpoint" },
		description = "Bitbucket REST API Endpoint"
	)
	private String bitbucketEndpoint = "";

	@Option(names = { "--bitbucket-token" }, description = "Bitbucket Access Token")
	private String bitbucketToken = "";

	@Option(names = { "--gitlab" }, description = "use GitLab RepoProvider")
	private boolean useGitlabProvider = false;

	@Option(names = { "--gitea" }, description = "use Gitea RepoProvider")
	private boolean useGiteaProvider = false;

	@Option(names = { "--host" }, description = "host to listen for Enki Server")
	private String host = "0.0.0.0";

	@Option(names = { "--port" }, description = "port to listen for Enki Server")
	private int port = 8080;

	@Parameters(index = "0", description = "jar with handlers")
	private String jar;

	public static void main(String[] args) {
		new CommandLine(new EnkiLauncher()).execute(args);
	}

	@Override
	public Integer call() throws Exception {
		List<RepoHandler<Repo>> handlers = ClasspathScanner.scan(jar);
		List<RepoProvider<Repo>> providers = new ArrayList<>();

		if (useGithubProvider) {
			providers.add(
				GitHubRepoProvider.builder()
					.username(githubUser)
					.password(githubPassword)
					.organization(githubOrg)
					.build()
			);
		}

		if (useBitbucketProvider) {
			providers.add(
				BitbucketRepoProvider.builder()
					.token(bitbucketToken)
					.endpoint(bitbucketEndpoint)
					.build()
			);
		}

		if (isServer) {
			EnkiServer.builder()
				.providers(providers)
				.handlers(handlers)
				.port(port)
				.serverHost(host)
				.build()
				.start();
		} else {
			EnkiRunner.<Repo>builder()
				.providers(providers)
				.handlers(handlers)
				.build()
				.handle();
		}

		return 0;
	}
}
