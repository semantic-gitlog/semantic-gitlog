package team.yi.tools.semanticgitlog.git;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.AnyObjectId;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTag;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import team.yi.tools.semanticcommit.model.GitCommit;
import team.yi.tools.semanticcommit.model.GitDate;
import team.yi.tools.semanticcommit.model.GitPersonIdent;
import team.yi.tools.semanticgitlog.GitlogConstants;
import team.yi.tools.semanticgitlog.git.model.GitTag;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@SuppressWarnings({"PMD.TooManyMethods", "PMD.GodClass"})
@Slf4j
public final class GitRepo implements Closeable {
    private final Repository repository;
    private final RevWalk revWalk;
    private final Git git;
    private List<RevCommit> commitsToInclude;

    private GitRepo(final Repository repository) {
        this.repository = repository;
        this.revWalk = new RevWalk(this.repository);
        this.git = new Git(this.repository);
    }

    public static GitRepo open() throws IOException {
        return open(null);
    }

    public static GitRepo open(final File file) throws IOException {
        final FileRepositoryBuilder builder = new FileRepositoryBuilder().findGitDir(file).readEnvironment();

        return new GitRepo(builder.build());
    }

    private static List<RevCommit> toList(final Iterable<RevCommit> call) {
        final List<RevCommit> items = new ArrayList<>();

        call.forEach(items::add);

        return items;
    }

    private static <T> T getLast(final Iterator<T> iterator) {
        T current;

        do {
            current = iterator.next();
        } while (iterator.hasNext());

        return current;
    }

