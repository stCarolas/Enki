package com.github.stcarolas.enki.core.repo;

import static com.github.stcarolas.enki.core.util.Lifting.call;
import static io.vavr.control.Option.none;
import static io.vavr.control.Option.of;

import java.io.File;
import java.util.function.Function;
import java.util.function.Supplier;

import com.github.stcarolas.enki.core.Repo;
import com.github.stcarolas.enki.core.RepoProvider;

import io.vavr.collection.Seq;
import io.vavr.control.Option;
import lombok.extern.log4j.Log4j2;

// TODO javadoc for StrategiesAsRepo
@Log4j2
public class StrategiesAsRepo implements Repo {

	private Option<Function<String, Option<? extends Repo>>> commitStrategy;
	private Option<Supplier<Option<File>>> directoryStrategy;
	private Option<Supplier<Option<String>>> identityStrategy;
	private Option<Supplier<Option<String>>> nameStrategy;
	private Option<Supplier<Seq<RepoProvider<? extends Repo>>>> providersStrategy;

	@Override
	public String id() {
		return identityStrategy
			.onEmpty(() -> log.info("no IdentityStrategy defined"))
			.flatMap(
				strategy -> call(strategy::get)
					.onEmpty(() -> log.error("cant call identity strategy"))
					.get()
			)
			.get();
	}

	@Override
	public String name() {
		return nameStrategy
			.onEmpty(() -> log.info("no NameStrategy defined"))
			.flatMap(strategy -> call(strategy::get).getOrElse(none()))
			.get();
	}

	@Override
	public File directory() {
		return directoryStrategy
			.onEmpty(() -> log.info("no DirectoryStrategy defined"))
			.flatMap(
				strategy -> call(strategy::get)
					.onEmpty(() -> log.error("cant call directory strategy"))
					.getOrElse(none())
			)
			.get();
	}

	@Override
	public Seq<RepoProvider<? extends Repo>> providers() {
		return providersStrategy
			.onEmpty(() -> log.info("no ProvidersStrategy defined"))
			.flatMap(
				strategy -> call(strategy::get)
					.onEmpty(() -> log.error("cant call providers strategy"))
			)
			.get();
	}

	@Override
	public Repo commit(String commitMessage) {
		return commitStrategy
			.onEmpty(() -> log.info("no CommitStrategy defined"))
			.flatMap(
				strategy -> call(() -> strategy.apply(commitMessage))
					.onEmpty(() -> log.error("cant call commit strategy"))
					.getOrElse(none())
			)
			.get();
	}

	public StrategiesAsRepo setDirectoryStrategy(Supplier<Option<File>> strategy) {
		this.directoryStrategy = of(strategy);
		return this;
	}

	// TODO set StrategiesAsRepo immutable
	public StrategiesAsRepo setProvidersStrategy(Supplier<Seq<RepoProvider<? extends Repo>>> strategy) {
		this.providersStrategy = of(strategy);
		return this;
	}

	public StrategiesAsRepo setIdentityStrategy(Supplier<Option<String>> strategy) {
		this.identityStrategy = of(strategy);
		return this;
	}

	public StrategiesAsRepo setNameStrategy(Supplier<Option<String>> strategy) {
		this.nameStrategy = of(strategy);
		return this;
	}

	public StrategiesAsRepo setCommitStrategy(Function<String, Option<? extends Repo>> strategy) {
		this.commitStrategy = of(strategy);
		return this;
	}

	@Override
	public String toString(){
		return describe();
	}
}
