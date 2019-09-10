package com.github.stcarolas.enki.logginganalyzers;

import java.util.Map;
import java.util.concurrent.Callable;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(
    name = "generator",
    mixinStandardHelpOptions = true,
    version = "checksum 0.1",
    description = "generate files from git repo with template"
)
public class GeneratorCli implements Callable<Integer> {

    @Option(names = { "-d", "--data" })
    Map<String, String> data;

    @Parameters(index = "0", description = "The git repo url")
    private String gitSshUrl;

    public static void main(String[] args) {
        int exitCode = new CommandLine(new GeneratorCli()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception {
        Generator.builder()
            .cloneUrl(gitSshUrl)
            .data(data)
            .build()
            .analyze(null);
        return 0;
    }
}
