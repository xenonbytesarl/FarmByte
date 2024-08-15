package cm.xenonbyte.farmbyte.bootstrap.config;

import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomService;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.ports.primary.IUomService;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.ports.secondary.UomRepository;
import cm.xenonbyte.farmbyte.catalog.domain.core.uomcategory.UomCategoryService;
import cm.xenonbyte.farmbyte.catalog.domain.core.uomcategory.ports.primary.IUomCategoryService;
import cm.xenonbyte.farmbyte.catalog.domain.core.uomcategory.ports.secondary.UomCategoryRepository;
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
    public IUomService uomDomainService(@Nonnull UomRepository uomRepository) {
        return new UomService(uomRepository);
    }

    @Bean
    public IUomCategoryService uomCategoryDomainService(@Nonnull UomCategoryRepository uomCategoryRepository) {
        return new UomCategoryService(uomCategoryRepository);
    }
}
