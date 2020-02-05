package com.github.stcarolas.enki.runner;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import com.github.stcarolas.enki.core.RepoHandler;
import org.xeustechnologies.jcl.JarClassLoader;

import lombok.extern.log4j.Log4j2;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(
    name = "enki",
    mixinStandardHelpOptions = true,
    version = "checksum 0.1",
    description = "run enki handlers from cli"
)
@Log4j2
public class ConsoleRunner implements Callable<Integer> {
    @Parameters(index = "0", description = "jar with handlers")
    private String jar;

    public static void main(String[] args) {
        int exitCode = new CommandLine(new ConsoleRunner()).execute(args);
        System.exit(exitCode);
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Integer call() throws Exception {
        Set<String> classes = new HashSet<>();
        try (ZipFile zipFile = new ZipFile(jar)) {
            Enumeration<? extends ZipEntry> zipEntries = zipFile.entries();
            while (zipEntries.hasMoreElements()) {
                String fileName = ((ZipEntry) zipEntries.nextElement()).getName();
                log.debug("seek {}", fileName);
                if (fileName.endsWith("class")) {
                    String className = fileName.replace("/", ".").replace(".class", "");
                    log.debug("detect {} as {}", fileName, className);
                    classes.add(className);
                }
            }
        }
        JarClassLoader jcl = new JarClassLoader();
        jcl.add(jar);
        for (String className : classes) {
            Class loadedClass = jcl.loadClass(className);
            if (List.of(loadedClass.getInterfaces()).contains(RepoHandler.class)) {
                log.info("Use {}", loadedClass.getName());
            }
        }

        return 0;
    }
}
