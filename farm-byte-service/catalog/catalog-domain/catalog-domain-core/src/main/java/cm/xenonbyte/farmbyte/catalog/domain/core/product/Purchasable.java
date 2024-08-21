package cm.xenonbyte.farmbyte.catalog.domain.core.product;

import jakarta.annotation.Nonnull;

import java.util.Objects;

/**
 * @author bamk
 * @version 1.0
 * @since 21/08/2024
 */
public final class Purchasable {

    private static final String PURCHASABLE_VALUE_IS_REQUIRED = "Purchasable.1";

    private final Boolean value;

    public Purchasable(@Nonnull Boolean value) {
        this.value = Objects.requireNonNull(value);
    }

    public static Purchasable of(@Nonnull Boolean value) {
        if (value == null) {
            throw new NullPointerException(PURCHASABLE_VALUE_IS_REQUIRED);
        }
        return new Purchasable(value);
    }

    public Boolean getValue() {
        return value;
    }

}
