package cm.xenonbyte.farmbyte.catalog.domain.core.uom;

import jakarta.annotation.Nonnull;

import java.util.Objects;

/**
 * @author bamk
 * @version 1.0
 * @since 06/08/2024
 */
public final class Ratio {

    public static final Double REFERENCE = 1.0;

    private final Double value;

    public Ratio(Double value) {
        this.value = value;
    }

    @Nonnull
    public static Ratio of(Double value) {
        return new Ratio(value);
    }

    @Nonnull
    public Double getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ratio ratio = (Ratio) o;
        return Objects.equals(value, ratio.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    public boolean isEqualOrLowerThanReference() {
        return value != null && value <= REFERENCE;
    }

    public boolean isEqualOrGreaterThanReference() {
        return value != null && value >= REFERENCE;
    }
}
