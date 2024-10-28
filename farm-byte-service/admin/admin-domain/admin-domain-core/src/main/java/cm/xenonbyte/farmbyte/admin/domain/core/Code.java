package cm.xenonbyte.farmbyte.admin.domain.core;

import cm.xenonbyte.farmbyte.common.domain.validation.Assert;
import cm.xenonbyte.farmbyte.common.domain.vo.Text;
import jakarta.annotation.Nonnull;

import java.util.Objects;

/**
 * @author bamk
 * @version 1.0
 * @since 28/10/2024
 */
public record Code(Text text) {
    public Code(@Nonnull Text text) {
        this.text = Objects.requireNonNull(text);
    }

    @Nonnull
    public static Code of(Text value) {
        Assert.field("Code", value)
                .notNull()
                .notNull(value.getValue())
                .notEmpty();
        return new Code(value);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Code code = (Code) object;
        return Objects.equals(text, code.text);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(text);
    }
}
