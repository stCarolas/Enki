package com.github.stcarolas.enki.core;

import java.io.File;

import io.vavr.collection.Seq;

public interface Repo {

	/**
	*
	* @return unique technical id of this copy of repository
	*/
	String id();

	/**
	*
	* @return some human-readable name of repository
	*/
	String name();

	/**
	*
	* @return file-based access to this repository
	*/
	File directory();

	/**
	*
	* @return list of {@link RepoProvider} which able to host and provide this repository
	*/
	Seq<RepoProvider<? extends Repo>> providers();

	/**
	*
	* @return copy of this repository after commiting changes
	*/
	Repo commit(String commitMessage);

}
