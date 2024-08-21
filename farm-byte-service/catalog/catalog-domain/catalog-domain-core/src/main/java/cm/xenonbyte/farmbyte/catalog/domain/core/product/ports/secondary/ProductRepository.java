package cm.xenonbyte.farmbyte.catalog.domain.core.product.ports.secondary;

import cm.xenonbyte.farmbyte.catalog.domain.core.product.Product;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import jakarta.annotation.Nonnull;

/**
 * @author bamk
 * @version 1.0
 * @since 20/08/2024
 */
public interface ProductRepository {
    @Nonnull Product save(@Nonnull Product product);

    Boolean existsByName(Name name);
}
