package com.github.stcarolas.enki.core.provider.strategies.download;

import static com.github.stcarolas.enki.core.util.Lifting.call;
import static io.vavr.API.Option;
import static io.vavr.API.Try;

import java.io.File;
import java.util.function.Supplier;

import com.github.stcarolas.enki.core.Repo;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import io.vavr.Function4;
import io.vavr.control.Option;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.With;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class GitHttpDownloadStrategy<T extends Repo> implements Supplier<File> {

	@With private final Option<String> url;
	@With private final Option<T> repository;
	@With private final Supplier<CloneCommand> cloneCommand;
	@With private final Supplier<CredentialsProvider> credentials;
	@With private final Function4<
    Supplier<CredentialsProvider>, Supplier<CloneCommand>, String, File, 
    CloneCommand
  > clone;

	public static final Supplier<CloneCommand> cloneCommandFn = 
		() -> Git.cloneRepository();

	public static final Supplier<CredentialsProvider> credentialsFn = 
		() -> new UsernamePasswordCredentialsProvider("","");

	public static final Function4<
    Supplier<CredentialsProvider>, Supplier<CloneCommand>, String, File, 
    CloneCommand
  > cloneFn = ( credentials, cloneCommand, url, dir ) -> 
			cloneCommand.get()
				.setURI(url)
        .setCloneAllBranches(true)
				.setDirectory(dir)
        .setCredentialsProvider(credentials.get());

	@Override
	public File get(){
    var dir = repository.flatMap(
      repo -> call(repo::directory)
        .onEmpty(() -> log.error("missing any directory to clone into"))
    );
    var cloneResult = Option(clone)
      .map($ -> $.apply(credentials))
      .map($ -> $.apply(cloneCommand))
      .flatMap($ -> url.map($::apply))
      .flatMap($ -> dir.map($::apply))
      .toTry()
      .mapTry(CloneCommand::call)
      .onFailure( error -> log.error("error while cloning {}: {}", url, error) )
      .onSuccess( git -> log.info("repository with url {} was cloned", url) );
    return cloneResult.isSuccess() ? dir.getOrNull() : null;
	}

	public static <T extends Repo>Supplier<File> GitHttpClone(T repo, String url){
		return GitHttpDownloadStrategy.builder()
			.url(
          Option(url).onEmpty(() -> log.error("missing ssh url to use for cloning"))
      )
			.repository(
          Option((Repo)repo).onEmpty(
						() -> log.error("missing repository to clone for url {}", url)
					)
      )
			.credentials(credentialsFn)
			.cloneCommand(cloneCommandFn)
			.clone(cloneFn)
			.build();
	}

}
