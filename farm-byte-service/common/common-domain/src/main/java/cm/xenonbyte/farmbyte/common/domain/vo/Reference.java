package cm.xenonbyte.farmbyte.common.domain.vo;

import cm.xenonbyte.farmbyte.common.domain.validation.Assert;
import jakarta.annotation.Nonnull;

import java.util.Objects;

/**
 * @author bamk
 * @version 1.0
 * @since 21/08/2024
 */
public final class Reference {

    
    private final Text text;

    public Reference(@Nonnull Text text) {
        this.text = Objects.requireNonNull(text);
    }
    
    public static Reference of(@Nonnull Text value) {
        Assert.field("Reference", value)
                .notNull()
                .notNull(value.getValue())
                .notEmpty();
        return new Reference(value);
    }

    public Text getText() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reference reference = (Reference) o;
        return Objects.equals(text, reference.text);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(text);
    }
}
