package cm.xenonbyte.farmbyte.uom;

import cm.xenonbyte.farmbyte.uom.vo.*;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

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

    private Uom(
            @Nonnull final Name name,
            @Nonnull final UomCategoryId uomCategoryId,
            @Nonnull final UomType uomType,
            @Nullable final Ratio ratio
    ) {

        this.name = name;
        this.uomCategoryId = uomCategoryId;
        this.uomType = uomType;
        this.ratio = ratio;
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

    public void initiate() {
        if(uomType.equals(UomType.REFERENCE)) {
            ratio = Ratio.from(Ratio.REFERENCE);
        } else {
            validateRatio();
        }
        uomId = UomId.generate(UUID.randomUUID());
        active = Active.from(true);
    }

    private void validateRatio() {
        if(ratio == null) {
            throw new IllegalArgumentException("Ratio is required when unit of measure type is not reference.");
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
}
