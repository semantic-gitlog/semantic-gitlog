package team.yi.tools.semanticgitlog.model;

import de.skuzzle.semantic.Version;
import lombok.*;
import team.yi.tools.semanticcommit.model.GitDate;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class ReleaseTag implements Serializable {
    private static final long serialVersionUID = -3583377058573092174L;

    @EqualsAndHashCode.Include
    @ToString.Include
    private final Version version;
    @ToString.Include
    private final GitDate releaseDate;
    private final List<ReleaseSection> sections;

    public ReleaseTag(final Version version, final GitDate releaseDate, final List<ReleaseSection> sections) {
        this.version = version;
        this.releaseDate = releaseDate;
        this.sections = sections;
    }

    public List<ReleaseSection> getSections() {
        return getSections(ReleaseSections.DEFAULT_ORDER_LIST);
    }

    public List<ReleaseSection> getSections(final List<String> sortedSections) {
        if (sortedSections == null || sortedSections.isEmpty()) throw new IllegalArgumentException("argument 'sortedSections' can not be null.");

        if (sections == null || sections.isEmpty()) return null;

        return sections.stream()
            .filter(x -> x.getCommits() != null)
            .filter(x -> !x.getCommits().isEmpty())
            .sorted((o1, o2) -> {
                if (o2 == null) return -1;

                final String t1 = o1.getTitle();
                final String t2 = o2.getTitle();

                if (t1 == null && t2 == null) {
                    return 0;
                } else if (t1 == null) {
                    return 1;
                } else if (t2 == null) {
                    return -1;
                } else if (t1.compareTo(t2) == 0) {
                    return 0;
                }

                final int i1 = sortedSections.indexOf(t1);
                final int i2 = sortedSections.indexOf(t2);

                return Integer.compare(i1, i2);
            })
            .collect(Collectors.toList());
    }
}
