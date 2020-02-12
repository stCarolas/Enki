package com.github.stcarolas.enki;

import com.github.stcarolas.enki.jacoco.Launcher;

import io.vavr.collection.List;
import picocli.CommandLine;

public class Application {

	public static void main(String[] args) {
		int exitCode = (new CommandLine(
			Launcher.builder().handlers(List.of(JacocoHandler.builder().build())).build()
		))
			.execute(args);
		System.exit(exitCode);
	}
}
