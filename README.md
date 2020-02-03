![Release](https://github.com/stCarolas/Enki/workflows/Release/badge.svg)
# Enki
Manipulate your git repos. At scale.

# Modules
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
- GitHub->Gitea Mirroring
- Maven Dependency Handlers
- Discord Channel Creator

# Examples
In directory `examples`

# How to use with Maven

Add dependency to pom
```
    <dependency>
        <groupId>com.github.stcarolas.enki</groupId>
        <artifactId>enki-*</artifactId>
        <version>X.X.X</version>
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

Add github personal token with 'read:packages' scope to settings.xml
```
<servers>
  <server>
    <id>enki-github-repo</id>
    <username>YOUR_GITHUB_USERNAME</username>
    <password>YOUR_GITHUB_TOKEN</password>
  </server>
</servers>
```
