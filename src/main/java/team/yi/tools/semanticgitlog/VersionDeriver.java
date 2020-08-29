package team.yi.tools.semanticgitlog;

import de.skuzzle.semantic.Version;
import lombok.extern.slf4j.Slf4j;
import team.yi.tools.semanticcommit.model.ReleaseCommit;
import team.yi.tools.semanticgitlog.config.GitlogSettings;

import java.util.*;
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
    private final VersionStrategies strategy;

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
        this.strategy = settings.getStrategy() == null ? VersionStrategies.strict : settings.getStrategy();
    }

    @SuppressWarnings({"PMD.NcssCount", "PMD.NPathComplexity"})
    public Version deduceNext(final Version lastVersion, final Stack<ReleaseCommit> versionCommits) {
        Version nextVersion = lastVersion == null
            ? GitlogConstants.INITIAL_VERSION
            : Version.parseVersion(lastVersion.toString(), true);

        // in slow strategy, same type grow only once. beaking changes are grow only once too.
        nextVersion = this.strategy == VersionStrategies.slow
            ? deduceNextSlow(nextVersion, versionCommits)
            : deduceNextStrict(nextVersion, versionCommits);

        final Version version = VersionUtils.ensureSuffix(nextVersion, this.preRelease, this.buildMetaData);

        return this.forceNextVersion
            ? VersionUtils.ensureNextVersion(version, lastVersion)
            : version;
    }

    private Version deduceNextSlow(final Version version, final Stack<ReleaseCommit> versionCommits) {
        final Map<String, Boolean> changeMap = new ConcurrentHashMap<>();
        Version nextVersion = version;

        while (!versionCommits.isEmpty()) {
            final ReleaseCommit commit = versionCommits.pop();
            final String commitType = commit.getCommitType();

            if (changeMap.containsKey(commitType)) continue;

            nextVersion = VersionUtils.ensureSuffix(nextVersion, this.preRelease, this.buildMetaData);

            if (log != null && log.isDebugEnabled()) {
                log.debug("#");
                log.debug("#  messageTitle: {}", commit.getMessageTitle());
                log.debug("#    commitType: {}", commitType);
                log.debug("#   nextVersion: {}", nextVersion);
                log.debug("#    preRelease: {}", preRelease);
                log.debug("# buildMetaData: {}", buildMetaData);
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
            } else continue;

            changeMap.putIfAbsent(commitType, true);
        }

        return nextVersion;
    }

    private Version deduceNextStrict(final Version version, final Stack<ReleaseCommit> versionCommits) {
        Version nextVersion = version;

        while (!versionCommits.isEmpty()) {
            final ReleaseCommit commit = versionCommits.pop();
            final String commitType = commit.getCommitType();

            nextVersion = VersionUtils.ensureSuffix(nextVersion, this.preRelease, this.buildMetaData);

            if (log != null && log.isDebugEnabled()) {
                log.debug("#");
                log.debug("#  messageTitle: {}", commit.getMessageTitle());
                log.debug("#    commitType: {}", commitType);
                log.debug("#   nextVersion: {}", nextVersion);
                log.debug("#    preRelease: {}", preRelease);
                log.debug("# buildMetaData: {}", buildMetaData);
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
