package team.yi.tools.semanticgitlog.git.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import team.yi.tools.semanticcommit.model.GitCommit;
import team.yi.tools.semanticcommit.model.GitDate;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class GitTag implements Serializable {
    private static final long serialVersionUID = -6853375736888618141L;

    @EqualsAndHashCode.Include
    @ToString.Include
    private final String name;
    private final String annotation;
    @ToString.Include
    private final GitDate tagTime;
    private final List<GitCommit> gitCommits;
    private final boolean hasTagTime;

    public GitTag(final String name, final String annotation, final GitDate tagTime, final List<GitCommit> gitCommits) {
        this.name = Objects.requireNonNull(name, "name");
        this.annotation = annotation;
        this.tagTime = tagTime;
        this.gitCommits = Objects.requireNonNull(gitCommits, "gitCommits");
        this.hasTagTime = tagTime != null;
    }

    public GitCommit getCommit() {
        if (this.gitCommits.isEmpty()) return null;

        return this.gitCommits.get(0);
    }
}
