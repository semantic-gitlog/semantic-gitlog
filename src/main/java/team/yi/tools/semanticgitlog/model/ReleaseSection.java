package team.yi.tools.semanticgitlog.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import team.yi.tools.semanticcommit.model.ReleaseCommit;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class ReleaseSection implements Serializable {
    private static final long serialVersionUID = -8070362801447560940L;

    @EqualsAndHashCode.Include
    @ToString.Include
    private final String title;
    private final List<ReleaseCommit> commits;

    public ReleaseSection(final String title, final List<ReleaseCommit> commits) {
        this.title = title;
        this.commits = commits;
    }

    public List<ReleaseCommit> getCommits() {
        if (commits == null || commits.isEmpty()) return null;

        return commits.stream()
            .distinct()
            .sorted(Comparator.comparing(ReleaseCommit::getCommitScope, Comparator.nullsLast(String::compareTo))
                .thenComparing(ReleaseCommit::getCommitTimeLong))
            .collect(Collectors.toList());
    }
}
