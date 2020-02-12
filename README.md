# Enki 

[![Release](https://github.com/stCarolas/Enki/workflows/Release/badge.svg)](https://github.com/stCarolas/Enki/actions?query=workflow%3ARelease)
[![Version](https://img.shields.io/github/v/tag/stCarolas/Enki?label=Version)](https://github.com/stCarolas/Enki/packages)
[![Docker Pulls](https://img.shields.io/docker/pulls/stcarolas/enki)](https://hub.docker.com/repository/docker/stcarolas/enki)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/a631d4eeb0834e8f99035ce52c204acb)](https://app.codacy.com/manual/stcarolas/Enki?utm_source=github.com&utm_medium=referral&utm_content=stCarolas/Enki&utm_campaign=Badge_Grade_Dashboard)

1. [Why](#why)
1. [Set Up With Maven](#set-up-with-maven)
1. [Set Up With Gradle](#set-up-with-gradle)
1. [Running With Docker](#running-with-docker)
1. ![Core Concepts](https://github.com/stCarolas/Enki/tree/master/core)
1. Providers:
    - ![Bitbucket](https://github.com/stCarolas/Enki/tree/master/providers/bitbucket)
    - ![Github](https://github.com/stCarolas/Enki/tree/master/providers/github)
    - ![Gitlab](https://github.com/stCarolas/Enki/tree/master/providers/gitlab)
    - ![Gitea](https://github.com/stCarolas/Enki/tree/master/providers/gitea)
1. Handlers:
    - ![Discord commit hook](https://github.com/stCarolas/Enki/tree/master/handlers/discord-commit-hook)
    - ![Mirror repo to Gitea](https://github.com/stCarolas/Enki/tree/master/handlers/gitea-mirror)
    - ![Archive GitHub repo](https://github.com/stCarolas/Enki/tree/master/handlers/github-archive-repo)
    - ![Sync with GoCD](https://github.com/stCarolas/Enki/tree/master/handlers/gocd-handlers)
    - ![JaCoCo configurator for maven projects](https://github.com/stCarolas/Enki/tree/master/handlers/jacoco)
    - ![Only print a list of found repositories](https://github.com/stCarolas/Enki/tree/master/handlers/logging-handlhandlers)
    - ![Collect all dependency from maven project](https://github.com/stCarolas/Enki/tree/master/handlers/logging-handlhandlers)
1. Tools:
    - ![Cli/Server Runner](https://github.com/stCarolas/Enki/tree/master/server)
    - ![Discord Bot](https://github.com/stCarolas/Enki/tree/master/tools/discord-bot)
    - ![Console Wrapper for Template Generator](https://github.com/stCarolas/Enki/tree/master/tools/generator)
1. ![Roadmap](https://github.com/stCarolas/Enki/milestones)

# Why

Sometimes we want to do some work with multiple repositories - add step with SonarQube to all 
pipelines, add JaCoCo to all projects, up library version everywhere, add helm charts, etc. 
There is where Enki can help you.

Enki takes all the problems with handling of cloning and iterating over multiple repositories, commiting and pushing them. 
All you need to do is write or reuse some handler which takes metadata and local cloned copy of repository and just does required job.

# Set Up with Maven

Add to pom.xml
```
<repositories>
    <repository>
      <id>jcenter</id>
      <url>https://jcenter.bintray.com/</url>
    </repository>
</repositories>
```
```
<dependency>
  <groupId>com.github.stcarolas.enki</groupId>
  <artifactId>enki-core</artifactId>
  <version>0.1.49</version>
  <type>pom</type>
</dependency>
```

# Set Up with Gradle

Add to build.gradle
```
repositories {
    jcenter()
}
```
```
implementation 'com.github.stcarolas.enki:enki-core:0.1.49'
```

# Running with Docker

Cli mode:
```
docker run -v $(pwd):/tmp stcarolas/enki \
    --github --github-username=stCarolas --github-password=252f939e823c441fc7a2f5914e93a9fe30725b3c \
    /tmp/enki-logging-handlers-0.1.5.jar 
```

Server mode:
```
docker run -v $(pwd):/tmp -p 8080:8080 stcarolas/enki \
    --server \
    --github --github-username=stCarolas --github-password=252f939e823c441fc7a2f5914e93a9fe30725b3c \
    /tmp/enki-logging-handlers-0.1.5.jar 
```
