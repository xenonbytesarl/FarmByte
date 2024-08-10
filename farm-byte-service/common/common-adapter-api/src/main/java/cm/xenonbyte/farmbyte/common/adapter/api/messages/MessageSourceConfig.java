package cm.xenonbyte.farmbyte.common.adapter.api.messages;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.nio.charset.StandardCharsets;

import static cm.xenonbyte.farmbyte.common.adapter.api.constant.CommonAdapterRestApi.DEFAULTS_PATH;
import static cm.xenonbyte.farmbyte.common.adapter.api.constant.CommonAdapterRestApi.DEFAULT_BUNDLE_PATH;

/**
 * @author bamk
 * @version 1.0
 * @since 09/08/2024
 */
@Configuration
public class MessageSourceConfig {

    @Bean
    public static ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames(DEFAULT_BUNDLE_PATH, DEFAULTS_PATH);
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
        messageSource.setCacheSeconds(3600);
        return messageSource;
    }

}
