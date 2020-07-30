package com.github.stcarolas.enki.generator.operators;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;
import com.github.stcarolas.gittemplateloader.GitTemplateLoader;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;

@Builder
@Log4j2
public class TemplateLoaderToPathMapper
	implements Function<GitTemplateLoader, Try<Seq<Path>>> {

	@Override
	public Try<Seq<Path>> apply(GitTemplateLoader t) {
		return Option.ofOptional(t.getDirectory())
			.toTry()
			.mapTry(
				directory -> List.ofAll(
					Files.walk(directory.toPath()).filter(Files::isRegularFile)
				)
			);
	}
}
