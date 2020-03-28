package team.yi.tools.semanticgitlog.render;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import team.yi.tools.semanticcommit.model.GitDate;
import team.yi.tools.semanticgitlog.GitlogConstants;
import team.yi.tools.semanticgitlog.model.ReleaseLog;
import team.yi.tools.semanticgitlog.render.mutache.functions.DateFormatFunction;
import team.yi.tools.semanticgitlog.render.mutache.functions.ScopeProfileFunction;
import team.yi.tools.semanticgitlog.service.ScopeProfileService;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;

public class MustacheGitlogRender extends GitlogRender {
    private final MustacheFactory mf = new DefaultMustacheFactory();
    private final File template;
    private final ScopeProfileService scopeProfileService;

    public MustacheGitlogRender(final ReleaseLog releaseLog, final File template, final ScopeProfileService scopeProfileService) {
        this(releaseLog, template, StandardCharsets.UTF_8, scopeProfileService);
    }

    public MustacheGitlogRender(final ReleaseLog releaseLog, final File template, final Charset charset, final ScopeProfileService scopeProfileService) {
        super(releaseLog, charset);

        this.template = template;
        this.scopeProfileService = scopeProfileService;
    }

    @Override
    public void render(final Writer writer) throws IOException {
        try (final Reader reader = Files.newBufferedReader(this.template.toPath(), this.getCharset())) {
            final HashMap<String, Object> extraScopes = new HashMap<>();
            extraScopes.put("now", new GitDate());
            extraScopes.put("formatDate", new DateFormatFunction(GitlogConstants.DEFAULT_DATE_FORMAT, GitlogConstants.DEFAULT_TIMEZONE));
            extraScopes.put("scopeProfile", new ScopeProfileFunction(this.scopeProfileService));

            final Object[] scopes = {this.getReleaseLog(), extraScopes};
            final Mustache mustache = mf.compile(reader, this.template.getAbsolutePath());

            mustache.execute(writer, scopes).flush();
        }
    }
}
