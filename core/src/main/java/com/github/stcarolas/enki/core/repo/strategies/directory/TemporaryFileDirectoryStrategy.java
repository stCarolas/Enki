package com.github.stcarolas.enki.core.repo.strategies.directory;

import java.io.File;
import java.util.function.Supplier;

import com.github.stcarolas.enki.core.Repo;
import com.github.stcarolas.enki.core.RepoProvider;

import static io.vavr.control.Option.some;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Log4j2
public class TemporaryFileDirectoryStrategy implements Supplier<Option<File>> {
	private static final String TEMPORARY_LOCATION = "/tmp/enki/";

	private final Option<? extends Repo> repo;

	@Override
	public Option<File> get() {
		return repo
			.onEmpty(() -> log.error("dont try to get directory for NULL repo"))
			.flatMap(it -> it.id().flatMap(this::constructDirectoryPath))

			.onEmpty(() -> log.error("dont use directory for repo without id"))
			.flatMap(this::createDirIfMissing)

			.onEmpty(() -> log.error("something is wrong, directory is missing"))
			.peek(dir -> this.load())

			.onEmpty(() -> log.error("fail to download repo"))
			;
	}

	private Option<File> constructDirectoryPath(String filename) {
		return some(filename)
			.map(it -> new File(TEMPORARY_LOCATION + it));
	}

	private Option<File> createDirIfMissing(File dir) {
		return Option.of(dir)
			.filter(File::exists)
			.peek( file -> log.info("directory {} was existed", file.getPath()) )
			.orElse(
				Try.of(dir::mkdir)
					.map(result -> dir)
					.onFailure( error -> log.error("error while creating directory {}: {}", dir, error) )
					.onSuccess( file -> log.info("directory {} was created", file.getPath()) )
					.toOption()
			);
	}

	private void load() {
		repo.map(it -> it.providers())
			.flatMap(it -> it.headOption())
			.onEmpty(() -> log.info("no providers to load from"))
			.peek(this::loadFrom);
	}

	@SuppressWarnings({ "unchecked" })
	private void loadFrom(RepoProvider<? extends Repo> provider) {
		repo.peek(it -> ((RepoProvider<Repo>) (RepoProvider<?>) provider).download(it));
	}

	public static Supplier<Option<File>> tmpStorage(Repo repo) {
		return new TemporaryFileDirectoryStrategy(Option.of(repo));
	}
}
