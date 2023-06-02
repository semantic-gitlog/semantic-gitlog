package team.yi.tools;

import de.skuzzle.semantic.Version;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import team.yi.tools.semanticcommit.model.GitCommit;
import team.yi.tools.semanticcommit.model.GitDate;
import team.yi.tools.semanticcommit.model.GitPersonIdent;
import team.yi.tools.semanticcommit.model.ReleaseCommit;
import team.yi.tools.semanticcommit.parser.CommitParser;
import team.yi.tools.semanticgitlog.VersionDeriver;
import team.yi.tools.semanticgitlog.VersionStrategy;
import team.yi.tools.semanticgitlog.config.GitlogSettings;

import java.text.ParseException;
import java.util.Stack;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class VersionDeriverTests {
    @ParameterizedTest
    @EnumSource(VersionStrategy.class)
    public void deduceNext(final VersionStrategy strategy) throws ParseException {
        final GitlogSettings settings = GitlogSettings.builder()
            .strategy(strategy)
            .build();
        final ReleaseCommitStack commits = new ReleaseCommitStack(settings);

        // git log --full-history --all --color --date-order --reverse --date=iso8601 --pretty=format:"\"%ad\", \"%s\""

        // @formatter:off

        // // -- after bugfix -----------------------------
        // // strict  slow  --
        // /* 3.0.0   2.0.0 */ commits.add("2020-09-01 22:29:16", "fix!: bugfix 2\nBREAKING CHANGE: had to...");
        // /* 2.0.0   2.0.0 */ commits.add("2020-08-31 22:29:16", "fix: bugfix 1\nBREAKING CHANGE: had to...");
        // /* 1.0.1   1.0.1 */ commits.add("2020-08-31 21:29:16", "fix: bugfix 0");
        //
        // // strict  slow  --
        // /* 3.0.0   2.0.0 */ commits.add("2020-09-01 22:29:16", "fix!: bugfix 2\nBREAKING CHANGE: had to...");
        // /* 2.0.0   2.0.0 */ commits.add("2020-08-31 22:29:16", "fix: bugfix 1\nBREAKING CHANGE: had to...");
        // /* 1.0.1   1.0.1 */ commits.add("2020-08-31 21:29:16", "build(deps): bump checkstyle from 8.30 to 8.35");
        //
        // // -- before bugfix ----------------------------
        // // strict  slow  --
        // /* 3.0.0   1.0.1 */ commits.add("2020-09-01 22:29:16", "fix!: bugfix 2\nBREAKING CHANGE: had to...");
        // /* 2.0.0   1.0.1 */ commits.add("2020-08-31 22:29:16", "fix: bugfix 1\nBREAKING CHANGE: had to...");
        // /* 1.0.1   1.0.1 */ commits.add("2020-08-31 21:29:16", "fix: bugfix 0");
        //
        // // strict  slow  --
        // /* 3.0.0   2.0.0 */ commits.add("2020-09-01 22:29:16", "fix!: bugfix 2\nBREAKING CHANGE: had to...");
        // /* 2.0.0   2.0.0 */ commits.add("2020-08-31 22:29:16", "fix: bugfix 1\nBREAKING CHANGE: had to...");
        // /* 1.0.1   1.0.1 */ commits.add("2020-08-31 21:29:16", "build(deps): bump checkstyle from 8.30 to 8.35");

        // ---------------------------------------------------------------------------

        /* 5.0.0   2.0.1 */ commits.add("2020-09-04 22:29:16", "fix!: bugfix 13\n\nBREAKING CHANGE: had to...");
        /* 4.0.0   2.0.1 */ commits.add("2020-09-03 22:29:16", "fix: bugfix 12\n\nBREAKING CHANGE: had to...");
        /* 3.0.1   2.0.1 */ commits.add("2020-09-02 21:29:16", "fix: bugfix 11");

        /* 3.0.0   2.0.0 */ commits.add("2020-09-01 22:29:16", "fix!: bugfix 2\nBREAKING CHANGE: had to...");
        /* 2.0.0   2.0.0 */ commits.add("2020-08-31 22:29:16", "fix: bugfix 1\nBREAKING CHANGE: had to...");
        /* 1.2.2   1.2.1 */ commits.add("2020-08-31 21:29:16", "build(deps): bump checkstyle from 8.30 to 8.35");

        /* 1.2.1   1.2.1 */ commits.add("2020-08-30 22:29:16", "release: 0.6.1");
        /* 1.2.1   1.2.1 */ commits.add("2020-08-30 22:26:21", "build(deps): bump team.yi.semantic-gitlog from 0.5.3 to 0.5.12");
        /* 1.2.0   1.2.0 */ commits.add("2020-08-29 23:16:15", "feat(config): implement option `strategy`");
        /* 1.1.11  1.1.3 */ commits.add("2020-08-01 15:52:15", "release: 0.5.11");
        /* 1.1.11  1.1.3 */ commits.add("2020-08-01 15:49:33", "refactor(render): display scope.name when scope.displayName is empty");
        /* 1.1.10  1.1.2 */ commits.add("2020-08-01 15:11:33", "ci(github): add github workflows");
        /* 1.1.10  1.1.2 */ commits.add("2020-03-28 20:41:28", "docs(docs): update docs");
        /* 1.1.9   1.1.1 */ commits.add("2020-08-01 14:49:28", "style(editorconfig): update .editorconfig, disable .mustache file indent");
        /* 1.1.9   1.1.1 */ commits.add("2020-08-01 14:46:57", "build(deps): bump gradle-wrapper from 6.3 to 6.5.1");
        /* 1.1.8   1.1.1 */ commits.add("2020-08-01 14:43:39", "build(deps): bump semantic-commit from 0.5.0 to 0.5.7");
        /* 1.1.7   1.1.1 */ commits.add("2020-08-01 14:39:27", "build(deps): bump pmd from 6.22.0 to 6.26.0");
        /* 1.1.6   1.1.1 */ commits.add("2020-08-01 12:09:25", "build(deps): bump checkstyle from 8.30 to 8.35");
        /* 1.1.5   1.1.1 */ commits.add("2020-07-16 21:23:51", "build(deps): bump org.eclipse.jgit");
        /* 1.1.4   1.1.1 */ commits.add("2020-06-25 21:22:21", "build(deps): bump jackson-databind from 2.10.3 to 2.11.1");
        /* 1.1.3   1.1.1 */ commits.add("2020-05-18 14:17:30", "build(deps): bump junit-jupiter from 5.6.1 to 5.6.2");
        /* 1.1.2   1.1.1 */ commits.add("2020-05-18 14:16:46", "build(deps): bump team.yi.semantic-gitlog from 0.4.1.1 to 0.5.3");
        /* 1.1.1   1.1.1 */ commits.add("2020-05-18 14:16:25", "build(deps): bump io.freefair.lombok from 4.1.6 to 5.1.0");
        /* 1.1.0   1.1.0 */ commits.add("2020-03-28 18:03:17", "release: 0.5.0");
        /* 1.1.0   1.1.0 */ commits.add("2020-03-28 16:50:44", "feat(service): implement primary features and challenges");
        // @formatter:on

        final VersionDeriver deriver = new VersionDeriver(settings);
        final Version lastVersion = Version.create(1, 0, 0);
        final Version version = deriver.deduceNext(lastVersion, commits);
        final Version expectedVersion = settings.getStrategy() == VersionStrategy.slow
            ? Version.create(2, 0, 1)
            : Version.create(5, 0, 0);

        log.info("version: {}, expectedVersion: {}", version, expectedVersion);

        assertEquals(expectedVersion, version);
    }

    public static class ReleaseCommitStack extends Stack<ReleaseCommit> {
        private static final long serialVersionUID = 6436024715746670200L;

        private final GitlogSettings settings;

        public ReleaseCommitStack(final GitlogSettings settings) {
            super();

            this.settings = settings;
        }

        public void add(final String date, final String message) throws ParseException {
            final String hashFull = UUID.randomUUID().toString().replace("-", "");

            add(hashFull, date, message);
        }

        public void add(final String hashFull, final String date, final String message) throws ParseException {
            final GitDate commitTime = new GitDate(DateUtils.parseDate(date, "yyyy-MM-dd HH:mm:ss"));

            add(hashFull, commitTime, message);
        }

        public void add(final String hashFull, final GitDate commitTime, final String message) {
            final ReleaseCommit item = createReleaseCommit(hashFull, commitTime, message);

            super.add(item);
        }

        private ReleaseCommit createReleaseCommit(final String hashFull, final GitDate commitTime, final String message) {
            final GitPersonIdent authorIdent = new GitPersonIdent(commitTime, "ymind", "ymind@yi.team");
            final GitPersonIdent committerIdent = new GitPersonIdent(commitTime, "ymind", "ymind@yi.team");

            final GitCommit gitCommit = new GitCommit(hashFull, commitTime, message, false, authorIdent, committerIdent);
            final CommitParser commitParser = new CommitParser(this.settings, gitCommit);

            return commitParser.parse();
        }
    }
}
