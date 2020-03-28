package team.yi.tools.semanticgitlog.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import team.yi.tools.semanticcommit.model.ScopeProfile;
import team.yi.tools.semanticcommit.parser.ParserConstants;
import team.yi.tools.semanticcommit.parser.ScopeProfileParser;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class ScopeProfileService {
    private final Set<ScopeProfile> items = new HashSet<>();
    private final String defaultLang;

    public ScopeProfileService(final String defaultLang) {
        this.defaultLang = StringUtils.defaultIfBlank(defaultLang, ParserConstants.DEFAULT_LANG);
    }

    public void load(final Map<String, File> scopeFiles) {
        this.items.clear();

        if (scopeFiles == null || scopeFiles.isEmpty()) return;

        for (final Map.Entry<String, File> entry : scopeFiles.entrySet()) {
            final String lang = entry.getKey();
            final File file = entry.getValue();

            ScopeProfileParser parser = null;

            try {
                parser = new ScopeProfileParser(lang, file);
            } catch (final IOException e) {
                log.debug(e.getMessage(), e);
            }

            if (parser == null) continue;

            final List<ScopeProfile> items = parser.parse();

            this.items.addAll(items);
        }
    }

    public ScopeProfile get(final String name) {
        return get(name, this.defaultLang);
    }

    public ScopeProfile get(final String name, final String lang) {
        return this.items.stream()
            .filter(x -> StringUtils.compareIgnoreCase(x.getLang(), StringUtils.defaultIfBlank(lang, this.defaultLang)) == 0)
            .filter(x -> StringUtils.startsWithIgnoreCase(name, x.getName()))
            .findFirst()
            .orElse(null);
    }

    public List<ScopeProfile> findAll(final String name) {
        return this.items.stream()
            .filter(x -> StringUtils.startsWithIgnoreCase(name, x.getName()))
            .collect(Collectors.toList());
    }
}
