package com.github.stcarolas.enki.core.repo;

import static com.github.stcarolas.enki.core.util.Lifting.call;
import static io.vavr.API.*;

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

	private Option<Function<String, ? extends Repo>> commitStrategy = None();
	private Option<Supplier<File>> directoryStrategy = None();
	private Option<Supplier<String>> identityStrategy = None();
	private Option<Supplier<String>> nameStrategy = None();
	private Option<Supplier<Seq<RepoProvider<? extends Repo>>>> providersStrategy = None();

	@Override
	public String id() {
		return identityStrategy
			.onEmpty(() -> log.info("no IdentityStrategy defined"))
			.flatMap(
				strategy -> call(strategy::get)
					.onEmpty(() -> log.error("cant call identity strategy"))
			)
			.getOrNull();
	}

	@Override
	public String name() {
		return nameStrategy
			.onEmpty(() -> log.info("no NameStrategy defined"))
			.flatMap(strategy -> call(strategy::get)
					.onEmpty(() -> log.error("cant call name strategy"))
			)
			.getOrNull();
	}

	@Override
	public File directory() {
		return directoryStrategy
			.onEmpty(() -> log.info("no DirectoryStrategy defined"))
			.flatMap(
				strategy -> call(strategy::get)
					.onEmpty(() -> log.error("cant call directory strategy"))
			)
			.getOrNull();
	}

	@Override
	public Seq<RepoProvider<? extends Repo>> providers() {
		return providersStrategy
			.onEmpty(() -> log.info("no ProvidersStrategy defined"))
			.flatMap(
				strategy -> call(strategy::get)
					.onEmpty(() -> log.error("cant call providers strategy"))
			)
			.getOrElse(Seq());
	}

	@Override
	public Repo commit(String commitMessage) {
		return commitStrategy
			.onEmpty(() -> log.info("no CommitStrategy defined"))
			.flatMap(
				strategy -> call(() -> strategy.apply(commitMessage))
					.onEmpty(() -> log.error("cant call commit strategy"))
			)
			.getOrNull();
	}

	public StrategiesAsRepo setDirectoryStrategy(Supplier<File> strategy) {
		this.directoryStrategy = Option(strategy);
		return this;
	}

	// TODO set StrategiesAsRepo immutable
	public StrategiesAsRepo setProvidersStrategy(
		Supplier<Seq<RepoProvider<? extends Repo>>> strategy
	){
		this.providersStrategy = Option(strategy);
		return this;
	}

	public StrategiesAsRepo setIdentityStrategy(Supplier<String> strategy) {
		this.identityStrategy = Option(strategy);
		return this;
	}

	public StrategiesAsRepo setNameStrategy(Supplier<String> strategy) {
		this.nameStrategy = Option(strategy);
		return this;
	}

	public StrategiesAsRepo setCommitStrategy(Function<String, ? extends Repo> strategy) {
		this.commitStrategy = Option(strategy);
		return this;
	}

	@Override
	public String toString(){
		return describe();
	}
}
