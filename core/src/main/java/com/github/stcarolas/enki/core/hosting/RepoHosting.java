package com.github.stcarolas.enki.core.hosting;

import static io.vavr.API.Option;
import java.util.function.Function;
import java.util.function.Supplier;
import com.github.stcarolas.enki.core.repo.remote.RemoteRepo;
import com.github.stcarolas.enki.core.repo.remote.RemoteRepoFactory;
import io.vavr.collection.Seq;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import static io.vavr.API.*;

@RequiredArgsConstructor
@Log4j2
public class RepoHosting {
	private final RemoteRepoFactory factory;
	private final Supplier<Try<Seq<String>>> list;

	public Try<Seq<RemoteRepo>> repositories() {
		return Try(() -> list.get())
			.flatMap(it -> it)
			.map(urls -> urls.map(url -> factory.from(url, this)));
	}
}
