package com.github.stcarolas.enki;

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipFile;

import com.github.stcarolas.enki.core.Repo;
import com.github.stcarolas.enki.core.RepoHandler;

import org.xeustechnologies.jcl.JarClassLoader;

import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ClasspathScanner {

	public static <T extends Repo> List<RepoHandler<T>> scan(String jar) {
		Set<String> classes = new HashSet<>();
		Try.of(() -> new ZipFile(jar))
			.onFailure(log::error)
			.map(ZipFile::entries)
			.onSuccess(
				enumeration -> enumeration.asIterator()
					.forEachRemaining(
						entry -> {
							String fileName = entry.getName();
							log.debug("seek {}", fileName);
							if (fileName.endsWith("class")) {
								String className = fileName.replace("/", ".")
									.replace(".class", "");
								log.debug("detect {} as {}", fileName, className);
								classes.add(className);
							}
						}
					)
			);
		return load(jar, classes);
	}

	public static <T extends Repo> List<RepoHandler<T>> load(String jar, Set<String> classes) {
		JarClassLoader jcl = new JarClassLoader();
		jcl.add(jar);
		return classes.stream()
			.map(
				className -> Try.of(() -> jcl.loadClass(className)).onFailure(log::error)
			)
			.filter(Try::isSuccess)
			.map(Try::get)
			.filter(
				loadedClass -> Arrays.asList(loadedClass.getInterfaces())
					.contains(RepoHandler.class)
			)
			.map(
				handler -> Try.of(() -> (RepoHandler<T>) handler.newInstance())
					.onFailure(log::error)
			)
			.filter(Try::isSuccess)
			.map(Try::get)
			.collect(toList());
	}
}
