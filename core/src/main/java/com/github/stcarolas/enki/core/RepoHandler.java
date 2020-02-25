package com.github.stcarolas.enki.core;

public interface RepoHandler<T extends Repo> {
	/**
	*  
	* @param repo a repository needed to be handled
	* @return handled repository
	 */
	T handle(T repo);
}
