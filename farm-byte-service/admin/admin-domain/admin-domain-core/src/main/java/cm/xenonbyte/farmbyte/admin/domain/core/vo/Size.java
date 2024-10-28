package cm.xenonbyte.farmbyte.admin.domain.core.vo;

import cm.xenonbyte.farmbyte.common.domain.validation.Assert;
import jakarta.annotation.Nonnull;

import java.util.Objects;

/**
 * @author bamk
 * @version 1.0
 * @since 28/10/2024
 */
public record Size(Long value) {
    public Size(@Nonnull Long value) {
        this.value = Objects.requireNonNull(value);
    }

    @Nonnull
    public static Size of(Long value) {
        Assert.field("Size", value)
                .notNull()
                .notNull(value);
        return new Size(value);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Size size = (Size) object;
        return Objects.equals(value, size.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
