package com.github.stcarolas.enki.logginganalyzers;

import java.util.Map;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GenerationParameters {
    private final String cloneUrl;
    private final Map<String, String> data;
    private final Map<String, String> mapping;
}
