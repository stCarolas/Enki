package com.github.stcarolas.enki.template;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import static io.vavr.API.*;

import java.io.FileWriter;
import java.nio.file.Path;
import java.util.Map;

import com.github.jknack.handlebars.Handlebars;

import io.vavr.Function3;
import io.vavr.control.Try;

@Log4j2
public class ApplyTemplateFile implements Function3<Path,Path,Map<String,String>,Try<Void>> {
	private final Handlebars handlebars = new Handlebars();

	public Try<Void> apply(Path source, Path destination, Map<String, String> data) {
		log.info("apply template {} to {}",source,destination);
		return Try(() -> handlebars.compile(source.toString()))
			.onFailure(error -> log.error(error))
			.flatMap(
				template -> Try.withResources(
					() -> new FileWriter(destination.toString())
				)
					.of(
						writer -> {
							template.apply(data, writer);
							log.info("file writed");
							return null;
						}
					)
					.onFailure(error -> log.error(error))
					.map(it -> null)
			);
	}
}
