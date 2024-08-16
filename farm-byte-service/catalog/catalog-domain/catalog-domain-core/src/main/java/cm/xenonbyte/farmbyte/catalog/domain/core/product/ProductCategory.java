package cm.xenonbyte.farmbyte.catalog.domain.core.product;

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

    private ProductCategory(Builder builder) {
        setId(builder.id);
        name = builder.name;
        active = builder.active;
        parentProductCategoryId = builder.parentProductCategoryId;
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

    public static Builder builder() {
        return new Builder();
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


    public static final class Builder {
        private ProductCategoryId id;
        private Name name;
        private Active active;
        private ProductCategoryId parentProductCategoryId;

        private Builder() {
        }

        public Builder id(ProductCategoryId val) {
            id = val;
            return this;
        }

        public Builder name(Name val) {
            name = val;
            return this;
        }

        public Builder active(Active val) {
            active = val;
            return this;
        }

        public Builder parentProductCategoryId(ProductCategoryId val) {
            parentProductCategoryId = val;
            return this;
        }

        public ProductCategory build() {
            return new ProductCategory(this);
        }
    }
}
