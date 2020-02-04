package com.github.stcarolas.enki.gocd.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class NotifyMaterialRequest {
    @JsonProperty("repository_url")
    private String repositoryUrl;
}
