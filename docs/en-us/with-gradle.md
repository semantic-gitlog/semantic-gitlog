# Quick Start

## 1. Installation

```groovy
plugins {
    id 'java'
    id 'team.yi.semantic-gitlog' version '0.4.1.1'
}

changelog {
    toRef = "master"
    isUnstable = true

    jsonFile = file("${project.rootDir}/CHANGELOG.json")
    fileSets = [
        {
            template = file("${project.rootDir}/config/gitlog/CHANGELOG.mustache")
            target = file("${project.rootDir}/CHANGELOG.md")
        },
        {
            template = file("${project.rootDir}/config/gitlog/CHANGELOG.zh-cn.mustache")
            target = file("${project.rootDir}/CHANGELOG.zh-cn.md")
        }
    ]
    commitLocales = [
        "zh-cn": file("${project.rootDir}/config/gitlog/commit-locales.zh-cn.md")
    ]
}

derive {
    toRef = "master"
    isUnstable = true
    derivedVersionMark = "NEXT_VERSION:=="

    commitLocales = [
        "zh-cn": file("${project.rootDir}/config/gitlog/commit-locales.zh-cn.md")
    ]
}
```

## 2. Place template

Contents of `CHANGELOG.mustache`:

```markdown
# Changelog
{{#tags}}

{{#version}}## {{version}} ({{#releaseDate}}{{#formatDate}}{{releaseDate}}|yyyy-MM-dd{{/formatDate}}{{/releaseDate}}{{^releaseDate}}{{#formatDate}}{{now}}|yyyy-MM-dd{{/formatDate}}{{/releaseDate}}){{/version}}{{^version}}## {{nextVersion}} (Unreleased, {{#releaseDate}}{{#formatDate}}{{releaseDate}}|yyyy-MM-dd{{/formatDate}}{{/releaseDate}}{{^releaseDate}}{{#formatDate}}{{now}}|yyyy-MM-dd{{/formatDate}}{{/releaseDate}}){{/version}}
{{#sections}}

### {{title}}

{{#commits}}
- {{#commitScope}}**{{commitPackage}}{{commitScope}}**: {{/commitScope}}{{& commitSubject}}{{#subjectIssues}} ([#{{id}}]({{url}})){{/subjectIssues}} ([{{hash8}}]({{commitUrl}})){{#hasCloseIssues}}, closes{{#closeIssues}} [#{{id}}]({{url}}){{/closeIssues}}{{/hasCloseIssues}}
{{/commits}}

{{/sections}}
{{^sections}}

No update notes.

{{/sections}}
{{/tags}}
{{^tags}}

No contents.
{{/tags}}
```

## 3. Push Conventional Commits

````cc
refactor(parser): refactor commit message parser

BREAKING CHANGE: regex-based parser already removed.

------------
# Locales
- **[zh-cn]** 重构提交消息解析器
- **[zh-tw]** 重構提交消息解析器
- **[it-IT]** Refactoring del parser del messaggio di commitRefactoring the commit message parser
- **[de]** Umgestaltung des Commit-Nachrichtenparsers
````

## 4. Execute goal

```bash
$ ./gradlew changelog
```

And then check your `CHANGELOG*` at root folder of the project.

```bash
$ ./gradlew derive
```

It's will print the `nextVersion`.

> [!TIP|label: See more]
> - [semantic-commit](/en-us/semantic-commit)
> - [semantic-gitlog](/en-us/semantic-gitlog)
> - [maven-semantic-gitlog](/en-us/maven-semantic-gitlog)
> - [gradle-semantic-gitlog](/en-us/gradle-semantic-gitlog)
