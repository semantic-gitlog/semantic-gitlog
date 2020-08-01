package team.yi.tools;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.lib.ObjectId;
import org.junit.jupiter.api.Test;
import team.yi.tools.semanticcommit.model.GitCommit;
import team.yi.tools.semanticgitlog.GitlogConstants;
import team.yi.tools.semanticgitlog.git.GitRepo;
import team.yi.tools.semanticgitlog.git.model.GitTag;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
public class GitRepoTests {
    @Test
    public void gitRepoTest() throws IOException {
        final Path path = Paths.get(System.getProperty("user.dir"));

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
