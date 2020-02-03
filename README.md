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
