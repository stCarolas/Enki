package com.github.stcarolas.enki;

import static com.github.stcarolas.enki.ProviderCache.cache;
import java.util.List;
import java.util.Objects;
import com.github.stcarolas.enki.core.Repo;
import com.github.stcarolas.enki.core.RepoHandler;
import com.github.stcarolas.enki.core.RepoProvider;
import io.undertow.Undertow;
import lombok.Builder;
import lombok.Singular;
import lombok.extern.log4j.Log4j2;

@Builder
@Log4j2
public class EnkiServer<T extends Repo> {

	@Singular
	private List<RepoProvider<T>> providers;

	@Singular
	private List<RepoHandler<T>> handlers;

	private String serverHost;
	private int port;

	public void start() {

		if (Objects.isNull(providers) || Objects.isNull(handlers)) {
			log.info("no providers or handlers, exiting");
			return;
		}

		RepoProvider<T> cachedProvider = cache(providers);
		Undertow.builder()
			.addHttpListener(port, serverHost)
			.setHandler(
				GithubWebhookHandler.<T>builder()
					.provider(cachedProvider)
					.handlers(handlers)
					.build()
			)
			.build()
			.start();

	}
}
