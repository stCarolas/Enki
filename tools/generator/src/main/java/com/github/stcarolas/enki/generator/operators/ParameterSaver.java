package com.github.stcarolas.enki.generator.operators;

import java.io.File;
import java.io.FileWriter;
import java.util.UUID;

import com.github.stcarolas.enki.logginganalyzers.GenerationParameters;
import com.google.gson.GsonBuilder;

import io.vavr.control.Try;
import lombok.val;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ParameterSaver {

    public void save(String url, GenerationParameters parameters) {
        val enkiDirectory = new File(url + ".enki");
        if (!enkiDirectory.exists()) {
            enkiDirectory.mkdirs();
        }
        val gson = new GsonBuilder().setPrettyPrinting().create();
        Try.of(
            () -> {
                FileWriter writer = new FileWriter(
                    enkiDirectory.toString() + "/" + UUID.randomUUID().toString() + ".json"
                );
                writer.write(gson.toJson(parameters));
                writer.close();
                return writer;
            }
        )
            .onFailure(error -> log.error("Error: {}", error));
    }
}
