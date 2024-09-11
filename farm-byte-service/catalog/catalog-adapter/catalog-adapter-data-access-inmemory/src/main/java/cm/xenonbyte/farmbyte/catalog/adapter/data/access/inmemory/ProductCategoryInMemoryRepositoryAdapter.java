package cm.xenonbyte.farmbyte.catalog.adapter.data.access.inmemory;

import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductCategory;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductCategoryId;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ports.secondary.ProductCategoryRepository;
import cm.xenonbyte.farmbyte.common.domain.vo.Direction;
import cm.xenonbyte.farmbyte.common.domain.vo.Keyword;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.PageInfo;
import jakarta.annotation.Nonnull;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * @author bamk
 * @version 1.0
 * @since 16/08/2024
 */
public final class ProductCategoryInMemoryRepositoryAdapter implements ProductCategoryRepository {

    private final Map<ProductCategoryId, ProductCategory> productCategories = new HashMap<>();

    @Nonnull
    @Override
    public ProductCategory save(@Nonnull ProductCategory productCategory) {
        productCategories.put(productCategory.getId(), productCategory);
        return productCategory;
    }

    @Override
    public Boolean existsById(@Nonnull ProductCategoryId productCategoryId) {
        return productCategories.containsKey(productCategoryId);
    }

    @Override
    public Boolean existsByName(@Nonnull Name name) {
        return productCategories.values().stream().anyMatch(productCategory -> productCategory.getName().equals(name));
    }

    @Nonnull
    @Override
    public Optional<ProductCategory> findById(@Nonnull ProductCategoryId productCategoryId) {
        return Optional.ofNullable(productCategories.get(productCategoryId));
    }

    @Nonnull
    @Override
    public PageInfo<ProductCategory> findAll(Integer page, Integer size, String attribute, Direction direction) {
        PageInfo<ProductCategory> productCategoryPageInfo = new PageInfo<>();
        Comparator<ProductCategory> comparing = Comparator.comparing((ProductCategory a) -> a.getName().getText().getValue());
        return productCategoryPageInfo.with(
                page,
                size,
                productCategories.values().stream()
                        .sorted(Direction.ASC.equals(direction) ? comparing : comparing.reversed())
                        .toList()
        );
    }

    @Nonnull
    @Override
    public PageInfo<ProductCategory> search(Integer page, Integer size, String attribute, Direction direction, @Nonnull Keyword keyword) {
        Predicate<ProductCategory> productCategoryNammePredicate = productCategory -> productCategory.getName().getText().getValue().toLowerCase().contains(keyword.getText().getValue().toLowerCase());
        Comparator<ProductCategory> comparing = Comparator.comparing((ProductCategory a) -> a.getName().getText().getValue());
        PageInfo<ProductCategory> productCategoryPageInfo = new PageInfo<>();
        return productCategoryPageInfo.with(
                page,
                size,
                productCategories.values().stream()
                        .filter(productCategoryNammePredicate)
                        .sorted(Direction.ASC.equals(direction) ? comparing: comparing.reversed())
                        .toList()
        );
    }

    @Override
    public Optional<ProductCategory> findByName(@Nonnull Name name) {
        return productCategories.values().stream()
                .filter(productCategory ->
                        productCategory.getName().getText().getValue().equalsIgnoreCase(name.getText().getValue()))
                .findFirst();
    }

    @Nonnull
    @Override
    public ProductCategory update(@Nonnull ProductCategory oldProductCategory, ProductCategory newProductCategory) {
        productCategories.replace(oldProductCategory.getId(), newProductCategory);
        return newProductCategory;
    }
}
