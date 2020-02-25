package com.github.stcarolas.enki.core.repo.strategies.directory;

import static com.github.stcarolas.enki.core.util.Lifting.call;

import java.io.File;
import java.util.function.Supplier;

import com.github.stcarolas.enki.core.Repo;
import com.github.stcarolas.enki.core.RepoProvider;

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
			.flatMap(
				it -> call(it::id)
					.onEmpty(() -> log.info("cant get identity for {}", repo))
					.flatMap( dir -> call(() -> this.constructDirectoryPath(dir)) )
					.onEmpty(() -> log.error("cant get path to store repo {}", repo))
			)

			.flatMap( dir -> call(() -> this.createDirIfMissing(dir)) )
			.onEmpty(() -> log.error("something is wrong, directory for storage is missing"))

			.peek(dir -> this.load())
			.onEmpty(() -> log.error("fail to store repo"));
	}

	private File constructDirectoryPath(String filename) {
		return new File(TEMPORARY_LOCATION + filename);
	}

	private File createDirIfMissing(File dir) {
		return Option.of(dir)
			.filter(File::exists)
			.peek( file -> log.info("directory {} was existed", file.getPath()) )
			.orElse(
				Try.of(dir::mkdir)
					.map(result -> dir)
					.onFailure( error -> log.error("error while creating directory {}: {}", dir, error) )
					.onSuccess( file -> log.info("directory {} was created", file.getPath()) )
					.toOption()
			).get();
	}

	private void load() {
		repo.flatMap(it -> call(it::providers)
				.onEmpty(() -> log.error("repo cant give us his repo provider")))
			.flatMap(it -> it.headOption())
			.onEmpty(() -> log.info("no providers to load from"))
			.map(this::loadFrom)
			.onEmpty(() -> log.error("cant load repo"));
	}

	@SuppressWarnings({ "unchecked" })
	private Option<? extends Repo> loadFrom(RepoProvider<? extends Repo> provider) {
		return repo.peek(it -> ((RepoProvider<Repo>) (RepoProvider<?>) provider).download(it));
	}

	public static Supplier<Option<File>> tmpStorage(Repo repo) {
		return new TemporaryFileDirectoryStrategy(Option.of(repo));
	}
}
