package com.github.stcarolas.enki.template;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import com.github.stcarolas.gittemplateloader.GitTemplateLoader;

import io.vavr.Function1;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import static io.vavr.API.*;

@Builder
@Log4j2
public class ListFiles implements Function1<File, Try<Seq<Path>>> {

	@Override
	public Try<Seq<Path>> apply(File directory) {
		Path rootPath = directory.toPath();
		return Try(
			() -> (Seq<Path>)List.ofAll(
				Files.walk(rootPath)
					.filter(Files::isRegularFile)
					.map(path -> rootPath.relativize(path))
					.filter(path -> !path.startsWith(".git"))
			)
		).onSuccess( list -> log.info("template files count:{}", list.size()));
	}
}
