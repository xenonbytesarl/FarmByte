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
public record Suffix(Text text) {

    public Suffix(@Nonnull Text text) {
        this.text = Objects.requireNonNull(text);
    }

    @Nonnull
    public static Suffix of(Text value) {
        Assert.field("Suffix", value)
                .notNull()
                .notNull(value.getValue())
                .notEmpty();
        return new Suffix(value);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Suffix suffix = (Suffix) object;
        return Objects.equals(text, suffix.text);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(text);
    }
}
