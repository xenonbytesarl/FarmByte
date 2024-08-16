package cm.xenonbyte.farmbyte.catalog.domain.core.productcategory.ports.secondary;

import cm.xenonbyte.farmbyte.catalog.domain.core.productcategory.ProductCategory;
import cm.xenonbyte.farmbyte.catalog.domain.core.productcategory.ProductCategoryId;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import jakarta.annotation.Nonnull;

/**
 * @author bamk
 * @version 1.0
 * @since 16/08/2024
 */
public interface ProductCategoryRepository {
    @Nonnull ProductCategory save(@Nonnull ProductCategory parentProductCategory);

    boolean existsById(@Nonnull ProductCategoryId parentProductCategoryId);

    boolean existsByName(@Nonnull Name name);
}
