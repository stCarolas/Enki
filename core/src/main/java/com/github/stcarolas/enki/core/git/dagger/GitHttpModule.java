package com.github.stcarolas.enki.core.git.dagger;
import java.io.File;

import javax.inject.Named;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Prototype;
import io.micronaut.context.annotation.Requires;
import io.vavr.Function2;
import lombok.AllArgsConstructor;

@Factory
public class GitHttpModule {

	@Prototype @Requires(property="protocol", value="http")
	CredentialsProvider credentials(
		//@Named("gitUsername") String username,
		//@Named("gitPassword") String password
	){
		return new UsernamePasswordCredentialsProvider("","");
	}

	@Prototype  @Requires(property="protocol", value="http") @Named("FilledCloneCommand") 
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
