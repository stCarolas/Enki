package com.github.stcarolas.enki.logginganalyzers;

import java.io.File;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import com.github.jknack.handlebars.Handlebars;
import com.github.stcarolas.enki.core.repo.local.LocalRepo;
import com.github.stcarolas.enki.core.repo.local.LocalRepoHandler;
import com.github.stcarolas.enki.generator.operators.CreateTemplateLoader;
import com.github.stcarolas.enki.generator.operators.CustomMappingMapper;
import com.github.stcarolas.enki.generator.operators.IgnoreFilesFilter;
import com.github.stcarolas.enki.generator.operators.ParameterSaver;
import com.github.stcarolas.enki.generator.operators.PathToFileMappingMapper;
import com.github.stcarolas.enki.generator.operators.TemplateLoaderToPathMapper;
import com.github.stcarolas.gittemplateloader.GitTemplateLoader;
import io.vavr.Function2;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.Builder;
import lombok.val;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import static io.vavr.API.*;

@Builder
@Log4j2
public class Generator implements LocalRepoHandler {
	private final String cloneUrl;
	private final java.util.Map<String, String> data;
	private final java.util.Map<String, String> mappings;
	private final boolean saveParameters;
	private CreateTemplateLoader createTemplateLoader = new CreateTemplateLoader();

	private IgnoreFilesFilter ignoreFilePatterns = IgnoreFilesFilter.builder()
		.ignoreList(
			Arrays.asList(".git", "README", "README.md", "README.MD", "default.json")
		)
		.build();

	public Try<LocalRepo> handle(LocalRepo repo) {
		log.info("Start generation");
		val parameters = GenerationParameters.builder()
			.cloneUrl(cloneUrl)
			.data(data)
			.mapping(mappings)
			.build();

		GitTemplateLoader templateLoader = createTemplateLoader.apply(cloneUrl);
		Option<File> directory = Option.ofOptional(templateLoader.getDirectory());
		directory.map(generate.apply(repo));

		//val destination = destinationDir(repo);
		//for (val mappingKey: mappings.keySet()) {
		//val mapping = mappings.get(mappingKey);
		//if (!mappings.get(mappingKey).startsWith(destination)) {
		//mappings.put(mappingKey, destination + mapping);
		//}
		//}
		//}
		//val toPathes = TemplateLoaderToPathMapper.builder().build();
		//val toMapping = PathToFileMappingMapper.builder()
		//.source(dir.toString())
		//.destination(destination)
		//.build();
		//val generateByMapping = GenerateWithMappingConsumer.builder()
		//.data(data)
		//.handlebars(new Handlebars(templateLoader))
		//.build();
		//val overrideWithCustomMapping = CustomMappingMapper.builder()
		//.customMappings(mappings)
		//.build();
		//Flux.just(templateLoader)
		//.flatMap(toPathes)
		//.filter(ignoreFilePatterns)
		//.map(toMapping)
		//.map(overrideWithCustomMapping)
		//.doOnNext(generateByMapping)
		//.blockLast();
		//}
		//);
		//if (repo != null) {
		//repo.commitAndPush("Generated by Enki");
		return Success(repo);
	}

	private String destinationDir(LocalRepo repo) {
		if (repo == null) {
			return "";
		}
		return null;
	}

	private Function2<LocalRepo, File, Try<Void>> generate = (repo, directory) -> Success(
		null
	);
}