package com.github.stcarolas.enki.core.git.dagger;
import java.io.File;

import com.github.stcarolas.enki.core.git.DefaultTransportConfigCallback;
import com.github.stcarolas.enki.core.git.GitCloneSsh;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.TransportConfigCallback;

import dagger.Module;
import dagger.Provides;
import io.vavr.Function2;
import io.vavr.control.Try;

@Module(includes=GitCommonModule.class)
public class GitSshModule {

	@Provides static Function2<String, File, CloneCommand> cloneCommand(
		CloneCommand cloneCommand,
		TransportConfigCallback callback
	){
		return (url, dir) -> 
			cloneCommand
				.setURI(url)
				.setDirectory(dir)
				.setTransportConfigCallback(callback);
	}

	@Provides static TransportConfigCallback callback(){
		return new DefaultTransportConfigCallback();
	}
	
}
