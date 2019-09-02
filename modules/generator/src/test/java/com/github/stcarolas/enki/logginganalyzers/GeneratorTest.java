package com.github.stcarolas.enki.logginganalyzers;

import org.junit.jupiter.api.Test;

public class GeneratorTest {

    @Test
    public void testGenerator() {
        Generator.builder()
            .cloneUrl("ssh://git@git.service.consul:2222/Mango/template-packer-pipeline.git")
            .build()
            .analyze(null);
    }
}
