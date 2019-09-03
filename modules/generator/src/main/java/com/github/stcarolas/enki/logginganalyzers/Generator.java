package com.github.stcarolas.enki.logginganalyzers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.stcarolas.enki.core.Repo;
import com.github.stcarolas.enki.core.RepoHandler;
import com.github.stcarolas.gittemplateloader.GitTemplateLoader;
import com.github.stcarolas.gittemplateloader.UrlType;

import lombok.Builder;
import lombok.val;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Builder
public class Generator implements RepoHandler {
    private final String cloneUrl;
    private final Map<String, String> data;
    private final Map<String, String> mapping;

    @Override
    public void analyze(Repo repo) {
        val handlebars = new Handlebars(
            GitTemplateLoader.builder()
                .url(cloneUrl)
                .urlType(UrlType.SSH)
                .build()
        );
        try {
            for (val pair : mapping.entrySet()) {
                Template template = handlebars.compile(pair.getKey());
                new File(pair.getValue()).toPath().getParent().toFile().mkdirs();
                try (val out = new FileWriter(pair.getValue())) {
                    template.apply(data, out);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
