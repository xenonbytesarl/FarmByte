package cm.xenonbyte.farmbyte.common.adapter.api.messages;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static cm.xenonbyte.farmbyte.common.adapter.api.constant.CommonAdapterRestApi.DEFAULT_LOCALE;

/**
 * @author bamk
 * @version 1.0
 * @since 09/08/2024
 */
@Configuration
public class CustomLocalResolver extends AcceptHeaderLocaleResolver {

    private static final List<Locale> LOCALES = Arrays.asList(
            Locale.FRENCH, Locale.ENGLISH);

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        if(StringUtils.isEmpty(request.getHeader(HttpHeaders.ACCEPT_LANGUAGE))) {
            return Locale.forLanguageTag(DEFAULT_LOCALE);
        }
        List<Locale.LanguageRange> languageRanges = Locale.LanguageRange.parse(request.getHeader(HttpHeaders.ACCEPT_LANGUAGE));
        return Locale.lookup(languageRanges, LOCALES);
    }

    @Bean
    public LocaleResolver localeResolver() {
        return new CustomLocalResolver();
    }
}
