package com.github.stcarolas.enki;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import com.github.stcarolas.enki.core.EnkiRunner;
import com.github.stcarolas.enki.core.RepoProvider;
import com.github.stcarolas.enki.gitlab.provider.GitlabRepoProvider;
import com.github.stcarolas.enki.shell.ShellRunner;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import static io.vavr.API.*;

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

	@Option(names = { "--gitlab-endpoint" }, description = "Gitlab REST API Endpoint")
	private String gitlabEndpoint = "";

	@Option(names = { "--gitlab-token" }, description = "Gitlab Access Token")
	private String gitlabToken = "";

	public static void main(String[] args) {
		new CommandLine(new EnkiLauncher()).execute(args);
	}

	@Override
	public Integer call() throws Exception {
		List<RepoProvider> providers = new ArrayList<>();

		EnkiRunner.Enki()
			.withProvider(
				GitlabRepoProvider.builder()
					.endpoint(gitlabEndpoint)
					.token(gitlabToken)
					.build()
			)
			.withLocalRepoHandler(new ShellRunner("touch /tmp/shellrunner"))
			.run();

		return 0;
	}
}
