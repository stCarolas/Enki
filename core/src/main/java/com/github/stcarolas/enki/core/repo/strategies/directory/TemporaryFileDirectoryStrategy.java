package com.github.stcarolas.enki.core.repo.strategies.directory;

import static com.github.stcarolas.enki.core.util.Lifting.call;
import static io.vavr.API.Option;

import java.io.File;
import java.util.function.Supplier;

import com.github.stcarolas.enki.core.Repo;

import io.vavr.Function1;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Log4j2
public class TemporaryFileDirectoryStrategy implements Supplier<File> {
	public static final String TEMPORARY_LOCATION = "/tmp/enki/";

	private final Option<? extends Repo> repo;

	protected Function1<String, File> constructDirectoryPath = filename -> new File(TEMPORARY_LOCATION + filename);

	protected Function1<File,File> createDirIfMissing = dir ->
			Option.of( dir )
				.filter( File::exists )
				.peek( file -> log.info("directory {} was existed", file.getPath()) )
				.getOrElse(
					() -> Try.of( dir::mkdir )
						.map( result -> dir )
						.onFailure( error -> log.error("error while creating directory {}: {}", dir, error) )
						.onSuccess( file -> log.info("directory {} was created", file.getPath()) )
						.getOrNull()
				);

	@Override
	public File get() {
		return repo
			.onEmpty(() -> log.error("dont try to get directory for NULL repo"))
			.flatMap(
				it -> call(it::id)
					.onEmpty(() -> log.info("cant get identity for {}", repo))
					.flatMap( dir -> call(() -> constructDirectoryPath.apply(dir)) )
					.onEmpty(() -> log.error("cant get path to store repo {}", repo))
			)
			.flatMap( dir -> call(() -> createDirIfMissing.apply(dir)) )
			.onEmpty(() -> log.error("something is wrong, directory for storage is missing"))
			.getOrNull();
	}

	public static Supplier<File> TemporaryDirectory(Repo repo) {
		return new TemporaryFileDirectoryStrategy(Option(repo));
	}
}
