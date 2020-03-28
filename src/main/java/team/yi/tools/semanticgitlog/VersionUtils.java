package team.yi.tools.semanticgitlog;

import de.skuzzle.semantic.Version;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

@UtilityClass
public class VersionUtils {
    public static Version ensureNextVersion(final Version nextVersion, final Version lastVersion) {
        final Version v1 = nextVersion == null ? GitlogConstants.INITIAL_VERSION : nextVersion;
        final Version v2 = lastVersion == null ? GitlogConstants.INITIAL_VERSION : lastVersion;
        final int compareValue = Version.compareWithBuildMetaData(v1, v2);

        Version version = v1;

        // nextVersion > lastVersion
        if (compareValue <= 0) {
            if (v2.hasBuildMetaData()) {
                version = v2.nextBuildMetaData();
            } else if (v2.isPreRelease()) {
                version = v2.nextPreRelease();
            } else if (v2.getPatch() > 0) {
                version = v2.nextPatch();
            } else if (v2.getMinor() > 0) {
                version = v2.nextMinor();
            } else if (v2.getMajor() > 0) {
                version = v2.nextMajor();
            } else {
                version = v2.nextPatch();
            }
        }

        if (v1.hasBuildMetaData()) version = version.withBuildMetaData(v1.getBuildMetaData());
        if (v1.isPreRelease()) version = version.withPreRelease(v1.getPreRelease());

        return version;
    }

    public Version ensureSuffix(final Version nextVersion, final String defaultPreRelease, final String defaultBuildMetaData) {
        Version version = nextVersion;

        final String preRelease = StringUtils.defaultIfEmpty(version.getPreRelease(), defaultPreRelease);
        final String buildMetaData = StringUtils.defaultIfEmpty(version.getBuildMetaData(), defaultBuildMetaData);

        if (StringUtils.isNotEmpty(preRelease)) version = version.withPreRelease(preRelease);
        if (StringUtils.isNotEmpty(buildMetaData)) version = version.withBuildMetaData(buildMetaData);

        return version;
    }
}
