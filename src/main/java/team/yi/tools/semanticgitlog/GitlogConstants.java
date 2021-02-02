package team.yi.tools.semanticgitlog;

import de.skuzzle.semantic.Version;

import java.util.*;

public final class GitlogConstants {
    public static final Version INITIAL_VERSION = Version.create(0, 1, 0);
    public static final String DEFAULT_TIMEZONE = "UTC";
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String COMMIT_ZERO = "0000000000000000000000000000000000000000";
    public static final String DEFAULT_UNTAGGED_NAME = "Unreleased";

    public static final String BREAKING_CHANGE_TYPE = "BREAKING CHANGE";

    public static final List<String> DEFAULT_MAJOR_TYPES = new ArrayList<>();
    public static final List<String> DEFAULT_MINOR_TYPES = Collections.singletonList("feat");
    public static final List<String> DEFAULT_PATCH_TYPES = Arrays.asList("refactor", "perf", "fix", "chore", "revert", "docs", "build");
    public static final List<String> DEFAULT_PRE_RELEASE_TYPES = new ArrayList<>();
    public static final List<String> DEFAULT_BUILD_META_DATA_TYPES = new ArrayList<>();
    public static final List<String> DEFAULT_HIDDEN_TYPES = Collections.singletonList("release");
    public static final String DEFAULT_TAG_PATTERN = "^(0|[1-9]\\d*)\\.(0|[1-9]\\d*)\\.(0|[1-9]\\d*)(?:-((?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*)"
        + "(?:\\.(?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*))*))?(?:\\+([0-9a-zA-Z-]+(?:\\.[0-9a-zA-Z-]+)*))?$";
}
