package cm.xenonbyte.farmbyte.catalog.domain.core.uom;

import jakarta.annotation.Nonnull;

import java.util.Objects;

/**
 * @author bamk
 * @version 1.0
 * @since 06/08/2024
 */
public record Ratio(@Nonnull Double value) {

    public static final Double REFERENCE = 1.0;

    @Nonnull
    public static Ratio of(Double value) {
        if (value == null) {
            throw new IllegalArgumentException("Ration.1");
        }
        return new Ratio(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ratio ratio = (Ratio) o;
        return Objects.equals(value, ratio.value);
    }

    public boolean isEqualOrLowerThanReference() {
        return value <= REFERENCE;
    }

    public boolean isEqualOrGreaterThanReference() {
        return value >= REFERENCE;
    }
}
