package cm.xenonbyte.farmbyte.catalog.domain.core.product;

import jakarta.annotation.Nonnull;

import java.util.Objects;

/**
 * @author bamk
 * @version 1.0
 * @since 21/08/2024
 */
public final class Sellable {

    private static final String SELLABLE_VALUE_IS_REQUIRED = "Sellable.1";

    private final Boolean value;

    public Sellable(@Nonnull Boolean value) {
        this.value = Objects.requireNonNull(value);
    }

    public static Sellable with(@Nonnull Boolean value) {
        if (value == null) {
            throw new NullPointerException(SELLABLE_VALUE_IS_REQUIRED);
        }
        return new Sellable(value);
    }

    public Boolean getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sellable sellable = (Sellable) o;
        return Objects.equals(value, sellable.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
