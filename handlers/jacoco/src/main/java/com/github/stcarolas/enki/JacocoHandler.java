package com.github.stcarolas.enki;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import com.github.stcarolas.enki.core.Repo;
import com.github.stcarolas.enki.core.RepoHandler;
import org.yaml.snakeyaml.Yaml;
import io.vavr.control.Try;
import lombok.Builder;
import lombok.val;
import lombok.extern.log4j.Log4j2;

@Builder
@Log4j2
public class JacocoHandler implements RepoHandler {

	public void analyze(Repo repo) {
		repo.getDirectory().ifPresent(this::analyze);
	}

	public void analyze(File repo) {
		checkDir(repo).forEach(this::transform);
	}

	private void transform(Path pom) {
		Try.of(() -> (Map) (new Yaml()).load(new FileReader(pom.toFile())))
			.onSuccess(
				yaml -> {
					val plugins = Try.of(
						() -> {
							return (List) ((Map) yaml.get("build")).get("plugins");
						}
					)
						.getOrElse(Collections.EMPTY_LIST);
					log.info("loaded: {}", plugins);
					if (plugins != null && !plugins.isEmpty() && !hasConfiguration((List) plugins)) {
						log.info("need to add jacoco config");
						((List) plugins).add((new JacocoConfig().getConfig()));
						log.info("wriging config");
						try (FileWriter writer = new FileWriter(pom.toFile())) {
							log.info("target file: {}", pom.toAbsolutePath().toString());
							log.info("all yaml: {}", yaml);
							writer.write(
								new Yaml()
									.dump(yaml)
									.replace(
										"!!com.github.stcarolas.enki.MavenPluginExecution\n      ",
										""
									)
							);
						} catch (Exception e) {
							log.error("error: {}", e);
							e.printStackTrace();
						}
					}
				}
			);
	}

	private boolean hasConfiguration(List plugins) {
		return plugins !=null && plugins.stream()
			.filter(
				plugin -> "jacoco-maven-plugin"
					.equals(((Map<String, Object>) plugin).get("artifactId"))
			)
			.findAny()
			.isPresent();
	}

	public Stream<Path> checkDir(File dir) {
		return (Stream<Path>) Try.of(
			() -> {
				return Files.walk(dir.toPath())
					.filter(
						file -> {
							return !file.toString().contains("target");
						}
					)
					.filter(
						file -> {
							return file.getFileName().toString().startsWith("pom");
						}
					);
			}
		)
			.getOrElse(Stream.empty());
	}
}
