package com.github.stcarolas.enki.core.provider;

import java.io.File;
import java.util.function.Supplier;

import com.github.stcarolas.enki.core.Repo;
import static com.github.stcarolas.enki.core.provider.strategies.download.GitCloneDownloadStrategy.GitSshClone;
import static com.github.stcarolas.enki.core.provider.strategies.download.GitHttpCloneWithDefaultBranch.GitHttpClone;
import static com.github.stcarolas.enki.core.provider.strategies.upload.GitPushUploadStrategy.GitPush;

import org.eclipse.jgit.transport.PushResult;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DefaultRepoProviderStrategiesFactory {

	public static <T extends Repo>Supplier<Iterable<PushResult>> gitSshPush(T repo){
		return GitPush(repo);
	}

	public static <T extends Repo>Supplier<File> gitSshClone(T repo, String url){
		return GitSshClone(repo,url);
	}

	public static <T extends Repo>Supplier<File> gitHttpClone(
		T repo, 
		String url, 
		Boolean allBranches
	){
		return GitHttpClone(repo,url);
	}

}
