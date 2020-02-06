# Enki

 [![Release](https://github.com/stCarolas/Enki/workflows/Release/badge.svg)](https://github.com/stCarolas/Enki/actions?query=workflow%3ARelease)
 [![Version](https://img.shields.io/github/v/tag/stCarolas/Enki?sort=semver)](https://github.com/stCarolas/Enki/packages)
 [![Codacy Badge](https://api.codacy.com/project/badge/Grade/a631d4eeb0834e8f99035ce52c204acb)](https://app.codacy.com/manual/stcarolas/Enki?utm_source=github.com&utm_medium=referral&utm_content=stCarolas/Enki&utm_campaign=Badge_Grade_Dashboard)

- ![Core Concepts](https://github.com/stCarolas/Enki/tree/master/core)
- ![Console Runner](https://github.com/stCarolas/Enki/tree/master/tools/cli-runner)
- Providers:
    - ![Bitbucket](https://github.com/stCarolas/Enki/tree/master/providers/bitbucket)
    - ![Github](https://github.com/stCarolas/Enki/tree/master/providers/github)
    - ![Gitlab](https://github.com/stCarolas/Enki/tree/master/providers/gitlab)
    - ![Gitea](https://github.com/stCarolas/Enki/tree/master/providers/gitea)

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
