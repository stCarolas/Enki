package com.github.stcarolas.enki.core.provider.strategies.download;

import java.io.File;
import java.util.function.Supplier;

import com.github.stcarolas.enki.core.transport.DefaultTransportConfigCallback;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;

import io.vavr.Function4;

public class GitFunctions {

	public static final Supplier<CloneCommand> CLONE_COMMAND_SUPPLIER = 
		() -> Git.cloneRepository();

	public static final Supplier<DefaultTransportConfigCallback> SSH_TRANSPORT_SUPPLIER = 
		() -> new DefaultTransportConfigCallback();

	public static final Function4<
		Supplier<DefaultTransportConfigCallback>, Supplier<CloneCommand>, String, File, 
		CloneCommand
	> SSH_CLONE_COMMAND_SUPPLIER = ( transport, cloneCommand, url, dir ) -> 
			cloneCommand.get()
				.setURI(url)
				.setDirectory(dir)
				.setTransportConfigCallback(transport.get());

}
