package com.github.stcarolas;

import java.util.Arrays;

import com.github.stcarolas.analyzers.MavenDependencyCollector;
import com.github.stcarolas.model.GiteaRepoProvider;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import lombok.val;

public class Launcher {

    private static Config config = ConfigFactory.load();

    public static void main(String[] args) {
        val gitea = new GiteaRepoProvider(
                config.getString("gitea.url"), 
                config.getString("gitea.org")
        );
        val gocdConfigRepoSync = new GocdConfigRepoSync(
            config.getString("gocd.url"),
            config.getString("gocd.username"),
            config.getString("gocd.password")
        );
        val mvnDepsCollector = new MavenDependencyCollector();
        new EnkiServer(
            Arrays.asList(gitea), Arrays.asList(gocdConfigRepoSync,mvnDepsCollector),
            "0.0.0.0", 8080
        );
    }
}
