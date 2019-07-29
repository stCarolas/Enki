package com.github.stcarolas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.stcarolas.gocd.api.GoCD;
import com.github.stcarolas.gocd.model.ConfigRepo;
import com.github.stcarolas.gocd.model.Material;
import com.github.stcarolas.model.Repo;

import feign.Feign;
import feign.auth.BasicAuthRequestInterceptor;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.val;

public class GocdConfigRepoSync implements Analyzer {
    private final GoCD gocd;
    private final List<String> configRepos;

    public GocdConfigRepoSync(
        String gocdUrl,
        String gocdUsername,
        String gocdPassword
    ){
        val interceptor = new BasicAuthRequestInterceptor(gocdUsername, gocdPassword);
        gocd = Feign.builder()
            .encoder(new JacksonEncoder())
            .decoder(new JacksonDecoder(Arrays.asList((Module) new JavaTimeModule())))
            .requestInterceptor(interceptor)
            .target(GoCD.class, gocdUrl);
        configRepos = getConfigRepos(gocd);
    }

    @Override
    public void analyze(Repo repo) {
        if ( configRepos.contains(repo.getSshUrl()) ) {
            return;
        }

        val materialAttributes = new HashMap<String, Object>();
        materialAttributes.put("url", repo.getSshUrl());
        
        val material = new Material();
        material.setType("git");
        material.setAttributes(materialAttributes);

        val configRepo = new ConfigRepo();
        configRepo.setConfiguration(new ArrayList<Object>());
        configRepo.setMaterial(material);
        configRepo.setPluginId("yaml.config.plugin");
        configRepo.setId(repo.getName());

        gocd.addConfigRepo(configRepo);
    }

    private static List<String> getConfigRepos(GoCD gocd){
        return gocd.listConfigRepos()
            .getResponse()
            .getRepos().stream()
            .map( repo -> (String) repo.getMaterial().getAttributes().get("url"))
            .collect(Collectors.toList());
    }
    
}
