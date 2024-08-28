package cm.xenonbyte.farmbyte.catalog.domain.core.product.ports.primary;

import cm.xenonbyte.farmbyte.catalog.domain.core.product.Product;
import jakarta.annotation.Nonnull;

/**
 * @author bamk
 * @version 1.0
 * @since 20/08/2024
 */
public interface ProductService {
    @Nonnull Product createProduct(@Nonnull Product product);
}
