package cm.xenonbyte.farmbyte.admin.domain.core.vo;

import cm.xenonbyte.farmbyte.common.domain.validation.Assert;
import jakarta.annotation.Nonnull;

import java.util.Objects;

/**
 * @author bamk
 * @version 1.0
 * @since 28/10/2024
 */
public record Next(Long value) {
    public Next(@Nonnull Long value) {
        this.value = Objects.requireNonNull(value);
    }

    @Nonnull
    public static Next of(Long value) {
        Assert.field("Next", value)
                .notNull()
                .notNull(value);
        return new Next(value);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Next next = (Next) object;
        return Objects.equals(value, next.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
