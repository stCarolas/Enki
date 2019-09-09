package com.github.stcarolas.enki.generator.operators;

import java.util.function.Supplier;

import com.github.stcarolas.gittemplateloader.GitTemplateLoader;
import com.github.stcarolas.gittemplateloader.UrlType;

import lombok.Builder;

@Builder
public class TemplateLoaderSupplier implements Supplier<GitTemplateLoader> {
    public final String url;

	@Override
	public GitTemplateLoader get() {
        return GitTemplateLoader.builder()
            .url(url)
            .urlType(UrlType.SSH)
            .build();
	}
}
