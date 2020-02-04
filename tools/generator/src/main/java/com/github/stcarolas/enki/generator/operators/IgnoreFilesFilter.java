package com.github.stcarolas.enki.generator.operators;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;

import lombok.Builder;
import lombok.val;

@Builder
public class IgnoreFilesFilter implements Predicate<Path> {
    private final List<String> ignoreList;

	@Override
	public boolean test(Path path) {
        for (val ignorePattern: ignoreList) {
            if (path.toString().contains(ignorePattern)) {
                return false;
            }
        }
        return true;
	}
    
}
