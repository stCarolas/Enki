package com.github.stcarolas.enki.bitbucket.provider;

import com.google.auto.value.AutoValue.Builder;

import lombok.Getter;

@Getter
@Builder
public class BitbucketRepoQueryOptions {

    private String visibility;
    private String permission;
    private String projectname;
    private String name;
    
}
