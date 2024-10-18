package cm.xenonbyte.farmbyte.common.domain.vo;

import cm.xenonbyte.farmbyte.common.domain.validation.Assert;
import jakarta.annotation.Nonnull;

import java.util.Objects;

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
        Assert.field("Active", value)
                .notNull();
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
