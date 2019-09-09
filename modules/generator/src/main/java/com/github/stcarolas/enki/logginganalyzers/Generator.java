package com.github.stcarolas.enki.logginganalyzers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.TemplateLoader;
import com.github.stcarolas.enki.core.Repo;
import com.github.stcarolas.enki.core.RepoHandler;
import com.github.stcarolas.enki.generator.operators.TemplateLoaderSupplier;
import com.github.stcarolas.enki.generator.operators.TemplateLoaderToPathMapper;
import lombok.Builder;
import lombok.val;
import reactor.core.publisher.Mono;

@Builder
public class Generator implements RepoHandler {
    private final String cloneUrl;
    private final Map<String, String> data;
    private final Map<String, String> mapping;
    private final List<String> ignoreFilesList;

    @Override
    public void analyze(Repo repo) {
        val loaderSupplier = TemplateLoaderSupplier.builder().url(cloneUrl).build();
        val toPathMapper = TemplateLoaderToPathMapper.builder().build();
        Object toFileMappingsMapper;
        Mono.fromSupplier(loaderSupplier).map(toPathMapper);
    }

    private void generate(
        TemplateLoader templateLoader,
        Map<String, String> preparedFileMappings,
        List<String> ignoreFilesList
    ) {
        val handlebars = new Handlebars(templateLoader);
        try {
            for (val pair : mapping.entrySet()) {
                Template template = handlebars.compile(pair.getKey());
                try {
                    new File(pair.getValue()).toPath().getParent().toFile().mkdirs();
                } catch (Exception e) {}
                try (val out = new FileWriter(pair.getValue())) {
                    template.apply(data, out);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
