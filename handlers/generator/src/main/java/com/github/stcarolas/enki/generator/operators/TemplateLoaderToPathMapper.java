package com.github.stcarolas.enki.generator.operators;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;
import com.github.stcarolas.gittemplateloader.GitTemplateLoader;
import io.vavr.control.Try;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;

@Builder
@Log4j2
public class TemplateLoaderToPathMapper
	implements Function<GitTemplateLoader, Flux<Path>> {

	@Override
	public Flux<Path> apply(GitTemplateLoader t) {
		log.info("get flux of path from templateLoader");
		return t.getDirectory()
			.map(
				path -> {
					return Try.of(
						() -> {
							return Flux.fromStream(
								Files.walk(path.toPath()).filter(Files::isRegularFile)
							);
						}
					)
						.getOrElse(Flux.empty());
				}
			)
			.orElse(Flux.empty());
	}
}
