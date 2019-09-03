package com.github.stcarolas.enki.logginganalyzers;

import java.util.HashMap;

import org.junit.jupiter.api.Test;

import lombok.val;

public class GeneratorTest {

    @Test
    public void testGenerator() {
        val mapping = new HashMap<String, String>();
        mapping.put("image.gocd.yaml","/tmp/test3");
        val data = new HashMap<String, String>();
        data.put("image","imageName");
        data.put("group","groupName");
        Generator.builder()
            .cloneUrl("ssh://git@git.service.consul:2222/Mango/template-packer-pipeline.git")
            .mapping(mapping)
            .data(data)
            .build()
            .analyze(null);
    }
}
