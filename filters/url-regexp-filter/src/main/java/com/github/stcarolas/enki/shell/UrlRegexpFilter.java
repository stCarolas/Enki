package com.github.stcarolas.enki.shell;

import java.io.File;
import java.util.function.Function;
import com.github.stcarolas.enki.core.repo.local.LocalRepo;
import com.github.stcarolas.enki.core.repo.local.LocalRepoHandler;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import static io.vavr.API.*;

@Log4j2
@RequiredArgsConstructor
public class UrlRegexpFilter implements Function<String, Boolean>{
	private final String regexp;

	public Boolean apply(String url) {
		return url.contains(regexp);
	}

}
