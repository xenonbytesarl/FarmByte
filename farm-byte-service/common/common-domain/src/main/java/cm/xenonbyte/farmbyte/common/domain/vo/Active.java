package cm.xenonbyte.farmbyte.common.domain.vo;

import jakarta.annotation.Nonnull;

import java.util.Objects;

import static cm.xenonbyte.farmbyte.common.domain.constant.CommonDomainConstant.ACTIVE_VALUE_IS_REQUIRED;

/**
 * @author bamk
 * @version 1.0
 * @since 06/08/2024
 */
public final class Active {


    private final Boolean value;

    public Active(@Nonnull Boolean value) {
        this.value = Objects.requireNonNull(value);
    }

    @Nonnull
    public static Active with(Boolean value) {
        if(value == null) {
            throw new IllegalArgumentException(ACTIVE_VALUE_IS_REQUIRED);
        }
        return new Active(value);
    }

    @Nonnull
    public Boolean getValue() {
        return value;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Active active = (Active) o;
        return Objects.equals(value, active.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
