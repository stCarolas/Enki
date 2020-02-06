# Enki

[![Release](https://github.com/stCarolas/Enki/workflows/Release/badge.svg)](https://github.com/stCarolas/Enki/actions?query=workflow%3ARelease) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/a631d4eeb0834e8f99035ce52c204acb)](https://app.codacy.com/manual/stcarolas/Enki?utm_source=github.com&utm_medium=referral&utm_content=stCarolas/Enki&utm_campaign=Badge_Grade_Dashboard)

Manipulate your git repos. At scale.
![Console Runner](https://github.com/stCarolas/Enki/tree/master/tools/cli-runner)

# RepoProviders, Handlers and Functions
Enki consists of modules. Main module - enki-core which provides core logic and interfaces for another modules to implement.
Each other module should offer one of three functionalities:
- RepoProvider
- RepoHandler
- Function

RepoProvider can be used for loading repos from git servers like GitHub, Gitlab, etc.
Available RepoProvider's at this moment are GitHub, Gitea.

RepoHandler are used to do some work on repos loaded by RepoProvider.
Available RepoHandler's:
- GoCD ConfigRepo Sync
- Logging
- GitHub -> Gitea Mirroring
- Maven Dependency Handlers
- Discord Channel Creator

# Set Up with Maven

Add dependency to pom
```
    <dependency>
        <groupId>com.github.stcarolas.enki</groupId>
        <artifactId>enki-core</artifactId>
        <version>0.0.29</version>
    </dependency>
```

Add github repository to pom
```
  <repositories>
    <repository>
      <id>enki-github-repo</id>
      <url>https://maven.pkg.github.com/stCarolas/Enki</url>
    </repository>
  </repositories>
```

Add github personal token with `read:packages` scope to settings.xml (use Personal Access Token from Github, not password)
```
<servers>
  <server>
    <id>enki-github-repo</id>
    <username>YOUR_GITHUB_USERNAME</username>
    <password>YOUR_GITHUB_TOKEN</password>
  </server>
</servers>
```

# Set Up with Gradle

Add repository (use Personal Access Token from Github with `read:packages` scope, not password)
```
    maven { 
        credentials(PasswordCredentials) {
            username "<MY_USERNAME>"
            password "<MY_GITHUB_TOKEN>"
        }
        url "https://maven.pkg.github.com/stCarolas/Enki" 
    }
```

Add dependency
```
implementation("com.github.stcarolas.enki:enki-core:0.0.29")
```
