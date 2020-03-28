# Quick Start

## 1. Installation

```xml
<project>
  <builds>
    <plugins>
      <plugin>
        <groupId>team.yi.maven.plugin</groupId>
        <artifactId>maven-semantic-gitlog</artifactId>
        <version>0.19.4</version>
        <configuration>
          <fileSets>
            <fileSet>
              <target>${project.basedir}/CHANGELOG.md</target>
              <template>${project.basedir}/config/gitlog/CHANGELOG.md.mustache</template>
            </fileSet>
            <fileSet>
              <target>${project.basedir}/CHANGELOG.zh-cn.md</target>
              <template>${project.basedir}/config/gitlog/CHANGELOG.zh-cn.md.mustache</template>
            </fileSet>
          </fileSets>
          <jsonFile>${project.basedir}/CHANGELOG.json</jsonFile>

          <issueUrlTemplate>${project.scm.url}/issues/:issueId</issueUrlTemplate>
          <commitUrlTemplate>${project.scm.url}/commit/:commitId</commitUrlTemplate>
          <mentionUrlTemplate>https://github.com/:username</mentionUrlTemplate>

          <updateProjectVersion>false</updateProjectVersion>
          <derivedVersionMark>NEXT_VERSION:==</derivedVersionMark>

          <commitLocales>
            <en>${project.basedir}/config/gitlog/commit-locales.md</en>
            <zh-cn>${project.basedir}/config/gitlog/commit-locales.zh-cn.md</zh-cn>
          </commitLocales>
        </configuration>
      </plugin>
    </plugins>
  </builds>
</project>
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
$ ./mvnw semantic-gitlog:changelog
```

And then check your `CHANGELOG*` at root folder of the project.

```bash
$ ./mvnw semantic-gitlog:derive
```

It's will print the `nextVersion`.

> [!TIP|label: See more]
> - [semantic-commit](/en-us/semantic-commit)
> - [semantic-gitlog](/en-us/semantic-gitlog)
> - [maven-semantic-gitlog](/en-us/maven-semantic-gitlog)
> - [gradle-semantic-gitlog](/en-us/gradle-semantic-gitlog)
