package team.yi.tools.semanticgitlog.render;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import team.yi.tools.semanticcommit.model.GitDate;
import team.yi.tools.semanticgitlog.model.ReleaseLog;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class JsonGitlogRender extends GitlogRender {
    private final ObjectMapper mapper = new ObjectMapper();

    public JsonGitlogRender(final ReleaseLog releaseLog) {
        super(releaseLog);
    }

    public JsonGitlogRender(final ReleaseLog releaseLog, final Charset charset) {
        super(releaseLog, charset);

        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.enable(SerializationFeature.FLUSH_AFTER_WRITE_VALUE);

        final SimpleDateFormat dateFormat = new SimpleDateFormat(GitDate.DATE_FORMAT, Locale.getDefault());
        mapper.setDateFormat(dateFormat);
    }

    @Override
    public void render(final Writer writer) throws IOException {
        this.mapper.writeValue(writer, this.getReleaseLog());
    }
}
