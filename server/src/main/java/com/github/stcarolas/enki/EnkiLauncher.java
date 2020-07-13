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

	@Option(names = { "--host" }, description = "host to listen for Enki Server")
	private String host = "0.0.0.0";

	@Option(names = { "--port" }, description = "port to listen for Enki Server")
	private int port = 8080;

	@Option(names = { "--gitlab" }, description = "use GitLab RepoProvider")
	private boolean useGitlabProvider = false;

	@Option(
		names = { "--gitlab-endpoint" },
		description = "Gitlab REST API Endpoint"
	)
	private String gitlabEndpoint = "";

	@Option(names = { "--gitlab-token" }, description = "Gitlab Access Token")
	private String gitlabToken = "";

	public static void main(String[] args) {
		new CommandLine(new EnkiLauncher()).execute(args);
	}

	@Override
	public Integer call() throws Exception {
		//List<RepoHandler<Repo>> handlers = ClasspathScanner.scan(jar);
		List<RepoProvider<Repo>> providers = new ArrayList<>();
		List<RepoHandler<Repo>> handlers = List(new ShellRunner("touch /tmp/touched"));

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
