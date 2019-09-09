package com.github.stcarolas.enki.generator;

import java.util.List;
import java.util.Map;

import com.github.stcarolas.enki.core.Repo;
import com.github.stcarolas.enki.core.RepoHandler;
import com.github.stcarolas.gittemplateloader.GitTemplateLoader;
import com.github.stcarolas.gittemplateloader.UrlType;

import lombok.Builder;
import lombok.val;

@Builder
public class GenerationHandler implements RepoHandler {
    private final String cloneUrl;
    private final Map<String, String> data;
    private final Map<String, String> mapping;
    private final List<String> ignoreFilesList;

    @Override
    public void analyze(Repo repo) {
        val templateLoader = GitTemplateLoader.builder()
            .url(cloneUrl)
            .urlType(UrlType.SSH)
            .build();
        templateLoader.getDirectory().ifPresent(dir -> {});
    }
}
