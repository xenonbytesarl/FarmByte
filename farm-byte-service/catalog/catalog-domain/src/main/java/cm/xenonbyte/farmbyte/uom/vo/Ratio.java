package cm.xenonbyte.farmbyte.uom.vo;

import jakarta.annotation.Nonnull;

import java.util.Objects;

/**
 * @author bamk
 * @version 1.0
 * @since 06/08/2024
 */
public final class Ratio {

    public static final Double REFERENCE = 1D;

    private final Double value;

    private Ratio(Double value) {
        this.value = value;
    }

    @Nonnull
    public static Ratio from(Double value) {
        return new Ratio(value);
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
}
