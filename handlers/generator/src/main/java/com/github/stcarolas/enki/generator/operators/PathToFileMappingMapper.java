package com.github.stcarolas.enki.generator.operators;

import java.nio.file.Path;
import java.util.function.Function;
import com.github.stcarolas.enki.generator.model.Mapping;

import lombok.Builder;
import lombok.extern.log4j.Log4j2;

@Builder
@Log4j2
public class PathToFileMappingMapper implements Function<Path, Mapping> {
	private final String source;
	private final String destination;

	@Override
	public Mapping apply(Path path) {
		log.info("create mapping, replacing {} with {}", source, destination);
		return Mapping.builder()
			.source(path.toString().replace(source + "/",""))
			.destination(path.toString().replace(source + "/", destination))
			.build();
	}
}
