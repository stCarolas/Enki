package com.github.stcarolas.enki.generator.operators;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.github.stcarolas.gittemplateloader.GitTemplateLoader;

import io.vavr.control.Try;
import lombok.Builder;

@Builder
public class TemplateLoaderToPathMapper
    implements Function<GitTemplateLoader, List<Path>> {

    @Override
    public List<Path> apply(GitTemplateLoader t) {
        return t.getDirectory()
            .map(
                path -> {
                    return Try.of(
                        () -> {
                            return Files.walk(path.toPath())
                                .filter(Files::isRegularFile)
                                .collect(Collectors.toList());
                        }
                    )
                        .getOrElse(new ArrayList<Path>());
                }
            )
            .orElse(new ArrayList<Path>());
    }

}
