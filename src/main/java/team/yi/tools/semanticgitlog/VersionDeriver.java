package team.yi.tools.semanticgitlog;

import de.skuzzle.semantic.Version;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import team.yi.tools.semanticcommit.model.ReleaseCommit;
import team.yi.tools.semanticgitlog.config.GitlogSettings;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class VersionDeriver {
    private final List<String> majorTypes;
    private final List<String> minorTypes;
    private final List<String> patchTypes;
    private final List<String> preReleaseTypes;
    private final List<String> buildMetaDataTypes;
    private final String preRelease;
    private final String buildMetaData;
    private final boolean forceNextVersion;
    private final boolean isUnstable;
    private final VersionStrategy strategy;

    public VersionDeriver(final GitlogSettings settings) {
        this.majorTypes = settings.getMajorTypes();
        this.minorTypes = settings.getMinorTypes();
        this.patchTypes = settings.getPatchTypes();
        this.preReleaseTypes = settings.getPreReleaseTypes();
        this.buildMetaDataTypes = settings.getBuildMetaDataTypes();
        this.preRelease = settings.getPreRelease();
        this.buildMetaData = settings.getBuildMetaData();
        this.forceNextVersion = settings.getForceNextVersion();
        this.isUnstable = settings.getIsUnstable();
        this.strategy = settings.getStrategy() == null ? VersionStrategy.strict : settings.getStrategy();
    }

    public Version deduceNext(final Version lastVersion, final Stack<ReleaseCommit> versionCommits) {
        Version nextVersion = lastVersion == null
            ? GitlogConstants.INITIAL_VERSION
            : Version.parseVersion(lastVersion.toString(), true);

        // in slow strategy, same type grow only once. beaking changes are grow only once too.
        nextVersion = this.strategy == VersionStrategy.slow
            ? deduceNextSlow(nextVersion, versionCommits)
            : deduceNextStrict(nextVersion, versionCommits);

        final Version version = VersionUtils.ensureSuffix(nextVersion, this.preRelease, this.buildMetaData);

        return this.forceNextVersion
            ? VersionUtils.ensureNextVersion(version, lastVersion)
            : version;
    }

    private Version deduceNextSlow(final Version version, final Stack<ReleaseCommit> versionCommits) {
        final List<String> versionParts = Arrays.asList("major", "minor", "patch", "preRelease", "buildMetaData");
        final Map<String, Set<String>> changeMap = new ConcurrentHashMap<>();
        changeMap.putIfAbsent(versionParts.get(0), new HashSet<>());
        changeMap.putIfAbsent(versionParts.get(1), new HashSet<>());
        changeMap.putIfAbsent(versionParts.get(2), new HashSet<>());
        changeMap.putIfAbsent(versionParts.get(3), new HashSet<>());
        changeMap.putIfAbsent(versionParts.get(4), new HashSet<>());

        Version nextVersion = version;

        while (!versionCommits.isEmpty()) {
            final ReleaseCommit commit = versionCommits.pop();
            final String commitType = commit.getCommitType();

            if (StringUtils.isEmpty(commitType)) continue;

            nextVersion = VersionUtils.ensureSuffix(nextVersion, this.preRelease, this.buildMetaData);

            if (log != null && log.isDebugEnabled()) {
                log.debug("#");
                log.debug("#     messageTitle: {}", commit.getMessageTitle());
                log.debug("#       commitType: {}, {}", commitType, commit.isBreakingChange());
                log.debug("#      nextVersion: {}, {}, {}", nextVersion, preRelease, buildMetaData);
                log.debug("#");
            }

            if (commit.isBreakingChange()) {
                if (changeMap.get(versionParts.get(0)).contains(GitlogConstants.BREAKING_CHANGE_TYPE)) continue;

                nextVersion = this.isUnstable ? nextVersion.nextMinor() : nextVersion.nextMajor();

                changeMap.get(versionParts.get(0)).add(GitlogConstants.BREAKING_CHANGE_TYPE);
                changeMap.get(versionParts.get(1)).clear();
                changeMap.get(versionParts.get(2)).clear();
                changeMap.get(versionParts.get(3)).clear();
                changeMap.get(versionParts.get(4)).clear();
            } else if (this.majorTypes.contains(commitType)) {
                if (changeMap.get(versionParts.get(0)).contains(commitType)) continue;

                nextVersion = nextVersion.nextMajor();

                changeMap.get(versionParts.get(0)).add(commitType);
                changeMap.get(versionParts.get(1)).clear();
                changeMap.get(versionParts.get(2)).clear();
                changeMap.get(versionParts.get(3)).clear();
                changeMap.get(versionParts.get(4)).clear();
            } else if (this.minorTypes.contains(commitType)) {
                if (changeMap.get(versionParts.get(1)).contains(commitType)
                    && changeMap.get(versionParts.get(2)).isEmpty()
                    && changeMap.get(versionParts.get(3)).isEmpty()
                    && changeMap.get(versionParts.get(4)).isEmpty()) continue;

                nextVersion = nextVersion.nextMinor();

                changeMap.get(versionParts.get(1)).add(commitType);
                changeMap.get(versionParts.get(2)).clear();
                changeMap.get(versionParts.get(3)).clear();
                changeMap.get(versionParts.get(4)).clear();
            } else if (this.patchTypes.contains(commitType)) {
                if (changeMap.get(versionParts.get(2)).contains(commitType)
                    && changeMap.get(versionParts.get(3)).isEmpty()
                    && changeMap.get(versionParts.get(4)).isEmpty()) continue;

                nextVersion = nextVersion.nextPatch();

                changeMap.get(versionParts.get(2)).add(commitType);
                changeMap.get(versionParts.get(3)).clear();
                changeMap.get(versionParts.get(4)).clear();
            } else if (this.preReleaseTypes.contains(commitType)) {
                if (changeMap.get(versionParts.get(3)).contains(commitType)
                    && changeMap.get(versionParts.get(4)).isEmpty()) continue;

                nextVersion = nextVersion.nextPreRelease();

                changeMap.get(versionParts.get(3)).add(commitType);
                changeMap.get(versionParts.get(4)).clear();
            } else if (this.buildMetaDataTypes.contains(commitType)) {
                if (changeMap.get(versionParts.get(4)).contains(commitType)) continue;

                nextVersion = nextVersion.nextBuildMetaData();

                changeMap.get(versionParts.get(4)).add(commitType);
            }
        }

        return nextVersion;
    }

    private Version deduceNextStrict(final Version version, final Stack<ReleaseCommit> versionCommits) {
        Version nextVersion = version;

        while (!versionCommits.isEmpty()) {
            final ReleaseCommit commit = versionCommits.pop();
            final String commitType = commit.getCommitType();

            if (StringUtils.isEmpty(commitType)) continue;

            nextVersion = VersionUtils.ensureSuffix(nextVersion, this.preRelease, this.buildMetaData);

            if (log != null && log.isDebugEnabled()) {
                log.debug("#");
                log.debug("#     messageTitle: {}", commit.getMessageTitle());
                log.debug("#       commitType: {}, {}", commitType, commit.isBreakingChange());
                log.debug("#      nextVersion: {}, {}, {}", nextVersion, preRelease, buildMetaData);
                log.debug("#");
            }

            if (commit.isBreakingChange()) {
                nextVersion = this.isUnstable ? nextVersion.nextMinor() : nextVersion.nextMajor();
            } else if (this.majorTypes.contains(commitType)) {
                nextVersion = nextVersion.nextMajor();
            } else if (this.minorTypes.contains(commitType)) {
                nextVersion = nextVersion.nextMinor();
            } else if (this.patchTypes.contains(commitType)) {
                nextVersion = nextVersion.nextPatch();
            } else if (this.preReleaseTypes.contains(commitType)) {
                nextVersion = nextVersion.nextPreRelease();
            } else if (this.buildMetaDataTypes.contains(commitType)) {
                nextVersion = nextVersion.nextBuildMetaData();
            }
        }

        return nextVersion;
    }
}
