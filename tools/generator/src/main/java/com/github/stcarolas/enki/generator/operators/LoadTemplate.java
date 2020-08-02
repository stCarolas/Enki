package com.github.stcarolas.enki.generator.operators;

import com.github.stcarolas.gittemplateloader.GitTemplateLoader;
import com.github.stcarolas.gittemplateloader.UrlType;

import io.vavr.Function1;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class LoadTemplate implements Function1<String,GitTemplateLoader> {

	@Override
	public GitTemplateLoader apply(String url) {
		log.info("create gitTemplateLoaderr");
		return GitTemplateLoader.builder().url(url).urlType(UrlType.SSH).build();
	}
}
