package com.github.stcarolas.enki.gocd.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GoCDResponseWrapper<T> {
    @JsonProperty("_links")
    Links links;

    @Data
    public static class Links {
        private Link self;
    }

    @JsonProperty("_embedded")
    private T response;
}
