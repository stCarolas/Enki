# Core Concepts

Enki consists of modules. Main module - enki-core which provides core logic and interfaces for another modules to implement.
Each other module should provide one of three functionalities:
- RepoProvider
- RepoHandler
- Function

### RepoProvider

RepoProvider can be used for loading repos from git servers like GitHub, Gitlab, etc.

### RepoHandler

RepoHandler are used to do some work on repos loaded by RepoProvider.

### Function

Function in Enki is a some form of adapter which takes list of repos from RepoProvider and transforms it to another list (filter, reduce, etc)
