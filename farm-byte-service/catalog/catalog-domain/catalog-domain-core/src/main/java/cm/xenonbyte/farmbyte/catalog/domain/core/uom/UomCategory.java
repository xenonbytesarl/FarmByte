package cm.xenonbyte.farmbyte.catalog.domain.core.uom;

import cm.xenonbyte.farmbyte.common.domain.entity.BaseEntity;
import cm.xenonbyte.farmbyte.common.domain.validation.Assert;
import cm.xenonbyte.farmbyte.common.domain.vo.Active;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import jakarta.annotation.Nonnull;

import java.util.Objects;
import java.util.UUID;

import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.UOM_CATEGORY_ID_IS_REQUIRED;

/**
 * @author bamk
 * @version 1.0
 * @since 14/08/2024
 */
public final class UomCategory  extends BaseEntity<UomCategoryId> {

    private final Name name;
    private UomCategoryId parentUomCategoryId;
    private Active active;

    private UomCategory(@Nonnull Name name) {
        this.name = Objects.requireNonNull(name);
    }

    private UomCategory(@Nonnull Name name, @Nonnull UomCategoryId parentUomCategoryId) {
        this.name = Objects.requireNonNull(name);
        this.parentUomCategoryId = Objects.requireNonNull(parentUomCategoryId, UOM_CATEGORY_ID_IS_REQUIRED);
    }

    private UomCategory(Builder builder) {
        setId(builder.id);
        name = builder.name;
        parentUomCategoryId = builder.parentUomCategoryId;
        active = builder.active;
    }

    public static UomCategory of(@Nonnull Name name) {
        Assert.field("Name", name)
                .notNull();
        return new UomCategory(name);
    }

    public static UomCategory of(@Nonnull Name name, @Nonnull UomCategoryId parentUomCategoryId) {
        Assert.field("Name", name)
                .notNull();
        Assert.field("Parent category", parentUomCategoryId)
                .notNull();
        return new UomCategory(name, parentUomCategoryId);
    }

    public static Builder builder() {
        return new Builder();
    }


    public Name getName() {
        return name;
    }

    public void initializeWithDefaults() {
        setId(new UomCategoryId(UUID.randomUUID()));
        this.active = Active.with(true);
    }

    public Active getActive() {
        return active;
    }

    public UomCategoryId getParentUomCategoryId() {
        return parentUomCategoryId;
    }

    public void validateMandatoryField() {
        Assert.field("Name", name)
                .notNull();
    }


    public static final class Builder {
        private UomCategoryId id;
        private Name name;
        private UomCategoryId parentUomCategoryId;
        private Active active;

        private Builder() {
        }

        public Builder id(UomCategoryId val) {
            id = val;
            return this;
        }

        public Builder name(Name val) {
            name = val;
            return this;
        }

        public Builder parentUomCategoryId(UomCategoryId val) {
            parentUomCategoryId = val;
            return this;
        }

        public Builder active(Active val) {
            active = val;
            return this;
        }

        public UomCategory build() {
            return new UomCategory(this);
        }
    }


}
