package cm.xenonbyte.farmbyte.catalog.domain.core.product.ports.primary;

import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductCategory;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductCategoryId;
import cm.xenonbyte.farmbyte.common.domain.vo.Direction;
import cm.xenonbyte.farmbyte.common.domain.vo.Keyword;
import cm.xenonbyte.farmbyte.common.domain.vo.PageInfo;
import jakarta.annotation.Nonnull;

/**
 * @author bamk
 * @version 1.0
 * @since 16/08/2024
 */
public interface ProductCategoryService {
    @Nonnull ProductCategory createProductCategory(@Nonnull ProductCategory productCategory);

    @Nonnull ProductCategory findProductCategoryById(@Nonnull ProductCategoryId productCategoryId);

    @Nonnull PageInfo<ProductCategory> findProductCategories(Integer page, Integer size, String attribute, Direction direction);

    @Nonnull PageInfo<ProductCategory> searchProductCategories(Integer page, Integer size, String sortAttribute, Direction direction, @Nonnull Keyword keyword);

    @Nonnull ProductCategory updateProductCategory(@Nonnull ProductCategoryId productCategoryId, @Nonnull ProductCategory productCategoryToUpdate);
}
