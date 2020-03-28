# semantic-gitlog

Changelog generator base on `angular-style`. This component contains only one service:
- `GitlogService` 

## GitlogService

Used to generate CHANGELOG base on `angular-style` with Mustache template engine.

### Preparations {docsify-ignore}

Please go and learn: 
- https://github.com/tomasbjerre/git-changelog-lib
- https://github.com/skuzzle/semantic-version

> [!NOTE]
> Maybe give up `git-changelog-lib` in the future.

### Usage {docsify-ignore}

```java
public class hello {
    public static final String DEFAULT_TEMPLATE_FILE = "config/gitlog/CHANGELOG.mustache";
    public static final String DEFAULT_TARGET_FILE = "CHANGELOG.md";

    public void generate() throws IOException, GitChangelogRepositoryException {
        final GitlogSettings gitlogSettings = new GitlogSettings();
        gitlogSettings.setStrategy(ReleaseStrategy.strict);
        // gitlogSettings...
    
        final GitChangelogApi builder = GitChangelogApi.gitChangelogApiBuilder();
        builder.withToRef("master");
        // builder...

        final Set<FileSet> fileSets = this.getFileSets();
        final File template = Paths.get(DEFAULT_TEMPLATE_FILE).toFile();
        final File target = Paths.get(DEFAULT_TARGET_FILE).toFile();
        fileSets.add(new FileSet(template, target));

        final GitlogService releaseLogService = new GitlogService(gitlogSettings, builder);
        releaseLogService.saveToFiles(fileSets);
    }
}
```

### Configuration {docsify-ignore}

| option | description |
| ------ | ----------- |
| `isUnstable` | Enable `development-phase`, breaking changes only increases the minor version number. Default is `false`. |
| `strategy` | Release strategy. Optional values: `strict`, `slow`. Default is `strict`. |
| `forceNextVersion` | Allow force increase `nextVersion` when the version dose not grow. Default is `true`. |
| `longDateFormat` | The long date format pattern. Default is `yyyy-MM-dd HH:mm:ss`. |
| `shortDateFormat` | The short date format pattern. Default is `yyyy-MM-dd`. |
| `lastVersion` | `Tag` as version by default. This option allows you to manually specify the value of `lastVersion`. Default is `0.1.0`. |
| `preRelease` | Set the initial value of `preRelease`. Default is `null`.  |
| `buildMetaData` | Set the initial value of `buildMetaData`. Default is `null`. |
| `majorTypes` | Increase major version when these commit types are matched. By default only when **BREAKING CHANGE** is discovered. |
| `minorTypes` | Increase minor version when these commit types are matched. Default is `feat`. |
| `patchTypes` | Increase patch version when these commit types are matched. Default is `refactor,perf,fix,chore,revert,docs,build`. |
| `preReleaseTypes` | Increase preRelease version when these commit types are matched. Default is `null`. |
| `buildMetaDataTypes` | Increase buildMetaData version when these commit types are matched. Default is `null`. |
| `hiddenTypes` | These commit types are hidden in the changelog. Default is `release`. |
| `commitLocales` | A file set with localized commit messages. Default is `null`. |
| `jsonFile` | JSON file path. Default is `null`. |

> [!TIP]
> Also refer to: [semantic-commit](/en-us/semantic-commit)
