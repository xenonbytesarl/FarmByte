package cm.xenonbyte.farmbyte.catalog.domain.core.productcategory;

import cm.xenonbyte.farmbyte.common.domain.entity.BaseEntity;
import cm.xenonbyte.farmbyte.common.domain.vo.Active;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import jakarta.annotation.Nonnull;

import java.util.Objects;
import java.util.UUID;

import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_CATEGORY_NAME_IS_REQUIRED;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_CATEGORY_PARENT_ID_IS_REQUIRED;

/**
 * @author bamk
 * @version 1.0
 * @since 16/08/2024
 */
public final class ProductCategory extends BaseEntity<ProductCategoryId> {

    private final Name name;
    private Active active;
    private ProductCategoryId parentProductCategoryId;

    public ProductCategory(@Nonnull Name name) {
        this.name = Objects.requireNonNull(name);
    }

    public ProductCategory(@Nonnull Name name, @Nonnull ProductCategoryId parentProductCategoryId) {
        this.name = Objects.requireNonNull(name);
        this.parentProductCategoryId = Objects.requireNonNull(parentProductCategoryId);
    }

    public static ProductCategory of(Name name) {
        if (name == null) {
            throw new IllegalArgumentException(PRODUCT_CATEGORY_NAME_IS_REQUIRED);
        }
        return new ProductCategory(name);
    }

    public static ProductCategory of(Name name, ProductCategoryId parentProductCategoryId) {
        if (name == null) {
            throw new IllegalArgumentException(PRODUCT_CATEGORY_NAME_IS_REQUIRED);
        }
        if (parentProductCategoryId == null) {
            throw new IllegalArgumentException(PRODUCT_CATEGORY_PARENT_ID_IS_REQUIRED);
        }
        return new ProductCategory(name, parentProductCategoryId);
    }

    public Active getActive() {
        return active;
    }

    public Name getName() {
        return name;
    }

    public void initiate() {
        setId(new ProductCategoryId(UUID.randomUUID()));
        this.active = Active.with(true);
    }

    public ProductCategoryId getParentProductCategoryId() {
        return parentProductCategoryId;
    }
}
