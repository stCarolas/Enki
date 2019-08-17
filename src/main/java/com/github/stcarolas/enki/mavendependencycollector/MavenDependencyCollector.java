package com.github.stcarolas.enki.mavendependencycollector;

import java.io.FileReader;
import java.util.Map;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.github.stcarolas.enki.Analyzer;
import com.github.stcarolas.enki.model.Repo;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class MavenDependencyCollector implements Analyzer {

    @Override
    public void analyze(Repo repo) {
        repo.getDirectory()
            .ifPresent(
                directory -> {
                    Try.of(
                        () -> {
                            return new FileReader(
                                directory.getAbsolutePath() + "/pom.yml"
                            );
                        }
                    )
                        .onFailure(
                            error -> System.out.println("Error: " + error)
                        )
                        .onSuccess(
                            fileReader -> {
                                Try.of(
                                    () -> (Map) new YamlReader(fileReader)
                                        .read()
                                )
                                    .onSuccess(
                                        pom -> {
                                            System.out.println(
                                                pom.get("dependencies")
                                            );
                                        }
                                    );
                            }
                        );
                }
            );
    }
}
