package com.github.stcarolas.enki.core.git.factories;
import java.io.File;

import javax.inject.Named;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Prototype;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.annotation.Value;
import io.vavr.Function2;

@Factory
public class GitHttpModule {

	@Value("${http.username}")
	String username;

	@Value("${http.password}")
	String password;

	@Prototype @Requires(property="protocol", value="http")
	CredentialsProvider credentials(){
		return new UsernamePasswordCredentialsProvider(username, password);
	}

	@Prototype @Requires(property="protocol", value="http") 
	@Named("FilledCloneCommand") 
	Function2<String, File, CloneCommand> cloneCommand(
		CloneCommand cloneCommand,
		CredentialsProvider credentials
	){
		return ( url, dir ) -> cloneCommand
			.setURI(url)
			.setDirectory(dir)
			.setCredentialsProvider(credentials);
	}
	
}
