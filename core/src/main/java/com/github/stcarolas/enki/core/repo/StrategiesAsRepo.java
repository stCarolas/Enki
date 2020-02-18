package com.github.stcarolas.enki.core.repo;

import static io.vavr.collection.List.empty;

import java.io.File;
import java.util.function.Function;
import java.util.function.Supplier;

import com.github.stcarolas.enki.core.Repo;
import com.github.stcarolas.enki.core.RepoProvider;

import io.vavr.collection.Seq;
import io.vavr.control.Option;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class StrategiesAsRepo implements Repo {

	private Option<Function<String, Option<? extends Repo>>> commitStrategy;
	private Option<Supplier<Option<File>>> directoryStrategy;
	private Option<Supplier<Option<String>>> identityStrategy;
	private Option<Supplier<Option<String>>> nameStrategy;
	private Option<Supplier<Seq<RepoProvider<? extends Repo>>>> providersStrategy;

	@Override
	public Option<String> id() {
		return identityStrategy
			.onEmpty(() -> log.info("no IdentityStrategy defined"))
			.flatMap(Supplier::get);
	}

	@Override
	public Option<String> name() {
		return nameStrategy
			.onEmpty(() -> log.info("no NameStrategy defined"))
			.flatMap(Supplier::get);
	}

	@Override
	public Option<File> directory() {
		return directoryStrategy
			.onEmpty(() -> log.info("no DirectoryStrategy defined"))
			.flatMap(Supplier::get);
	}

	@Override
	public Seq<RepoProvider<? extends Repo>> providers() {
		return providersStrategy
			.onEmpty(() -> log.info("no ProvidersStrategy defined"))
			.map(Supplier::get)
			.getOrElse(empty());
	}

	@Override
	public Option<? extends Repo> commit(String commitMessage) {
		return commitStrategy
			.onEmpty(() -> log.info("no CommitStrategy defined"))
			.flatMap( strategy -> strategy.apply(commitMessage) );
	}

	public StrategiesAsRepo setDirectoryStrategy(Supplier<Option<File>> strategy) {
		this.directoryStrategy = Option.of(strategy);
		return this;
	}

	public StrategiesAsRepo setProvidersStrategy(Supplier<Seq<RepoProvider<? extends Repo>>> strategy) {
		this.providersStrategy = Option.of(strategy);
		return this;
	}

	public StrategiesAsRepo setIdentityStrategy(Supplier<Option<String>> strategy) {
		this.identityStrategy = Option.of(strategy);
		return this;
	}

	public StrategiesAsRepo setNameStrategy(Supplier<Option<String>> strategy) {
		this.nameStrategy = Option.of(strategy);
		return this;
	}

	public StrategiesAsRepo setCommitStrategy(Function<String, Option<? extends Repo>> strategy) {
		this.commitStrategy = Option.of(strategy);
		return this;
	}
}
