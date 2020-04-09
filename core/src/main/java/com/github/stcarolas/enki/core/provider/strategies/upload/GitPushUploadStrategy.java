package com.github.stcarolas.enki.core.provider.strategies.upload;

import static com.github.stcarolas.enki.core.util.Lifting.call;
import static io.vavr.collection.List.empty;

import java.io.File;
import java.util.function.Supplier;
import com.github.stcarolas.enki.core.Repo;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.PushResult;

import io.vavr.Function1;
import io.vavr.control.Option;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import static io.vavr.API.*;

@Log4j2
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class GitPushUploadStrategy<T extends Repo> implements Supplier<Iterable<PushResult>> {

	private final Option<T> repository;
	private final Supplier<CredentialsProvider> credentials;
	private final Function1<File, Option<Git>> gitOpenFn;
	private final Function1<Git, Option<Iterable<PushResult>>> gitPushFn;
	private final Function1<Repo, Option<File>> repoDirectoryFn;

	// todo test for defaultGitOpenFn in GitPushUploadStrategy
	public static final Function1<File, Option<Git>> defaultGitOpenFn = directory ->
		Try(() -> Git.open(directory))
			.onFailure(
				error -> log.error( "error while acessing repository aborting push: {}", error)
			)
			.toOption()
			;

	public static final Function1<Git, Option<Iterable<PushResult>>> defaultGitPushFn = git ->
		Try(() -> git.push().call())
			.onFailure( error -> log.error("error while pushing repo: {}", error) )
			.onSuccess( result -> log.info("git push result: {}", result) )
			.toOption()
			;

	public static final Function1<Repo, Option<File>> defaultRepoDirectoryFn = repo ->
		call(repo::directory)
			.onEmpty( () -> log.error( "missing directory for repository {}, aborting push", repo) )
			;

	@Override
	public Iterable<PushResult> get(){
		return repository
			.onEmpty(() -> log.error("missing repository for uploading, aborting push"))
			.peek(repo -> log.info("pushing {}", repo))
			.flatMap(repoDirectoryFn)
			.flatMap(gitOpenFn)
			.flatMap(gitPushFn)
			.getOrElse(empty())
			;
	}

	public static <T extends Repo> Supplier<Iterable<PushResult>> GitPush(T repo){
		return GitPushUploadStrategy.builder()
			.repository(Option(repo))
      .credentials(None())
			.repoDirectoryFn(defaultRepoDirectoryFn)
			.gitOpenFn(defaultGitOpenFn)
			.gitPushFn(defaultGitPushFn)
			.build()
		;
	}

	public static <T extends Repo> Supplier<Iterable<PushResult>> GitPush(
    T repo, 
    CredentialsProvider credentials
  ){
		return GitPushUploadStrategy.builder()
			.repository(Option(repo))
      .credentials(Option(credentials))
			.repoDirectoryFn(defaultRepoDirectoryFn)
			.gitOpenFn(defaultGitOpenFn)
			.gitPushFn(defaultGitPushFn)
			.build()
		;
	}
}
