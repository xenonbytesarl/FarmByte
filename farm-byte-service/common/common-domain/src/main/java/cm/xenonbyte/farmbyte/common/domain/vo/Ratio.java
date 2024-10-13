package cm.xenonbyte.farmbyte.common.domain.vo;

import cm.xenonbyte.farmbyte.common.domain.validation.Assert;
import jakarta.annotation.Nonnull;

import java.util.Objects;

/**
 * @author bamk
 * @version 1.0
 * @since 06/08/2024
 */
public class Ratio {

    public static final Double REFERENCE = 1.0;

    private final Double value;

    public Ratio(@Nonnull Double value) {
        this.value = Objects.requireNonNull(value);
    }

    @Nonnull
    public static Ratio of(Double value) {
        Assert.field("ratio", value)
            .notNull();
        return new Ratio(value);
    }

    public @Nonnull Double getValue() {
        return value;
    }


    public boolean isEqualOrLowerThanReference() {
        return value <= REFERENCE;
    }

    public boolean isEqualOrGreaterThanReference() {
        return value >= REFERENCE;
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
}
