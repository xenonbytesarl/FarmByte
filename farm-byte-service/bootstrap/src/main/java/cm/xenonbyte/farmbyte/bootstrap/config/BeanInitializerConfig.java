package cm.xenonbyte.farmbyte.bootstrap.config;

import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomService;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.ports.primary.IUomDomainService;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.ports.secondary.UomRepository;
import jakarta.annotation.Nonnull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author bamk
 * @version 1.0
 * @since 08/08/2024
 */
@Configuration
public class BeanInitializerConfig {
    @Bean
    public IUomDomainService uomDomainService(@Nonnull UomRepository uomRepository) {
        return new UomService(uomRepository);
    }
}
