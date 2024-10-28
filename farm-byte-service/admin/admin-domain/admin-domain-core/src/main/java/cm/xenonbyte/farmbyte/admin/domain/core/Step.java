package cm.xenonbyte.farmbyte.admin.domain.core;

import cm.xenonbyte.farmbyte.common.domain.validation.Assert;
import jakarta.annotation.Nonnull;

import java.util.Objects;

/**
 * @author bamk
 * @version 1.0
 * @since 28/10/2024
 */
public record Step(Long value) {
    public Step(@Nonnull Long value) {
        this.value = Objects.requireNonNull(value);
    }

    @Nonnull
    public static Step of(Long value) {
        Assert.field("Step", value)
                .notNull()
                .notNull(value);
        return new Step(value);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Step step = (Step) object;
        return Objects.equals(value, step.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
