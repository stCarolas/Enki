package com.github.stcarolas.enki.template;

import static io.vavr.API.Success;
import java.io.File;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.Function;
import com.github.stcarolas.enki.core.repo.local.LocalRepo;
import com.github.stcarolas.enki.core.repo.local.LocalRepoHandler;
import com.github.stcarolas.enki.generator.operators.LoadTemplate;
import com.github.stcarolas.gittemplateloader.GitTemplateLoader;
import io.vavr.collection.Seq;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.Builder;
import lombok.val;
import lombok.extern.log4j.Log4j2;

@Builder
@Log4j2
public class Generator implements LocalRepoHandler {
	private final String cloneUrl;
	private final Map<String, String> data;
	private final Map<String, String> mappings;
	private final boolean saveParameters;

	private final Function<Option<File>, Try<Seq<Path>>> listFiles = new ListFiles();
	private final Function<String, GitTemplateLoader> loadTemplate = new LoadTemplate();

	@Override
	public Try<LocalRepo> handle(LocalRepo repo) {
		Option<File> template = Option.ofOptional(
			loadTemplate.apply(cloneUrl).getDirectory()
		);

		//repo.apply(directory -> template.toTry().map( templateDirectory
		return Success(repo);
	}
}
