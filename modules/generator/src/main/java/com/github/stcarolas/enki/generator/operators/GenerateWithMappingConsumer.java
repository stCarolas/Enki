package com.github.stcarolas.enki.generator.operators;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.function.Consumer;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.stcarolas.enki.generator.model.Mapping;
import lombok.Builder;
import lombok.val;
import lombok.extern.log4j.Log4j2;

@Builder
@Log4j2
public class GenerateWithMappingConsumer implements Consumer<Mapping> {
    private final Handlebars handlebars;
    private final Map<String, String> data;

    @Override
    public void accept(Mapping mapping) {
        log.info("generate: {} to {}", mapping.getSource(), mapping.getDestination());
        try {
            Template template = handlebars.compile(mapping.getSource());
            try {
                new File(mapping.getSource()).toPath().getParent().toFile().mkdirs();
            } catch (Exception e) {}
            try (val out = new FileWriter(mapping.getDestination())) {
                template.apply(data, out);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
