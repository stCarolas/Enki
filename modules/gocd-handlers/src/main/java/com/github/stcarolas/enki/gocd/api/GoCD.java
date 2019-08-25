package com.github.stcarolas.enki.gocd.api;

import com.github.stcarolas.enki.gocd.model.ConfigRepo;
import com.github.stcarolas.enki.gocd.model.GoCDResponseWrapper;
import com.github.stcarolas.enki.gocd.model.NotifyMaterialRequest;
import com.github.stcarolas.enki.gocd.model.NotifyMaterialResponse;
import feign.Headers;
import feign.RequestLine;

public interface GoCD {
    @RequestLine("GET /go/api/admin/config_repos")
    @Headers("Accept: application/vnd.go.cd.v2+json")
    GoCDResponseWrapper<ConfigRepo.List> listConfigRepos();

    @RequestLine("POST /go/api/admin/config_repos")
    @Headers({ "Accept: application/vnd.go.cd.v2+json", "Content-Type: application/json" })
    ConfigRepo addConfigRepo(ConfigRepo repo);

    @RequestLine("POST /go/api/admin/materials/git/notify")
    @Headers({ "Accept: application/vnd.go.cd.v1+json", "Content-Type: application/json" })
    NotifyMaterialResponse notifyGitMaterials(NotifyMaterialRequest request);
}
