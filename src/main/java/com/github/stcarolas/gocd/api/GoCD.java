package com.github.stcarolas.gocd.api;

import com.github.stcarolas.gocd.model.ConfigRepo;
import com.github.stcarolas.gocd.model.GoCDResponseWrapper;

import feign.Headers;
import feign.RequestLine;

public interface GoCD {

  @RequestLine("GET /go/api/admin/config_repos")
  @Headers("Accept: application/vnd.go.cd.v2+json")
  GoCDResponseWrapper<ConfigRepo.List> listConfigRepos();

  @RequestLine("POST /go/api/admin/config_repos")
  @Headers({"Accept: application/vnd.go.cd.v2+json","Content-Type: application/json"})
  ConfigRepo addConfigRepo(ConfigRepo repo);
  
}
