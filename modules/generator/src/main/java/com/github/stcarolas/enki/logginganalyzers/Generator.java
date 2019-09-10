package com.github.stcarolas.enki.logginganalyzers;

import java.io.File;
import java.util.Arrays;
import java.util.Map;
import com.github.jknack.handlebars.Handlebars;
import com.github.stcarolas.enki.core.Repo;
import com.github.stcarolas.enki.core.RepoHandler;
import com.github.stcarolas.enki.generator.operators.CustomMappingMapper;
import com.github.stcarolas.enki.generator.operators.GenerateWithMappingConsumer;
import com.github.stcarolas.enki.generator.operators.IgnoreFilesFilter;
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
    private final Map<String, String> mapping;

    @Override
    public void analyze(Repo repo) {
        log.info("Start generation");
        val templateLoader = TemplateLoaderSupplier.builder().url(cloneUrl).build().get();
        templateLoader.getDirectory()
            .ifPresent(
                dir -> {
                    val toPathes = TemplateLoaderToPathMapper.builder().build();
                    val toMapping = PathToFileMappingMapper.builder()
                        .source(dir.toString())
                        .destination("")
                        .build();
                    val generateByMapping = GenerateWithMappingConsumer.builder()
                        .data(data)
                        .handlebars(new Handlebars(templateLoader))
                        .build();
                    val ignoreFilePatterns = IgnoreFilesFilter.builder()
                        .ignoreList(Arrays.asList(".git"))
                        .build();
                    val overrideWithCustomMapping = CustomMappingMapper.builder()
                        .customMappings(mapping)
                        .build();
                    Flux.just(templateLoader)
                        .flatMap(toPathes)
                        .filter(ignoreFilePatterns)
                        .map(toMapping)
                        .map(overrideWithCustomMapping)
                        .doOnNext(generateByMapping)
                        .subscribe();
                }
            );
    }
}
