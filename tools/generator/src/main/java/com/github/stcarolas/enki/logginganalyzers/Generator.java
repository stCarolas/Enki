package com.github.stcarolas.enki.logginganalyzers;

import java.util.Arrays;
import java.util.Map;

import com.github.jknack.handlebars.Handlebars;
import com.github.stcarolas.enki.core.Repo;
import com.github.stcarolas.enki.core.RepoHandler;
import com.github.stcarolas.enki.generator.operators.CustomMappingMapper;
import com.github.stcarolas.enki.generator.operators.GenerateWithMappingConsumer;
import com.github.stcarolas.enki.generator.operators.IgnoreFilesFilter;
import com.github.stcarolas.enki.generator.operators.ParameterSaver;
import com.github.stcarolas.enki.generator.operators.PathToFileMappingMapper;
import com.github.stcarolas.enki.generator.operators.TemplateLoaderSupplier;
import com.github.stcarolas.enki.generator.operators.TemplateLoaderToPathMapper;

import lombok.Builder;
import lombok.val;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;

@Builder
@Log4j2
public class Generator implements RepoHandler {
	private final String cloneUrl;
	private final Map<String, String> data;
	private final Map<String, String> mappings;
	private final boolean saveParameters;

	@Override
	public void analyze(Repo repo) {
		log.info("Start generation");
		val parameters = GenerationParameters.builder().cloneUrl(cloneUrl).data(data).mapping(mappings).build();
		val templateLoader = TemplateLoaderSupplier.builder().url(cloneUrl).build().get();
		templateLoader.getDirectory()
			.ifPresent(
				dir -> {
					val destination = destinationDir(repo);
					if (saveParameters){
						val saver = new ParameterSaver();
						saver.save(destination, parameters);
					}
					if (!destination.isEmpty()){
						for (val mappingKey: mappings.keySet()) {
							val mapping = mappings.get(mappingKey);
							if (!mappings.get(mappingKey).startsWith(destination)) {
								mappings.put(mappingKey, destination + mapping);
							}
						}
					}
					val toPathes = TemplateLoaderToPathMapper.builder().build();
					val toMapping = PathToFileMappingMapper.builder()
						.source(dir.toString())
						.destination(destination)
						.build();
					val generateByMapping = GenerateWithMappingConsumer.builder()
						.data(data)
						.handlebars(new Handlebars(templateLoader))
						.build();
					val ignoreFilePatterns = IgnoreFilesFilter.builder()
						.ignoreList(
							Arrays.asList(".git", "README", "README.md", "README.MD", "default.json")
						)
						.build();
					val overrideWithCustomMapping = CustomMappingMapper.builder()
						.customMappings(mappings)
						.build();
					Flux.just(templateLoader)
						.flatMap(toPathes)
						.filter(ignoreFilePatterns)
						.map(toMapping)
						.map(overrideWithCustomMapping)
						.doOnNext(generateByMapping)
						.blockLast();
				}
			);
			if (repo != null) {
				repo.commitAndPush("Generated by Enki");
			}
	}

	private String destinationDir(Repo repo) {
		if (repo == null) {
			return "";
		}
		val clonedDir = repo.getDirectory();
		return clonedDir.isPresent() ? clonedDir.get().toString() + "/" : "";
	}
}
