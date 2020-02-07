# Enki Runner

Tool for running jar with RepoHandler from cli

### Usage

```
$ java -jar enki-server-0.1.9.jar
Missing required parameter: <jar>
Usage: enki [-hV] [--bitbucket] [--gitea] [--github] [--gitlab]
            [--bitbucket-endpoint=<bitbucketEndpoint>]
            [--bitbucket-token=<bitbucketToken>]
            [--github-organization=<githubOrg>]
            [--github-password=<githubPassword>]
            [--github-username=<githubUser>] <jar>
run enki handlers from cli
      <jar>         jar with handlers
      --bitbucket   use Bitbucket RepoProvider
      --bitbucket-endpoint=<bitbucketEndpoint>
                    Bitbucket REST API Endpoint
      --bitbucket-token=<bitbucketToken>
                    Bitbucket Access Token
      --gitea       use Gitea RepoProvider
      --github      use GitHub RepoProvider
      --github-organization=<githubOrg>
                    GitHub Organization
      --github-password=<githubPassword>
                    GitHub Password or Personal Access Token
      --github-username=<githubUser>
                    GitHub Username
      --gitlab      use GitLab RepoProvider
  -h, --help        Show this help message and exit.
  -V, --version     Print version information and exit.
```

### Run handler in server mode
```
java -jar enki-server-0.1.9.jar \
    enki-logging-handlers-0.1.9.jar \
    --server \
    --github \
    --github-username <GITHUB_USERNAME> \
    --github-password <GITHUB_PASSWORD_OR_TOKEN>
```


### Run handler from cli

```
java -jar enki-server-0.1.9.jar \
    enki-logging-handlers-0.1.9.jar \
    --github \
    --github-username <GITHUB_USERNAME> \
    --github-password <GITHUB_PASSWORD_OR_TOKEN>
```
