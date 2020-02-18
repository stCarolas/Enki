package com.github.stcarolas.enki.core;

import io.vavr.control.Option;

public interface RepoHandler<T extends Repo> {
	/**
	*  
	* @param repo a repository needed to be handled
	* @return handled repository
	 */
	Option<T> handle(T repo);
}
