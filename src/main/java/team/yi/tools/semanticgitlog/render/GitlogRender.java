package team.yi.tools.semanticgitlog.render;

import lombok.extern.slf4j.Slf4j;
import team.yi.tools.semanticgitlog.GitlogUtils;
import team.yi.tools.semanticgitlog.model.ReleaseLog;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Slf4j
public abstract class GitlogRender {
    private final ReleaseLog releaseLog;
    private final Charset charset;

    protected GitlogRender(final ReleaseLog releaseLog) {
        this(releaseLog, StandardCharsets.UTF_8);
    }

    protected GitlogRender(final ReleaseLog releaseLog, final Charset charset) {
        this.releaseLog = releaseLog;
        this.charset = charset;
    }

    public ReleaseLog getReleaseLog() {
        return releaseLog;
    }

    public Charset getCharset() {
        return charset;
    }

    public String render() throws IOException {
        final Writer writer = new StringWriter();

        this.render(writer);

        return writer.toString();
    }

    public void render(final File file) throws IOException {
        GitlogUtils.forceMkdir(file.getParentFile());

        try (final BufferedWriter writer = Files.newBufferedWriter(file.toPath(), this.getCharset())) {
            this.render(writer);
        }
    }

    public abstract void render(final Writer writer) throws IOException;
}
