package team.yi.tools.semanticgitlog.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import team.yi.tools.semanticcommit.model.ReleaseCommitLocale;
import team.yi.tools.semanticcommit.parser.CommitLocaleParser;
import team.yi.tools.semanticcommit.parser.ParserConstants;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class CommitLocaleService {
    private final Set<ReleaseCommitLocale> items = new HashSet<>();
    private final String defaultLang;

    public CommitLocaleService(final String defaultLang) {
        this.defaultLang = StringUtils.defaultIfBlank(defaultLang, ParserConstants.DEFAULT_LANG);
    }

    public void load(final Map<String, File> localeFiles) {
        this.items.clear();

        if (localeFiles == null || localeFiles.isEmpty()) return;

        for (final Map.Entry<String, File> entry : localeFiles.entrySet()) {
            final String lang = entry.getKey();
            final File file = entry.getValue();

            CommitLocaleParser parser = null;

            try {
                parser = new CommitLocaleParser(lang, file);
            } catch (final IOException e) {
                log.debug(e.getMessage(), e);
            }

            if (parser == null) continue;

            final List<ReleaseCommitLocale> items = parser.parse();

            this.items.addAll(items);
        }
    }

    public ReleaseCommitLocale get(final String commitHash) {
        return get(commitHash, this.defaultLang);
    }

    public ReleaseCommitLocale get(final String commitHash, final String lang) {
        return this.items.stream()
            .filter(x -> StringUtils.compareIgnoreCase(x.getLang(), StringUtils.defaultIfBlank(lang, this.defaultLang)) == 0)
            .filter(x -> StringUtils.startsWithIgnoreCase(commitHash, x.getCommitHash()))
            .findFirst()
            .orElse(null);
    }

    public List<ReleaseCommitLocale> findAll(final String commitHash) {
        return this.items.stream()
            .filter(x -> StringUtils.startsWithIgnoreCase(commitHash, x.getCommitHash()))
            .collect(Collectors.toList());
    }
}
