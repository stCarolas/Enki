package com.github.stcarolas.enki.core;

import java.io.File;

import io.vavr.collection.Seq;
import io.vavr.control.Option;

public interface Repo {

	/**
	*
	* @return unique technical id of this copy of repository
	*/
	Option<String> id();

	/**
	*
	* @return some human-readable name of repository
	*/
	Option<String> name();

	/**
	*
	* @return file-based access to this repository
	*/
	Option<File> directory();

	/**
	*
	* @return list of {@link RepoProvider} which able to host and provide this repository
	*/
	Seq<RepoProvider<? extends Repo>> providers();

	/**
	*
	* @return copy of this repository after commiting changes
	*/
	Option<? extends Repo> commit(String commitMessage);

}
