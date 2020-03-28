# CommitLocaleParser

Used to parse localized mapping files.

## Usage

<!-- tabs:start -->

### **code**

```java
@Slf4j
public class GitCommitLocaleParserTests {
    @Test
    public void parseTest() throws IOException, URISyntaxException {
        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        final URL resource = classLoader.getResource("commit-locales.md");

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

### **commit-locales.md**

```markdown
# Previous commits

> This is useful for you to override the commit history.

- [f765dbb9] fix xxx error
- [fcfe57ae] add changelog page
- [92909a16] update `README.md`
- [630a1a33] feat: add or update badges
```

<!-- tabs:end -->
