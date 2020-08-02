package com.github.stcarolas.enki.template;

import static io.vavr.API.Success;
import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import com.github.stcarolas.enki.core.repo.local.LocalRepo;
import com.github.stcarolas.enki.core.repo.local.LocalRepoHandler;
import com.github.stcarolas.enki.generator.operators.LoadTemplate;
import com.github.stcarolas.gittemplateloader.GitTemplateLoader;
import io.vavr.Function1;
import io.vavr.Function3;
import io.vavr.collection.Seq;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.extern.log4j.Log4j2;
import static io.vavr.API.*;

@Log4j2
@RequiredArgsConstructor
public class Generator implements LocalRepoHandler {
	private final String cloneUrl;

	private Function1<File, Try<Seq<Path>>> listFiles = new ListFiles();
	private Function1<String, GitTemplateLoader> loadTemplate = new LoadTemplate();
	private Function3<Path, Path, Map<String, String>, Try<Void>> applyTemplateFile = new ApplyTemplateFile();

	@Override
	public Try<LocalRepo> handle(LocalRepo repo) {
		Try<File> templateRepo = Option.ofOptional(
			loadTemplate.apply(cloneUrl).getDirectory()
		)
			.toTry();
		Try<Seq<Path>> templates = templateRepo.flatMap(listFiles);
		return repo.apply(
			destination -> For(templateRepo.map(file -> file.toPath()), templates)
				.yield(Function(this::generate).apply(destination.toPath()))
				.map(result -> null)
		);
	}

	private Seq<Object> generate(Path destination, Path root, Seq<Path> templates) {
		log.info("call generate");
		return templates.map(
			template -> applyTemplateFile.apply(
				root.resolve(template).toAbsolutePath(),
				destination.resolve(template).toAbsolutePath(),
				new HashMap()
			)
		);
	}
}
