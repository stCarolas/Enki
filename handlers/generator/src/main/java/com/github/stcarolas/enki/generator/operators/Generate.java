package com.github.stcarolas.enki.generator.operators;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.inject.Inject;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.stcarolas.enki.generator.model.Mapping;
import io.vavr.CheckedRunnable;
import io.vavr.Function2;
import io.vavr.control.Try;
import lombok.Builder;
import lombok.val;
import lombok.extern.log4j.Log4j2;
import static io.vavr.API.*;

@Log4j2
public class Generate implements Function2<Map<String,String>,Mapping, Try<Void>> {
	private final Handlebars handlebars;

	public Generate(Handlebars handlebars){
		this.handlebars = handlebars;
	}

	@Override
	public Try<Void> apply(Map<String, String> data, Mapping mapping) {
		log.info("generate: {} to {}", mapping.getSource(), mapping.getDestination());
		return Try.run(createPath(mapping.getDestination()))
			.mapTry(it -> handlebars.compile(mapping.getSource()))
			.flatMap(
				template -> Try.withResources(
					() -> new FileWriter(mapping.getDestination())
				)
					.of(
						writer -> {
							template.apply(data, writer);
							return null;
						}
					)
			);
	}

	private CheckedRunnable createPath(String path) {
		return () -> new File(path)
			.toPath()
			.getParent()
			.toFile()
			.mkdirs();
	}
}
