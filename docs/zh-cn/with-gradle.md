# 入门

## 1. 安装

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

## 2. 设置模板

模板 `CHANGELOG.mustache` 的内容如下：

```markdown
# 更新日志
{{#tags}}

{{#version}}## {{version}} ({{#releaseDate}}{{#formatDate}}{{releaseDate}}|yyyy-MM-dd{{/formatDate}}{{/releaseDate}}{{^releaseDate}}{{#formatDate}}{{now}}|yyyy-MM-dd{{/formatDate}}{{/releaseDate}}){{/version}}{{^version}}## {{nextVersion}} (Unreleased, {{#releaseDate}}{{#formatDate}}{{releaseDate}}|yyyy-MM-dd{{/formatDate}}{{/releaseDate}}{{^releaseDate}}{{#formatDate}}{{now}}|yyyy-MM-dd{{/formatDate}}{{/releaseDate}}){{/version}}
{{#sections}}

### {{title}}

{{#commits}}
- {{#commitScope}}**{{#scopeProfile}}{{commitPackage}}{{commitScope}}|zh-cn{{/scopeProfile}}**: {{/commitScope}}{{#localeMap}}{{& zh-cn.subject}}{{/localeMap}}{{^localeMap}}{{& commitSubject}}{{/localeMap}}{{#subjectIssues}} ([#{{id}}]({{url}})){{/subjectIssues}} ([{{hash8}}]({{commitUrl}})){{#hasCloseIssues}}, closes{{#closeIssues}} [#{{id}}]({{url}}){{/closeIssues}}{{/hasCloseIssues}}
{{/commits}}

{{/sections}}
{{^sections}}

暂无更新说明。

{{/sections}}
{{/tags}}
{{^tags}}

暂无内容。
{{/tags}}
```

## 3. 推送约定式提交（Conventional Commits）

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

## 4. 运行插件

```bash
$ ./gradlew changelog
```

然后查看项目根目录下的 `CHANGELOG*` 文件。

```bash
$ ./gradlew derive
```

这将打印 `nextVersion`。

> [!TIP|label: 了解更多]
> - [semantic-commit](/zh-cn/semantic-commit)
> - [semantic-gitlog](/zh-cn/semantic-gitlog)
> - [maven-semantic-gitlog](/zh-cn/maven-semantic-gitlog)
> - [gradle-semantic-gitlog](/zh-cn/gradle-semantic-gitlog)
