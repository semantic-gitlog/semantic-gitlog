package team.yi.tools.semanticgitlog.config;

import de.skuzzle.semantic.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import team.yi.tools.semanticcommit.parser.CommitParserSettings;
import team.yi.tools.semanticgitlog.GitlogConstants;
import team.yi.tools.semanticgitlog.VersionStrategies;

import java.io.File;
import java.util.*;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@SuppressWarnings("PMD.TooManyFields")
public class GitlogSettings extends CommitParserSettings {
    private static final long serialVersionUID = 4854964815370323001L;

    protected String fromRef;
    protected String fromCommit;
    protected String toRef;
    protected String toCommit;

    private Map<String, File> commitLocales;
    private Map<String, File> scopeProfiles;

    private String untaggedName;
    private Boolean isUnstable;
    private VersionStrategies strategy;
    private Boolean forceNextVersion;

    private Version lastVersion;
    private String preRelease;
    private String buildMetaData;
    private String majorTypes;
    private String minorTypes;
    private String patchTypes;
    private String preReleaseTypes;
    private String buildMetaDataTypes;
    private String hiddenTypes;

    public Boolean getForceNextVersion() {
        return BooleanUtils.toBooleanDefaultIfNull(this.forceNextVersion, true);
    }

    public Boolean getIsUnstable() {
        return BooleanUtils.toBooleanDefaultIfNull(this.isUnstable, false);
    }

    public String getUntaggedName() {
        return StringUtils.defaultIfBlank(this.untaggedName, GitlogConstants.DEFAULT_UNTAGGED_NAME);
    }

    public List<String> getMajorTypes() {
        final String[] items = StringUtils.split(this.majorTypes, ",");

        return items == null || items.length == 0 ? GitlogConstants.DEFAULT_MAJOR_TYPES : Arrays.asList(items);
    }

    public List<String> getMinorTypes() {
        final String[] items = StringUtils.split(this.minorTypes, ",");

        return items == null || items.length == 0 ? GitlogConstants.DEFAULT_MINOR_TYPES : Arrays.asList(items);
    }

    public List<String> getPatchTypes() {
        final String[] items = StringUtils.split(this.patchTypes, ",");

        return items == null || items.length == 0 ? GitlogConstants.DEFAULT_PATCH_TYPES : Arrays.asList(items);
    }

    public List<String> getPreReleaseTypes() {
        final String[] items = StringUtils.split(this.preReleaseTypes, ",");

        return items == null || items.length == 0 ? GitlogConstants.DEFAULT_PRE_RELEASE_TYPES : Arrays.asList(items);
    }

    public List<String> getBuildMetaDataTypes() {
        final String[] items = StringUtils.split(this.buildMetaDataTypes, ",");

        return items == null || items.length == 0 ? GitlogConstants.DEFAULT_BUILD_META_DATA_TYPES : Arrays.asList(items);
    }

    public List<String> getHiddenTypes() {
        final String[] items = StringUtils.split(this.hiddenTypes, ",");

        return items == null || items.length == 0 ? GitlogConstants.DEFAULT_HIDDEN_TYPES : Arrays.asList(items);
    }
}
