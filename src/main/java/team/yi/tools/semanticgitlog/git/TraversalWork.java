package team.yi.tools.semanticgitlog.git;

import org.eclipse.jgit.revwalk.RevCommit;

import java.util.Objects;

public class TraversalWork implements Comparable<TraversalWork> {
    private final RevCommit to;
    private final String currentTagName;

    public TraversalWork(final RevCommit to, final String currentTagName) {
        this.to = to;
        this.currentTagName = currentTagName;
    }

    public String getCurrentTagName() {
        return currentTagName;
    }

    public RevCommit getTo() {
        return to;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((currentTagName == null) ? 0 : currentTagName.hashCode());
        result = prime * result + ((to == null) ? 0 : to.hashCode());

        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        final TraversalWork other = (TraversalWork) obj;

        if (currentTagName == null) {
            if (other.currentTagName != null) return false;
        } else if (!currentTagName.equals(other.currentTagName)) {
            return false;
        }

        return Objects.equals(to, other.to);
    }

    @Override
    public String toString() {
        return "TraversalWork [to=" + to + ", currentTagName=" + currentTagName + "]";
    }

    @Override
    public int compareTo(final TraversalWork o) {
        final int otherCommitTime = o.getTo().getCommitTime();
        final int compareTo = compareTo(to.getCommitTime(), otherCommitTime);

        return compareTo == 0
            ? (to.getName() + currentTagName).compareTo(o.getTo().getName() + o.getCurrentTagName())
            : compareTo;
    }

    private int compareTo(final int selfCommitTime, final int otherCommitTime) {
        return Integer.compare(selfCommitTime, otherCommitTime);
    }
}
