package team.yi.tools.semanticgitlog.model;

import de.skuzzle.semantic.Version;
import lombok.Getter;
import lombok.ToString;
import team.yi.tools.semanticcommit.model.GitDate;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@ToString(onlyExplicitlyIncluded = true)
public class ReleaseLog implements Serializable {
    public static final int VERSION = 1;

    private static final long serialVersionUID = -8269453799524252579L;

    @ToString.Include
    private final Version nextVersion;
    @ToString.Include
    private final Version lastVersion;
    private final List<ReleaseTag> tags;

    public ReleaseLog(final Version nextVersion, final Version lastVersion, final List<ReleaseTag> tags) {
        this.nextVersion = nextVersion;
        this.lastVersion = lastVersion;
        this.tags = tags;
    }

    public List<ReleaseTag> getTags() {
        if (tags == null || tags.isEmpty()) return null;

        return tags.stream()
            .distinct()
            .sorted(Comparator.nullsFirst((Comparator<ReleaseTag>) (o1, o2) -> {
                if (o2 == null) return -1;

                final GitDate t1 = o1.getReleaseDate();
                final GitDate t2 = o2.getReleaseDate();

                if (t1 == null && t2 == null) {
                    return 0;
                } else if (t1 == null) {
                    return 1;
                } else if (t2 == null) {
                    return -1;
                }

                return t1.compareTo(t2);
            }).reversed())
            .collect(Collectors.toList());
    }
}
