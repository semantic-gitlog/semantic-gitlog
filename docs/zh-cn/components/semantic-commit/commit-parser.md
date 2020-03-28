# CommitParser

用于解析提交消息。

## 用法

<!-- tabs:start -->

### **code**

```java
@Slf4j
public class GitCommitParserTests {
    private CommitParser parser;

    @BeforeEach
    public void init() throws URISyntaxException, ParseException, IOException {
        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        final URL resource = classLoader.getResource("commit-message.md");

        if (resource == null) return;

        final Path path = Paths.get(resource.toURI());

        final Date date = DateUtils.parseDate("2020-02-10 12:23:45", "yyyy-MM-dd HH:mm:ss");
        final GitDate commitTime = GitDate.valueOf(date);
        final GitPersonIdent authorIdent = new GitPersonIdent(commitTime, "ymind", "ymind.chan@yi.team");
        final GitPersonIdent committerIdent = new GitPersonIdent(commitTime, "ymind", "ymind.chan@yi.team");
        final String hashFull = "02ce19bbbf7058f474f760fe4a4447301190dea9";
        final String message = new String(Files.readAllBytes(path), UTF_8).trim();
        final Boolean isMerge = false;

        final GitCommit gitCommit = new GitCommit(hashFull, commitTime, message, isMerge, authorIdent, committerIdent);
        final CommitParserSettings settings = CommitParserSettings.builder().build();

        this.parser = new CommitParser(settings, gitCommit);
    }

    @Test
    public void parseTokensTest() {
        assertNotNull(this.parser, "null");

        parser.parseTokens();
    }

    @Test
    public void parseTest() {
        final ReleaseCommit releaseCommit = this.parser.parse();

        assertNotNull(releaseCommit, "null");

        log.info(releaseCommit.getMessageTitle());
    }
}
```

### **commit-message.md**

```cc
feat!(@team/yi/maven/plugin/mojo): automatically @alice @bob @candy increments Fixes #16851 #16251 #16351 #12854 from-256 earlier versions when the `nextVersion` does not grow (#123) (#456) (#789) not grow (#234, #345, #567) xxx

BREAKING CHANGE: The `isPreRelease` option has been renamed to `isUnstable`.

Fixes #16851, #16251, #16351, #12854.

Close #12854.
Close project/#123.
Close username/project/#123.

/close #12854.
/close project/#123.
/Close username/project/#123.

This commit Close #12854.
This commit Close project/#123.
This commit Close username/project/#123.

This commit /close #12854.
This commit /close              project/#123.
This commit /Close username/project/#123.

Prior to this fix #666 was ---------- @ymind @ymind @ymind ------------ impossible to apply a binding to a the ngMessage directive to represent the name of the error.

BREAKING CHANGE: The `ngMessagesInclude` attribute ###### is now # Locales its own directive and that must be placed as a **child** element within the element with the ngMessages directive.

(cherry picked from commit 29eec77)

------------
# Locales
- **[zh-cn]** 当 `nextVersion` 不增长时，从早期版本自动递增
- **[zh-hk]** 當 `nextVersion` 不增長時，從早期版本自動遞增
- **[de-de]** automatisch inkrementiert von früheren Versionen, wenn die `nextVersion` nicht wächst
```

> [!Warning]
> 上述提交内容是为词法解析器、代码高亮等测试而刻意构建的，请勿直接引用。

<!-- tabs:end -->

## 配置

| 选项 | 说明 |
| ---- | ---- |
| `defaultLang` | 提交主题的默认语言。默认为 `en`. |
| `closeIssueActions` | 用于匹配已关闭议题的快捷动作类型。默认为 `close,closes,closed,fix,fixes,fixed,resolve,resolves,resolved`。 |
| `issueUrlTemplate` | 用于构建议题链接的包含 `:issueId` 占位符的字符串。默认为 `null`。 |
| `commitUrlTemplate` | 用于构建提交链接的包含 `:commitId` 占位符的字符串。默认为 `null`。 |
| `mentionUrlTemplate` | 用于构建提名链接的包含 `:username` 占位符的字符串。默认为 `null`。 |
