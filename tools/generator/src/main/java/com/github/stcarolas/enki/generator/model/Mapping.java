package com.github.stcarolas.enki.generator.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Mapping {
	private final String source;
	private final String destination;
}
