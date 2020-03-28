# CommitLocaleParser

用于解析提交消息的本地化映射文件。

## 用法

<!-- tabs:start -->

### **code**

```java
@Slf4j
public class GitCommitLocaleParserTests {
    @Test
    public void parseTest() throws IOException, URISyntaxException {
        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        final URL resource = classLoader.getResource("commit-locales.zh-cn.md");

        if (resource == null) return;

        final Path path = Paths.get(resource.toURI());
        final File file = path.toFile();
        final CommitLocaleParser parser = new CommitLocaleParser("zh-cn", file);

        final List<ReleaseCommitLocale> commitLocales = parser.parse();

        assertNotNull(commitLocales, "null");

        log.info(String.valueOf(commitLocales.size()));
    }
}
```

### **commit-locales.zh-cn.md**

```markdown
# Previous commits

> This is useful for you to override the commit history.

- [4ef660df] feat: 添加 `forceNextVersion` 配置项
- [8d964149] 实现主要功能
- [8d964169] perf: xxxx
- [da97c904] chore: 实现主要功能
```

<!-- tabs:end -->
