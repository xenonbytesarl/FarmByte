package cm.xenonbyte.farmbyte.catalog.adapter.data.access.inmemory;

import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductCategory;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductCategoryId;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ports.secondary.ProductCategoryRepository;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import jakarta.annotation.Nonnull;

import java.util.HashMap;
import java.util.Map;

/**
 * @author bamk
 * @version 1.0
 * @since 16/08/2024
 */
public final class InMemoryProductCategoryRepository implements ProductCategoryRepository {

    private final Map<ProductCategoryId, ProductCategory> productCategoryMap = new HashMap<>();

    @Nonnull
    @Override
    public ProductCategory save(@Nonnull ProductCategory parentProductCategory) {
        productCategoryMap.put(parentProductCategory.getId(), parentProductCategory);
        return parentProductCategory;
    }

    @Override
    public Boolean existsById(@Nonnull ProductCategoryId parentProductCategoryId) {
        return productCategoryMap.containsKey(parentProductCategoryId);
    }

    @Override
    public Boolean existsByName(@Nonnull Name name) {
        return productCategoryMap.values().stream().anyMatch(productCategory -> productCategory.getName().equals(name));
    }
}
