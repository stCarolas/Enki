package com.github.stcarolas.enki.logginganalyzers;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Data {
    private final String image;
    private final String group;
}
