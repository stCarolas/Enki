package com.github.stcarolas.enki.core.git.factories;

import java.io.File;

import javax.inject.Named;

import com.github.stcarolas.enki.core.git.DefaultTransportConfigCallback;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.TransportConfigCallback;

import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Prototype;
import io.micronaut.context.annotation.Requires;
import io.vavr.Function2;

@Factory
public class GitSshModule {

	@Prototype @Requires(property="protocol", value="ssh")
	@Named("FilledCloneCommand") 
	Function2<String, File, CloneCommand> cloneCommand(
		CloneCommand cloneCommand,
		TransportConfigCallback callback
	){
		return (url, dir) -> 
			cloneCommand
				.setURI(url)
				.setDirectory(dir)
				.setTransportConfigCallback(callback);
	}

	@Prototype @Requires(property="protocol", value="ssh")
	TransportConfigCallback callback(){
		return new DefaultTransportConfigCallback();
	}
	
}
