package com.github.stcarolas.enki.generator.operators;

import java.util.Map;
import java.util.function.UnaryOperator;
import com.github.stcarolas.enki.generator.model.Mapping;
import lombok.Builder;

@Builder
public class CustomMappingMapper implements UnaryOperator<Mapping> {
	private final Map<String, String> customMappings;

	@Override
	public Mapping apply(Mapping mapping) {
		if (customMappings.containsKey(mapping.getSource())) {
			return Mapping.builder()
				.source(mapping.getSource())
				.destination(customMappings.get(mapping.getSource()))
				.build();
		}
		return mapping;
	}
}
