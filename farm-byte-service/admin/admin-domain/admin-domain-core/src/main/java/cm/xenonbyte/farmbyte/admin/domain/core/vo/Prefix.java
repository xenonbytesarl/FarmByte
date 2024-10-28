package cm.xenonbyte.farmbyte.admin.domain.core.vo;

import cm.xenonbyte.farmbyte.common.domain.validation.Assert;
import cm.xenonbyte.farmbyte.common.domain.vo.Text;
import jakarta.annotation.Nonnull;

import java.util.Objects;

/**
 * @author bamk
 * @version 1.0
 * @since 28/10/2024
 */
public record Prefix(Text text) {
    public Prefix(@Nonnull Text text) {
        this.text = Objects.requireNonNull(text);
    }

    @Nonnull
    public static Prefix of(Text value) {
        Assert.field("Prefix", value)
                .notNull()
                .notNull(value.getValue())
                .notEmpty();
        return new Prefix(value);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Prefix prefix = (Prefix) object;
        return Objects.equals(text, prefix.text);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(text);
    }
}
