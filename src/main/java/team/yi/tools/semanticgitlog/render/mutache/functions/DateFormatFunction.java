package team.yi.tools.semanticgitlog.render.mutache.functions;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import team.yi.tools.semanticcommit.model.GitDate;

import java.text.ParseException;
import java.util.Date;
import java.util.TimeZone;
import java.util.function.Function;

@Slf4j
public class DateFormatFunction implements Function<String, String> {
    private final String dateFormat;
    private final String timeZone;

    public DateFormatFunction(final String dateFormat, final String timeZone) {
        this.dateFormat = dateFormat;
        this.timeZone = timeZone;
    }

    @Override
    public String apply(final String input) {
        // {{#formatDate}}{{releaseDate}}|yyyy-MM-dd|+0800{{/formatDate}}
        final String[] inputParams = StringUtils.split(input, '|');
        final String dateString = StringUtils.trimToNull(inputParams[0]);

        if (dateString == null) return null;

        final Date date;

        try {
            // 2020-03-04 20:49:55.891 -0500
            date = DateUtils.parseDate(dateString, GitDate.DATE_FORMAT);
        } catch (final ParseException e) {
            log.debug(e.getMessage(), e);

            return dateString;
        }

        final String dateFormat = StringUtils.trimToNull(inputParams.length > 1 ? inputParams[1] : null);
        final String zoneId = StringUtils.trimToNull(inputParams.length > 2 ? inputParams[2] : null);
        final TimeZone timeZone = TimeZone.getTimeZone(StringUtils.defaultIfBlank(zoneId, this.timeZone));

        return DateFormatUtils.format(date, StringUtils.defaultIfBlank(dateFormat, this.dateFormat), timeZone);
    }
}
