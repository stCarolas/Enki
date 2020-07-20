package com.github.stcarolas.enki.generator.operators;

import java.util.function.Function;
import java.util.function.Supplier;
import com.github.stcarolas.gittemplateloader.GitTemplateLoader;
import com.github.stcarolas.gittemplateloader.UrlType;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

@NoArgsConstructor
@Log4j2
public class CreateTemplateLoader implements Function<String, GitTemplateLoader> {

	public GitTemplateLoader apply(String url) {
		log.info("create gitTemplateLoaderr");
		return GitTemplateLoader.builder().url(url).urlType(UrlType.SSH).build();
	}
}
