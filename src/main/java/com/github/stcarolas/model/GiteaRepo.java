package com.github.stcarolas.model;

import lombok.Data;

@Data
public class GiteaRepo {
    Gitea gitea;
    String name;
    String owner;
}
