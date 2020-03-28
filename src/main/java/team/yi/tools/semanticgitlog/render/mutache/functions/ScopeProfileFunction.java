package team.yi.tools.semanticgitlog.render.mutache.functions;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import team.yi.tools.semanticcommit.model.ScopeProfile;
import team.yi.tools.semanticgitlog.service.ScopeProfileService;

import java.util.function.Function;

@Slf4j
public class ScopeProfileFunction implements Function<String, String> {
    private final ScopeProfileService scopeProfileService;

    public ScopeProfileFunction(final ScopeProfileService scopeProfileService) {
        this.scopeProfileService = scopeProfileService;
    }

    @Override
    public String apply(final String input) {
        // {{#scopeProfile}}{{commitScope}}|zh-cn:displayName{{/scopeProfile}}
        final String[] inputParams = StringUtils.split(input, "|:");
        final String name = StringUtils.trimToNull(inputParams[0]);

        if (name == null) return null;

        final String locale = inputParams.length > 1 ? inputParams[1] : null;
        final ScopeProfile scopeProfile = this.scopeProfileService.get(name, locale);

        if (scopeProfile == null) return null;

        final String prop = inputParams.length > 2 ? inputParams[2] : "displayName";

        return "description".equals(prop) ? scopeProfile.getDescription() : scopeProfile.getDisplayName();
    }
}
