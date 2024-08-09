package cm.xenonbyte.farmbyte.catalog.domain.core.uom;

import cm.xenonbyte.farmbyte.catalog.domain.core.uomcategory.UomCategoryId;
import cm.xenonbyte.farmbyte.common.domain.entity.BaseEntity;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Objects;
import java.util.UUID;

/**
 * @author bamk
 * @version 1.0
 * @since 06/08/2024
 */
public final class Uom extends BaseEntity<UomId> {
    private final Name name;
    private final UomCategoryId uomCategoryId;
    private final UomType uomType;
    private Ratio ratio;
    private Active active;

    private Uom(
            @Nonnull final Name name,
            @Nonnull final UomCategoryId uomCategoryId,
            @Nonnull final UomType uomType,
            @Nullable final Ratio ratio
    ) {
        this.name = Objects.requireNonNull(name);
        this.uomCategoryId = Objects.requireNonNull(uomCategoryId);
        this.uomType = Objects.requireNonNull(uomType);
        this.ratio = ratio;
    }

    private Uom(Builder builder) {
        setId(builder.id);
        name = builder.name;
        uomCategoryId = builder.uomCategoryId;
        uomType = builder.uomType;
        ratio = builder.ratio;
        active = builder.active;
    }

    @Nonnull
    public static Uom from(
            @Nonnull final Name name,
            @Nonnull final UomCategoryId uomCategoryId,
            @Nonnull final UomType uomType,
            @Nullable final Ratio ratio
    ) {
        return new Uom(
                name,
                uomCategoryId,
                uomType,
                ratio
        );
    }

    public static Builder builder() {
        return new Builder();
    }

    public void initiate() {
        if(uomType.equals(UomType.REFERENCE)) {
            ratio = Ratio.of(Ratio.REFERENCE);
        } else {
            validateRatio();
        }
        setId(new UomId(UUID.randomUUID()));
        active = Active.with(true);
    }

    private void validateRatio() {
        if(ratio == null) {
            throw new IllegalArgumentException("Ratio is required when unit of measure type is not reference.");
        }
        if(uomType.equals(UomType.GREATER) && ratio.isEqualOrLowerThanReference()) {
            throw new IllegalArgumentException("Ratio should be greater than 1 when unit of measure type is not greater.");
        }
        if(uomType.equals(UomType.LOWER) && ratio.isEqualOrGreaterThanReference()) {
            throw new IllegalArgumentException("Ratio should be lower than 1 when unit of measure type is not lower.");
        }
    }



    @Nonnull
    public Active getActive() {
        return active;
    }

    @Nonnull
    public Ratio getRatio() {
        return ratio;
    }
    @Nonnull
    public Name getName() {
        return name;
    }
    @Nonnull
    public UomCategoryId getUomCategoryId() {
        return uomCategoryId;
    }

    @Nonnull
    public UomType getUomType() {
        return uomType;
    }

    public static final class Builder {
        private UomId id;
        private Name name;
        private UomCategoryId uomCategoryId;
        private UomType uomType;
        private Ratio ratio;
        private Active active;

        private Builder() {
        }

        public Builder id(UomId val) {
            id = val;
            return this;
        }

        public Builder name(Name val) {
            name = val;
            return this;
        }

        public Builder uomCategoryId(UomCategoryId val) {
            uomCategoryId = val;
            return this;
        }

        public Builder uomType(UomType val) {
            uomType = val;
            return this;
        }

        public Builder ratio(Ratio val) {
            ratio = val;
            return this;
        }

        public Builder active(Active val) {
            active = val;
            return this;
        }

        public Uom build() {
            return new Uom(this);
        }
    }
}
