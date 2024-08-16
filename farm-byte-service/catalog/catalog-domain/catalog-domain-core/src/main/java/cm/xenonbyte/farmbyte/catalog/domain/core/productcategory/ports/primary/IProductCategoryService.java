package cm.xenonbyte.farmbyte.catalog.domain.core.productcategory.ports.primary;

import cm.xenonbyte.farmbyte.catalog.domain.core.productcategory.ProductCategory;
import jakarta.annotation.Nonnull;

/**
 * @author bamk
 * @version 1.0
 * @since 16/08/2024
 */
public interface IProductCategoryService {
    @Nonnull ProductCategory createProductCategory(@Nonnull ProductCategory productCategory);
}
