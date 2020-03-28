package team.yi.tools.semanticgitlog;

import de.skuzzle.semantic.Version;
import lombok.extern.slf4j.Slf4j;
import team.yi.tools.semanticcommit.model.ReleaseCommit;
import team.yi.tools.semanticgitlog.config.GitlogSettings;

import java.util.List;
import java.util.Stack;

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
    }

    @SuppressWarnings({"PMD.NcssCount", "PMD.NPathComplexity"})
    public Version deduceNext(final Version lastVersion, final Stack<ReleaseCommit> versionCommits) {
        Version nextVersion = lastVersion == null
            ? GitlogConstants.INITIAL_VERSION
            : Version.parseVersion(lastVersion.toString(), true);
        boolean minorUp = false;
        boolean patchUp = false;
        boolean preReleaseUp = false;
        boolean buildMetaDataUp = false;

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
                if (this.isUnstable) {
                    nextVersion = nextVersion.nextMinor();
                    minorUp = true;
                } else {
                    nextVersion = nextVersion.nextMajor();
                }
            } else if (this.majorTypes.contains(commitType)) {
                nextVersion = nextVersion.nextMajor();
            } else if (this.minorTypes.contains(commitType)) {
                nextVersion = nextVersion.nextMinor();

                if (!minorUp) minorUp = true;
            } else if (this.patchTypes.contains(commitType)) {
                nextVersion = nextVersion.nextPatch();

                if (!patchUp) patchUp = true;
            } else if (this.preReleaseTypes.contains(commitType)) {
                nextVersion = nextVersion.nextPreRelease();

                if (!preReleaseUp) preReleaseUp = true;
            } else if (this.buildMetaDataTypes.contains(commitType)) {
                nextVersion = nextVersion.nextBuildMetaData();

                if (!buildMetaDataUp) buildMetaDataUp = true;
            }
        }

        final Version version = VersionUtils.ensureSuffix(nextVersion, this.preRelease, this.buildMetaData);

        return this.forceNextVersion
            ? VersionUtils.ensureNextVersion(version, lastVersion)
            : version;
    }
}
