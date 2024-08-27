package cm.xenonbyte.farmbyte.common.domain.vo;

import jakarta.annotation.Nonnull;

import java.util.Objects;

import static cm.xenonbyte.farmbyte.common.domain.constant.CommonDomainConstant.RATION_CAN_NOT_BE_NULL;

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
        if (value == null) {
            throw new IllegalArgumentException(RATION_CAN_NOT_BE_NULL);
        }
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
