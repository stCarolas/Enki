package com.github.stcarolas.enki;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import com.github.stcarolas.enki.core.EnkiRunner;
import com.github.stcarolas.enki.core.RepoProvider;
import com.github.stcarolas.enki.gitlab.provider.GitlabRepoProvider;
import com.github.stcarolas.enki.shell.ShellRunner;
import com.github.stcarolas.enki.shell.UrlRegexpFilter;
import com.github.stcarolas.enki.template.Generator;

import lombok.extern.log4j.Log4j2;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import static io.vavr.API.*;

@Command(name = "enki", mixinStandardHelpOptions = true, version = "0.2")
@Log4j2
public class EnkiLauncher implements Callable<Integer> {
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
			.withRemoteRepoFilter(new UrlRegexpFilter("efgi-accounting-object.git"))
			.withLocalRepoHandler(new Generator("git@github.com:stCarolas/enriched-beans.git"))
			.run()
			.onFailure(error -> log.error(error))
			.map(result -> result.map(repo -> repo.onFailure(error -> log.error(error))));

		return 0;
	}
}
