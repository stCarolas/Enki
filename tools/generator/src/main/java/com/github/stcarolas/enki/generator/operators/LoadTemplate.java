package com.github.stcarolas.enki.generator.operators;

import java.util.function.Function;
import java.util.function.Supplier;
import com.github.stcarolas.gittemplateloader.GitTemplateLoader;
import com.github.stcarolas.gittemplateloader.UrlType;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class LoadTemplate implements Function<String,GitTemplateLoader> {

	@Override
	public GitTemplateLoader apply(String url) {
		log.info("create gitTemplateLoaderr");
		return GitTemplateLoader.builder().url(url).urlType(UrlType.SSH).build();
	}
}
