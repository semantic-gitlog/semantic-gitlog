# semantic-gitlog

`angular-style` 风格的更新日志生成器。该组件仅包含一个服务：
- `GitlogService` 

## GitlogService

使用 Mustache 生成 `angular-style` 风格的 CHANGELOG。

### 学前准备 {docsify-ignore}

请前往学习: 
- https://github.com/tomasbjerre/git-changelog-lib
- https://github.com/skuzzle/semantic-version

> [!NOTE]
> 未来可能会抛弃 `git-changelog-lib`。

### 用法 {docsify-ignore}

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

### 配置 {docsify-ignore}

| 选项 | 说明 |
| ---- | ---- |
| `isUnstable` | 打开 `development-phase`，使得中断性变更仅递增次版本号。默认为 `false`。 |
| `strategy` | 发布策略。可选值：`strict`、`slow`。默认为 `strict`。 |
| `forceNextVersion` | 当版本号未增长时强制递增 `nextVersion`。默认为 `true`。 |
| `longDateFormat` | 长日期格式。默认为 `yyyy-MM-dd HH:mm:ss`。 |
| `shortDateFormat` | 短日期格式。默认为 `yyyy-MM-dd`。 |
| `lastVersion` | 默认尝试将 `Tag` 转换为版本号。该选项允许您手工指定 `lastVersion` 的值。默认为 `0.1.0`。 |
| `preRelease` | 设置 `preRelease` 的初始值。默认为 `null`。  |
| `buildMetaData` | 设置 `buildMetaData` 的初始值。默认为 `null`。 |
| `majorTypes` | 当匹配到这些提交类型时增加主版本号（major）。默认仅当发现了 **BREAKING CHANGE** 时才生效。 |
| `minorTypes` | 当匹配到这些提交类型时增加次版本号（minor）。默认为 `feat`。 |
| `patchTypes` | 当匹配到这些提交类型时增加修订版本号（patch）。默认为 `refactor,perf,fix,chore,revert,docs,build`。 |
| `preReleaseTypes` | 当匹配到这些提交类型时增加预发布版本号（preRelease）。默认为 `null`。 |
| `buildMetaDataTypes` | 当匹配到这些提交类型时增加构建（元数据）版本号（buildMetaData）。默认为 `null`。 |
| `hiddenTypes` | 这些提交类型将在更新日志中隐藏。默认为 `release`。 |
| `commitLocales` | 包含本地化提交消息的文件集。默认为 `null`。 |
| `jsonFile` | JSON 文件路径。默认为 `null`。 |

> [!TIP]
> 请同时参考：[semantic-commit](/zh-cn/semantic-commit)
