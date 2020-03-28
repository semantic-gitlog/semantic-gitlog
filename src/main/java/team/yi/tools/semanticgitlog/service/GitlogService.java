package team.yi.tools.semanticgitlog.service;

import de.skuzzle.semantic.Version;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.lib.ObjectId;
import team.yi.tools.semanticcommit.model.GitCommit;
import team.yi.tools.semanticcommit.model.GitDate;
import team.yi.tools.semanticcommit.model.ReleaseCommit;
import team.yi.tools.semanticcommit.model.ReleaseCommitLocale;
import team.yi.tools.semanticcommit.parser.CommitParser;
import team.yi.tools.semanticgitlog.VersionDeriver;
import team.yi.tools.semanticgitlog.config.GitlogSettings;
import team.yi.tools.semanticgitlog.git.GitRepo;
import team.yi.tools.semanticgitlog.git.model.GitTag;
import team.yi.tools.semanticgitlog.model.ReleaseLog;
import team.yi.tools.semanticgitlog.model.ReleaseSection;
import team.yi.tools.semanticgitlog.model.ReleaseSections;
import team.yi.tools.semanticgitlog.model.ReleaseTag;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class GitlogService {
    private final GitlogSettings settings;
    private final Stack<ReleaseCommit> versionCommits = new Stack<>();
    private final VersionDeriver versionDeriver;
    private final CommitLocaleService commitLocaleService;
    private final GitRepo gitRepo;

    public GitlogService(final GitlogSettings settings, final GitRepo gitRepo, final CommitLocaleService commitLocaleService) {
        this.settings = settings;
        this.gitRepo = gitRepo;

        this.versionDeriver = new VersionDeriver(this.settings);
        this.commitLocaleService = commitLocaleService;
    }

    public ReleaseLog generate() {
        this.versionCommits.clear();

        final List<ReleaseTag> releaseTags = new ArrayList<>();
        final ObjectId fromId = this.gitRepo.getFromId(this.settings.getFromRef(), this.settings.getFromCommit());
        final ObjectId toId = this.gitRepo.getToId(this.settings.getToRef(), this.settings.getToCommit());
        final List<GitTag> tags = this.gitRepo.getTags(fromId, toId, this.settings.getUntaggedName());
        ReleaseTag releaseTag = null;
        Version lastVersion = null;

        for (final GitTag tag : tags) {
            final Version tagVersion = Version.isValidVersion(tag.getName())
                ? Version.parseVersion(tag.getName(), true)
                : null;

            if (lastVersion == null) lastVersion = tagVersion;

            final ReleaseTag section = this.processTag(tag, tagVersion, lastVersion);

            if (releaseTag == null
                || releaseTag.getVersion() == null
                || section.getVersion() == null
                || Version.compare(releaseTag.getVersion(), section.getVersion()) != 0) {
                releaseTag = section;
            }

            releaseTags.add(releaseTag);
        }

        if (lastVersion == null) lastVersion = this.settings.getLastVersion();

        final Version nextVersion = this.versionDeriver.deduceNext(lastVersion, this.versionCommits);

        return new ReleaseLog(nextVersion, lastVersion, releaseTags);
    }

    private ReleaseTag processTag(final GitTag tag, final Version tagVersion, final Version lastVersion) {
        final GitDate releaseDate = tag.getTagTime();
        final List<ReleaseSection> groups = this.getGroups(tag, lastVersion);

        return new ReleaseTag(tagVersion, releaseDate, groups);
    }

    private List<ReleaseSection> getGroups(final GitTag tag, final Version lastVersion) {
        final Map<String, List<ReleaseCommit>> map = new ConcurrentHashMap<>();

        for (final GitCommit item : tag.getGitCommits()) {
            final ReleaseCommit commit;

            try {
                final CommitParser commitParser = new CommitParser(this.settings, item);

                commit = commitParser.parse();
            } catch (final Exception ignore) {
                continue;
            }

            if (commit == null || StringUtils.isEmpty(commit.getCommitSubject())) continue;

            final List<ReleaseCommitLocale> commitLocales = this.commitLocaleService.findAll(commit.getHashFull());

            commit.getLocales().addAll(commitLocales);

            if (lastVersion == null) this.versionCommits.add(commit);

            if (this.settings.getHiddenTypes().contains(commit.getCommitType())) continue;

            final String groupTitle = ReleaseSections.fromCommitType(commit.getCommitType(), commit.isBreakingChange());

            if (map.containsKey(groupTitle)) {
                map.get(groupTitle).add(commit);
            } else {
                final List<ReleaseCommit> releaseCommits = new ArrayList<>();
                releaseCommits.add(commit);

                map.put(groupTitle, releaseCommits);
            }
        }

        final List<ReleaseSection> commitGroups = new ArrayList<>();

        map.forEach((key, value) -> {
            final ReleaseSection releaseSection = new ReleaseSection(key, value);

            commitGroups.add(releaseSection);
        });

        return commitGroups;
    }
}
