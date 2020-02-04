package com.github.stcarolas.enki.generator.operators;

import java.util.function.Supplier;

import com.github.stcarolas.gittemplateloader.GitTemplateLoader;
import com.github.stcarolas.gittemplateloader.UrlType;

import lombok.Builder;
import lombok.extern.log4j.Log4j2;

@Builder
@Log4j2
public class TemplateLoaderSupplier implements Supplier<GitTemplateLoader> {
    public final String url;

	@Override
	public GitTemplateLoader get() {
        log.info("create gitTemplateLoaderr");
        return GitTemplateLoader.builder()
            .url(url)
            .urlType(UrlType.SSH)
            .build();
	}
}
