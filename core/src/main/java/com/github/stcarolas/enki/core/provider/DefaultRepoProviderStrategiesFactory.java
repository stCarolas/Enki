package com.github.stcarolas.enki.core.provider;

import java.io.File;
import java.util.function.Supplier;

import com.github.stcarolas.enki.core.Repo;
import com.github.stcarolas.enki.core.provider.strategies.download.GitCloneDownloadStrategy;
import static com.github.stcarolas.enki.core.provider.strategies.upload.GitPushUploadStrategy.GitPushUploadStrategy;

import org.eclipse.jgit.transport.PushResult;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DefaultRepoProviderStrategiesFactory {

	public static <T extends Repo>Supplier<Iterable<PushResult>> gitSshPush(T repo){
		return GitPushUploadStrategy(repo);
	}

	public static <T extends Repo>Supplier<File> gitSshClone(T repo, String sshUrl){
		return GitCloneDownloadStrategy.GitSshClone(repo,sshUrl);
	}

}
