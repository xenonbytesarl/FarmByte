package cm.xenonbyte.farmbyte.catalog.domain.core.product.ports.secondary;

import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductCategory;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductCategoryId;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import jakarta.annotation.Nonnull;

/**
 * @author bamk
 * @version 1.0
 * @since 16/08/2024
 */
public interface ProductCategoryRepository {
    @Nonnull ProductCategory save(@Nonnull ProductCategory parentProductCategory);

    Boolean existsById(@Nonnull ProductCategoryId parentProductCategoryId);

    Boolean existsByName(@Nonnull Name name);
}
