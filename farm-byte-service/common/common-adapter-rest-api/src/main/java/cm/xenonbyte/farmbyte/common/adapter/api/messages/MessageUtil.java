package cm.xenonbyte.farmbyte.common.adapter.api.messages;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Locale;

/**
 * @author bamk
 * @version 1.0
 * @since 09/08/2024
 */
@Component
public final class MessageUtil {

    private MessageUtil() {}

    public static String getMessage(String message, String... dynamicValues) {
        return MessageSourceConfig.messageSource().getMessage(message, dynamicValues, LocaleContextHolder.getLocale());
    }

    public static String getMessage(String message, Locale locale, String... dynamicValues) {
        return MessageSourceConfig.messageSource().getMessage(message, dynamicValues, locale);
    }

    public static String[] toStringArray(Object[] args) {
        return args ==null? new String[]{} : Arrays.stream(args).map(Object::toString).toArray(String[]::new);
    }
}