    @Override
    public void close() {
        this.git.close();
        this.repository.close();
        revWalk.dispose();

        try {
            ((AutoCloseable) revWalk).close();
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public List<RevCommit> getCommits() {
        return getCommits(Constants.HEAD);
    }

    public List<RevCommit> getCommits(final String refName) {
        return getCommits(refName, null);
    }

    public List<RevCommit> getCommits(final String refName, final AnyObjectId fromId) {
        return getCommits(refName, fromId, null);
    }

    public List<RevCommit> getCommits(final String refName, final AnyObjectId fromId, final AnyObjectId toId) {
        final List<RevCommit> commits = new ArrayList<>();

        try {
            final AnyObjectId refId = getRefId(refName);
            final LogCommand logCommand = this.git.log().add(refId);

            if (fromId == null || toId == null) {
                if (fromId != null) {
                    logCommand.add(fromId);
                }
            } else {
                logCommand.addRange(fromId, toId);
            }

            final Iterable<RevCommit> iterable = logCommand.call();

            for (final RevCommit commit : iterable) {
                commits.add(commit);
            }
        } catch (final Exception e) {
            throw new RuntimeException("First commit not found in " + this.repository.getDirectory(), e);
        }

        return commits;
    }

    public List<GitTag> getTags(final AnyObjectId fromId, final AnyObjectId toId, final String untaggedName) {
        final RevCommit from = this.revWalk.lookupCommit(Objects.requireNonNull(fromId, "fromId"));
        final RevCommit to = this.revWalk.lookupCommit(Objects.requireNonNull(toId, "toId"));

        final List<GitTag> tags = new ArrayList<>();

        try {
            this.commitsToInclude = this.getDiffingCommits(from, to);

            final List<Ref> tagList = this.tagsBetweenFromAndTo(from, to);
            final Map<String, Ref> tagPerCommitHash = this.getTagPerCommitHash(tagList);
            final Map<String, String> tagPerCommitsHash = new ConcurrentHashMap<>();
            final Map<String, Set<GitCommit>> commitsPerTag = new ConcurrentHashMap<>();
            final Map<String, GitDate> datePerTag = new ConcurrentHashMap<>();

            this.populateCommit(from, to, tagPerCommitHash, tagPerCommitsHash, commitsPerTag, datePerTag, null);
            this.populateCommit(from, to, tagPerCommitHash, tagPerCommitsHash, commitsPerTag, datePerTag, untaggedName);

            final Map<String, RevTag> annotatedTagPerTagName = this.getAnnotatedTagPerTagName(tagList);

            this.addToTags(commitsPerTag, untaggedName, null, tags, annotatedTagPerTagName);

            final List<Ref> tagCommitHashSortedByCommitTime = this.getTagCommitHashSortedByCommitTime(tagPerCommitHash.values());

            for (final Ref tag : tagCommitHashSortedByCommitTime) {
                this.addToTags(commitsPerTag, tag.getName(), datePerTag.get(tag.getName()), tags, annotatedTagPerTagName);
            }
        } catch (final Exception e) {
            log.debug(e.getMessage(), e);
        }

        return tags;
    }

    private List<Ref> getTagCommitHashSortedByCommitTime(final Collection<Ref> refs) {
        return refs.stream()
            .sorted((o1, o2) -> {
                final RevCommit revCommit1 = this.revWalk.lookupCommit(getPeeled(o1));

                try {
                    this.revWalk.parseHeaders(revCommit1);

                    final RevCommit revCommit2 = this.revWalk.lookupCommit(getPeeled(o2));

                    this.revWalk.parseHeaders(revCommit2);

                    return toGitCommit(revCommit1).compareTo(toGitCommit(revCommit2));
                } catch (final Exception e) {
                    log.debug(e.getMessage(), e);

                    throw new RuntimeException(e);
                }
            })
            .collect(Collectors.toList());
    }

    private void addToTags(
        final Map<String, Set<GitCommit>> commitsPerTag,
        final String tagName,
        final GitDate tagTime,
        final List<GitTag> addTo,
        final Map<String, RevTag> annotatedTagPerTagName
    ) {
        if (!commitsPerTag.containsKey(tagName)) return;

        final List<GitCommit> gitCommits = new ArrayList<>(commitsPerTag.get(tagName));
        gitCommits.sort(Comparator.comparing(GitCommit::getCommitTimeLong).reversed());

        final boolean isAnnotated = annotatedTagPerTagName.containsKey(tagName);
        String annotation = null;

        if (isAnnotated) annotation = annotatedTagPerTagName.get(tagName).getFullMessage();

        final String name = StringUtils.removeStart(tagName, "refs/tags/");
        final GitTag gitTag = new GitTag(name, annotation, tagTime, gitCommits);

        addTo.add(gitTag);
    }

    private Map<String, RevTag> getAnnotatedTagPerTagName(final List<Ref> tagList) throws IOException {
        final Map<String, RevTag> tagPerCommit = new ConcurrentHashMap<>();

        for (final Ref tag : tagList) {
            final Ref peeledTag = this.repository.getRefDatabase().peel(tag);

            if (peeledTag.getPeeledObjectId() != null) {
                try {
                    final RevTag revTag = RevTag.parse(this.repository.open(tag.getObjectId()).getBytes());
                    tagPerCommit.put(tag.getName(), revTag);
                } catch (final IOException e) {
                    log.debug(e.getMessage(), e);
                }
            }
        }

        return tagPerCommit;
    }

    private void populateCommit(
        final RevCommit from,
        final RevCommit to,
        final Map<String, Ref> tagPerCommitHash,
        final Map<String, String> tagPerCommitsHash,
        final Map<String, Set<GitCommit>> commitsPerTag,
        final Map<String, GitDate> datePerTag,
        final String startingTagName
    ) throws IOException {
        final Set<TraversalWork> moreWork = populateCommitPerTag(
            from,
            to,
            tagPerCommitHash,
            tagPerCommitsHash,
            commitsPerTag,
            datePerTag,
            startingTagName
        );

        do {
            final Set<TraversalWork> evenMoreWork = new TreeSet<>();

            for (final TraversalWork tw : new ArrayList<>(moreWork)) {
                moreWork.remove(tw);

                final Set<TraversalWork> newWork = populateCommitPerTag(
                    from,
                    tw.getTo(),
                    tagPerCommitHash,
                    tagPerCommitsHash,
                    commitsPerTag,
                    datePerTag,
                    tw.getCurrentTagName()
                );

                evenMoreWork.addAll(newWork);
            }

            moreWork.addAll(evenMoreWork);
        } while (!moreWork.isEmpty());
    }

    private Set<TraversalWork> populateCommitPerTag(
        final RevCommit from,
        final RevCommit to,
        final Map<String, Ref> tagPerCommitHash,
        final Map<String, String> tagPerCommitsHash,
        final Map<String, Set<GitCommit>> commitsPerTag,
        final Map<String, GitDate> datePerTag,
        final String currentTagName
    ) throws IOException {
        final String thisCommitHash = to.getName();

        if (isMappedToAnotherTag(tagPerCommitsHash, thisCommitHash)) {
            return new TreeSet<>();
        }

        final RevCommit thisCommit = this.revWalk.lookupCommit(to);

        this.revWalk.parseHeaders(thisCommit);

        String tagName = currentTagName;

        if (thisIsANewTag(tagPerCommitHash, thisCommitHash)) {
            tagName = getTagName(tagPerCommitHash, thisCommitHash);
        }

        if (tagName != null) {
            if (addCommitToCurrentTag(commitsPerTag, tagName, thisCommit)) {
                datePerTag.put(tagName, new GitDate(thisCommit.getCommitTime() * 1000L));
            }

            noteThatTheCommitWasMapped(tagPerCommitsHash, tagName, thisCommitHash);
        }

        if (notFirstIncludedCommit(from, to)) {
            final Set<TraversalWork> work = new TreeSet<>();

            for (final RevCommit parent : thisCommit.getParents()) {
                if (shouldInclude(parent)) {
                    work.add(new TraversalWork(parent, tagName));
                }
            }

            return work;
        }

        return new TreeSet<>();
    }

    private boolean addCommitToCurrentTag(
        final Map<String, Set<GitCommit>> commitsPerTagName,
        final String currentTagName,
        final RevCommit thisCommit
    ) {
        final GitCommit gitCommit = toGitCommit(thisCommit);
        boolean newTagFound = false;

        if (!commitsPerTagName.containsKey(currentTagName)) {
            commitsPerTagName.put(currentTagName, new TreeSet<>());

            newTagFound = true;
        }

        final Set<GitCommit> gitCommitsInCurrentTag = commitsPerTagName.get(currentTagName);
        gitCommitsInCurrentTag.add(gitCommit);

        return newTagFound;
    }

    private GitCommit toGitCommit(final RevCommit revCommit) {
        Objects.requireNonNull(revCommit, "revCommit");

        final String hashFull = revCommit.getId().getName();
        final GitDate commitTime = new GitDate(revCommit.getCommitTime() * 1000L);
        final String message = revCommit.getFullMessage();
        final boolean merge = revCommit.getParentCount() > 1;
        final GitPersonIdent authorIdent = this.toGitPersonIdent(revCommit.getAuthorIdent());
        final GitPersonIdent committerIdent = this.toGitPersonIdent(revCommit.getCommitterIdent());

        return new GitCommit(hashFull, commitTime, message, merge, authorIdent, committerIdent);
    }

    private GitPersonIdent toGitPersonIdent(final PersonIdent authorIdent) {
        final GitDate when = new GitDate(authorIdent.getWhen());
        final String name = authorIdent.getName();
        final String email = authorIdent.getEmailAddress();

        return new GitPersonIdent(when, name, email);
    }

    private boolean isMappedToAnotherTag(final Map<String, String> tagPerCommitsHash, final String thisCommitHash) {
        return tagPerCommitsHash.containsKey(thisCommitHash);
    }

    private boolean thisIsANewTag(final Map<String, Ref> tagsPerCommitHash, final String thisCommitHash) {
        return tagsPerCommitHash.containsKey(thisCommitHash);
    }

    private void noteThatTheCommitWasMapped(
        final Map<String, String> tagPerCommitsHash,
        final String currentTagName,
        final String thisCommitHash
    ) {
        tagPerCommitsHash.put(thisCommitHash, currentTagName);
    }

    private String getTagName(final Map<String, Ref> tagPerCommitHash, final String thisCommitHash) {
        return tagPerCommitHash.get(thisCommitHash).getName();
    }

    private boolean notFirstIncludedCommit(final ObjectId from, final ObjectId to) {
        return !from.getName().equals(to.getName());
    }

    private boolean shouldInclude(final RevCommit candidate) {
        return this.commitsToInclude.contains(candidate);
    }

    private Map<String, Ref> getTagPerCommitHash(final List<Ref> tagList) {
        final Map<String, Ref> tagPerCommit = new ConcurrentHashMap<>();

        for (final Ref tag : tagList) {
            tagPerCommit.put(getPeeled(tag).getName(), tag);
        }

        return tagPerCommit;
    }

    private List<Ref> tagsBetweenFromAndTo(final ObjectId from, final ObjectId to)
        throws IncorrectObjectTypeException, MissingObjectException, GitAPIException {
        final List<Ref> tagList = this.git.tagList().call();
        final List<RevCommit> icludedCommits = toList(this.git.log().addRange(from, to).call());
        final List<Ref> includedTags = new ArrayList<>();

        for (final Ref tag : tagList) {
            final ObjectId peeledTag = getPeeled(tag);

            // noinspection SuspiciousMethodCalls
            if (icludedCommits.contains(peeledTag)) {
                includedTags.add(tag);
            }
        }

        return includedTags;
    }

    private List<RevCommit> getDiffingCommits(final RevCommit from, final RevCommit to)
        throws IncorrectObjectTypeException, MissingObjectException, GitAPIException {
        final RevCommit firstCommit = firstCommit();
        final List<RevCommit> allInFrom = toList(this.git.log().addRange(firstCommit, from).call());
        final List<RevCommit> allInTo = toList(this.git.log().addRange(firstCommit, to).call());

        return allInTo.stream()
            .filter(o -> !allInFrom.contains(o))
            .collect(Collectors.toList());
    }

    private ObjectId getPeeled(final Ref tag) {
        try {
            final Ref peeledTag = this.repository.getRefDatabase().peel(tag);

            if (peeledTag.getPeeledObjectId() != null) {
                return peeledTag.getPeeledObjectId();
            }
        } catch (final IOException e) {
            log.debug(e.getMessage(), e);
        }

        return tag.getObjectId();
    }

    public List<Ref> getRefs() {
        try {
            return this.repository.getRefDatabase().getRefs();
        } catch (final IOException e) {
            log.debug(e.getMessage(), e);

            return new ArrayList<>();
        }
    }

    public Ref getRef(final String name) {
        final List<Ref> refs = this.getRefs();
        final String refName = StringUtils.defaultIfBlank(name, Constants.MASTER);

        for (final Ref ref : refs) {
            if (StringUtils.endsWith(ref.getName(), refName)) {
                return ref;
            }
        }

        return null;
    }

    public ObjectId getRefId(final String name) {
        final Ref ref = this.getRef(name);

        if (ref == null) return null;

        try {
            final Ref peeledRef = this.repository.getRefDatabase().peel(ref);

            if (peeledRef.getPeeledObjectId() == null) {
                return ref.getObjectId();
            } else {
                return peeledRef.getPeeledObjectId();
            }
        } catch (final IOException e) {
            log.debug(e.getMessage(), e);
        }

        return null;
    }

    public ObjectId getCommitId(final String revstr) {
        if (revstr.startsWith(GitlogConstants.COMMIT_ZERO)) {
            return this.firstCommit();
        }

        try {
            return this.repository.resolve(revstr);
        } catch (final IOException e) {
            log.debug(e.getMessage(), e);
        }

        return null;
    }

    public ObjectId getId(final String refName, final String commitRevstr) {
        if (StringUtils.isNotEmpty(commitRevstr)) return this.getCommitId(commitRevstr);
        if (StringUtils.isNotEmpty(refName)) return this.getRefId(refName);

        return null;
    }

    private RevCommit firstCommit() {
        try {
            final AnyObjectId refId = getRefId(Constants.HEAD);
            final Iterator<RevCommit> itr = this.git.log().add(refId).call().iterator();

            return getLast(itr);
        } catch (final Exception e) {
            throw new RuntimeException("First commit not found in " + this.repository.getDirectory(), e);
        }
    }

    public ObjectId getFromId(final String refName, final String commitRevstr) {
        final ObjectId fromId = this.getId(refName, commitRevstr);

        if (fromId == null) return this.getCommitId(GitlogConstants.COMMIT_ZERO);

        return fromId;
    }

    public ObjectId getToId(final String refName, final String commitRevstr) {
        return this.getId(refName, commitRevstr);
    }
}
