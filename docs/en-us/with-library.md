# With library

## 1. Add dependency

<!-- tabs:start -->

### **Maven**

```xml
<dependency>
  <groupId>team.yi.tools</groupId>
  <artifactId>semantic-gitlog</artifactId>
  <version>0.3.2</version>
</dependency>
```

### **Gradle**

```groovy
api 'team.yi.tools:semantic-gitlog:0.5.0'
```

<!-- tabs:end -->

## 2. Code sample

```java
@Slf4j
public class GitRepoTests {
    @Test
    public void gitRepoTest() throws IOException {
        final Path path = Paths.get("...");

        final ObjectId fromId;
        final ObjectId toId;
        final List<GitTag> tags;

        try (final GitRepo gitRepo = GitRepo.open(path.toFile())) {
            final String refName = "master";

            fromId = gitRepo.getFromId(null, null);
            toId = gitRepo.getToId(refName, null);
            tags = gitRepo.getTags(fromId, toId, GitlogConstants.DEFAULT_UNTAGGED_NAME);
        }

        for (final GitTag tag : tags) {
            for (final GitCommit commit : tag.getGitCommits()) {
                log.info("{}, {}, {}", commit.getHash7(), commit.getCommitTime(), commit.getMessageTitle());
                log.info("  - {} {} {}", commit.getAuthorIdent().getName(), commit.getAuthorIdent().getEmail(), commit.getAuthorIdent().getWhen());
                log.info("  - {} {} {}", commit.getCommitterIdent().getName(), commit.getCommitterIdent().getEmail(), commit.getCommitterIdent().getWhen());
            }
        }

        log.info("{}, {}", fromId, toId);

        assertNotNull(tags, "tags");
    }
}
```

> [!TIP|label: See more]
> - [semantic-commit](/en-us/semantic-commit)
> - [semantic-gitlog](/en-us/semantic-gitlog)
> - [maven-semantic-gitlog](/en-us/maven-semantic-gitlog)
> - [gradle-semantic-gitlog](/en-us/gradle-semantic-gitlog)
