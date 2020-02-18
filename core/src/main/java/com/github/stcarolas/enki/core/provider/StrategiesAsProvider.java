package com.github.stcarolas.enki.core.provider;

import static io.vavr.collection.List.empty;

import java.util.function.Function;
import java.util.function.Supplier;

import com.github.stcarolas.enki.core.Repo;
import com.github.stcarolas.enki.core.RepoProvider;

import io.vavr.collection.Seq;
import io.vavr.control.Option;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class StrategiesAsProvider<T extends Repo> implements RepoProvider<T> {
	private Option<Supplier<Seq<T>>> repositoriesStrategy;
	private Option<Function<T, Option<T>>> downloadStrategy;
	private Option<Function<Repo, Option<T>>> uploadStrategy;

	@Override
	public Seq<T> repositories() {
		return repositoriesStrategy
			.onEmpty(() -> log.info("no RepositoriesStrategy provided"))
			.map(Supplier::get)
			.onEmpty(() -> log.info("no repositories provided by strategy"))
			.getOrElse(empty());
	}

	@Override
	public Option<T> download(T repo) {
		return downloadStrategy
			.onEmpty(() -> log.info("no DownloadStrategy provided"))
			.iterator()
			.zip(
				Option.of(repo)
					.onEmpty(() -> log.error("NULL repo was given to download"))
			)
			.flatMap( 
				tuple -> tuple._1
					.apply(tuple._2)
					.onEmpty(() -> log.error("unknown error while downloading repo {}", tuple._2))
			)
			.singleOption();
	}

	@Override
	public Option<T> upload(Repo repo) {
		return uploadStrategy
			.onEmpty(() -> log.info("no UploadStrategy provided"))
			.iterator()
			.zip(
				Option.of(repo)
					.onEmpty(() -> log.error("NULL repo was given to upload"))
			)
			.flatMap( 
				tuple -> tuple._1
					.apply(tuple._2)
					.onEmpty(() -> log.error("unknown error while uploading repo {}", tuple._2))
			)
			.singleOption();
	}
}
