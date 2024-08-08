package cm.xenonbyte.farmbyte.catalog.domain.core.uom;

import cm.xenonbyte.farmbyte.catalog.domain.core.uomcategory.UomCategoryId;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Objects;
import java.util.UUID;

/**
 * @author bamk
 * @version 1.0
 * @since 06/08/2024
 */
public final class Uom {
    private final Name name;
    private final UomCategoryId uomCategoryId;
    private final UomType uomType;
    private Ratio ratio;
    private UomId uomId;
    private Active active;

    public Uom(
            @Nullable final UomId uomId,
            @Nonnull final Name name,
            @Nonnull final UomCategoryId uomCategoryId,
            @Nonnull final UomType uomType,
            @Nullable final Ratio ratio,
            @Nullable final Active active
    ) {
        this.uomId = uomId;
        this.name = Objects.requireNonNull(name);
        this.uomCategoryId = Objects.requireNonNull(uomCategoryId);
        this.uomType = Objects.requireNonNull(uomType);
        this.ratio = ratio;
        this.active = active;
    }

    @Nonnull
    public static Uom from(
            @Nonnull final Name name,
            @Nonnull final UomCategoryId uomCategoryId,
            @Nonnull final UomType uomType,
            @Nullable final Ratio ratio
    ) {
        return new Uom(
                null,
                name,
                uomCategoryId,
                uomType,
                ratio,
                null
        );
    }

    public void initiate() {
        if(uomType.equals(UomType.REFERENCE)) {
            ratio = Ratio.of(Ratio.REFERENCE);
        } else {
            validateRatio();
        }
        uomId = UomId.of(UUID.randomUUID());
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
    public UomId getUomId() {
        return uomId;
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
}
