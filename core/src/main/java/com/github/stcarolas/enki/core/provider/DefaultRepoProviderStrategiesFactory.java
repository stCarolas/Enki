package com.github.stcarolas.enki.core.provider;

import java.io.File;
import java.util.function.Supplier;

import com.github.stcarolas.enki.core.Repo;
import com.github.stcarolas.enki.core.provider.strategies.download.GitCloneDownloadStrategy;
import com.github.stcarolas.enki.core.provider.strategies.upload.GitPushUploadStrategy;

import org.eclipse.jgit.transport.PushResult;

public class DefaultRepoProviderStrategiesFactory {

	public static <T extends Repo>Supplier<Iterable<PushResult>> gitSshPush(T repo, String sshUrl){
		return GitPushUploadStrategy.gitSshPush(repo, sshUrl);
	}

	public static <T extends Repo>Supplier<File> gitSshClone(T repo, String sshUrl){
		return GitCloneDownloadStrategy.gitSshClone(repo,sshUrl);
	}

}
