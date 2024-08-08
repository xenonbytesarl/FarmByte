package cm.xenonbyte.farmbyte.adapter.data.access.jpa.config;

import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author bamk
 * @version 1.0
 * @since 10/03/2024
 */
@Configuration
public class JacksonModuleConfiguration {
    @Bean
    public JsonNullableModule jsonNullableModule() {
        return new JsonNullableModule();
    }
}
