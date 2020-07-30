package com.github.stcarolas.enki.template;

import java.io.File;
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
public class ListFiles implements Function<Option<File>, Try<Seq<Path>>> {

	@Override
	public Try<Seq<Path>> apply(Option<File> directory) {
		return directory.toTry()
			.mapTry(
				dir -> List.ofAll(Files.walk(dir.toPath()).filter(Files::isRegularFile))
			);
	}
}
