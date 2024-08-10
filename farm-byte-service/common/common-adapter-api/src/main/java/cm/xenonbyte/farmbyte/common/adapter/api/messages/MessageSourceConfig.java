package cm.xenonbyte.farmbyte.common.adapter.api.messages;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.nio.charset.StandardCharsets;

/**
 * @author bamk
 * @version 1.0
 * @since 09/08/2024
 */
@Configuration
public class MessageSourceConfig {

    public static final String DEFAULT_BUNDLE_PATH = "classpath:messages/messages";
    public static final String DEFAULTS_PATH = "classpath:messages/defaults";

    @Bean
    public static ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames(DEFAULT_BUNDLE_PATH, DEFAULTS_PATH);
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
        messageSource.setCacheSeconds(3600);
        return messageSource;
    }

}
