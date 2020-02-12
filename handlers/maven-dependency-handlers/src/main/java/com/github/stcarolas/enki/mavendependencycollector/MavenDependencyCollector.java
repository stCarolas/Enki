package com.github.stcarolas.enki.mavendependencycollector;

import java.io.FileReader;
import java.util.Map;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.github.stcarolas.enki.core.Repo;
import com.github.stcarolas.enki.core.RepoHandler;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class MavenDependencyCollector implements RepoHandler {

	@Override
	public void handle(Repo repo) {
		repo.getDirectory()
			.ifPresent(
				directory -> {
					Try.of(
						() -> {
							return new FileReader(
								directory.getAbsolutePath() + "/service/pom.yml"
							);
						}
					)
						.onFailure(error -> System.out.println("Error: " + error))
						.onSuccess(
							fileReader -> {
								Try.of(() -> (Map) new YamlReader(fileReader).read())
									.onSuccess(
										pom -> {
											log.info("pom:{}", pom);
											log.info("deps:{}", pom.get("dependencies"));
										}
									);
							}
						);
				}
			);
	}
}
