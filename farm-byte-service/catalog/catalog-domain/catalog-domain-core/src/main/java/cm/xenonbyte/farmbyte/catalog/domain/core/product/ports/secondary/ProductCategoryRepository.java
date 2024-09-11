package cm.xenonbyte.farmbyte.catalog.domain.core.product.ports.secondary;

import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductCategory;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductCategoryId;
import cm.xenonbyte.farmbyte.common.domain.vo.Direction;
import cm.xenonbyte.farmbyte.common.domain.vo.Keyword;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.PageInfo;
import jakarta.annotation.Nonnull;

import java.util.Optional;

/**
 * @author bamk
 * @version 1.0
 * @since 16/08/2024
 */
public interface ProductCategoryRepository {
    @Nonnull ProductCategory save(@Nonnull ProductCategory parentProductCategory);

    Boolean existsById(@Nonnull ProductCategoryId parentProductCategoryId);

    Boolean existsByName(@Nonnull Name name);

    @Nonnull
    Optional<ProductCategory> findById(@Nonnull ProductCategoryId productCategoryId);

    @Nonnull PageInfo<ProductCategory> findAll(Integer page, Integer size, String attribute, Direction direction);

    @Nonnull PageInfo<ProductCategory> search(Integer page, Integer size, String attribute, Direction direction, @Nonnull Keyword keyword);

    Optional<ProductCategory> findByName(@Nonnull Name name);

    @Nonnull ProductCategory update(@Nonnull ProductCategory oldProductCategory, ProductCategory productCategoryToUpdate);
}
