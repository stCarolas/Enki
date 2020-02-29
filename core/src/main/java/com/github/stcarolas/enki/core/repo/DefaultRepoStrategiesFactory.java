package com.github.stcarolas.enki.core.repo;

import static com.github.stcarolas.enki.core.repo.strategies.commit.GitCommitStrategy.GitCommit;

import java.io.File;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import com.github.stcarolas.enki.core.Repo;
import com.github.stcarolas.enki.core.RepoProvider;
import com.github.stcarolas.enki.core.repo.strategies.directory.TemporaryFileDirectoryStrategy;
import com.github.stcarolas.enki.core.repo.strategies.identity.UUIDIdentityStrategy;
import com.github.stcarolas.enki.core.repo.strategies.name.ProvidedNameStrategy;
import com.github.stcarolas.enki.core.repo.strategies.provider.ProvidedProvidersStrategy;

import org.eclipse.jgit.revwalk.RevCommit;

import io.vavr.collection.Seq;
import io.vavr.control.Try;

public class DefaultRepoStrategiesFactory {

	public static Function<String, Try<RevCommit>> commit(Repo repo) {
		return GitCommit(repo::directory);
	}

	public static Supplier<File> directory(Repo repo) {
		return TemporaryFileDirectoryStrategy.tmpStorage(repo);
	}

	public static Supplier<String> identity() {
		return UUIDIdentityStrategy.uuidIdentity();
	}

	public static Supplier<String> name(String name) {
		return ProvidedNameStrategy.providedName(name);
	}

	public static Supplier<Seq<RepoProvider<? extends Repo>>> providers(
		List<RepoProvider<? extends Repo>> providers
	){
		return ProvidedProvidersStrategy.providers(providers);
	}

}
