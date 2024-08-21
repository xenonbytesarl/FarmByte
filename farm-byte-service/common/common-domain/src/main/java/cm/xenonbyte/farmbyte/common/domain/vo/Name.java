package cm.xenonbyte.farmbyte.common.domain.vo;

import jakarta.annotation.Nonnull;

import java.util.Objects;

import static cm.xenonbyte.farmbyte.common.domain.constant.CommonDomainConstant.NAME_VALUE_IS_REQUIRED;

/**
 * @author bamk
 * @version 1.0
 * @since 06/08/2024
 */
public final class Name {

    private final Text text;

    public Name(@Nonnull Text text) {
        this.text = Objects.requireNonNull(text);
    }

    @Nonnull
    public static Name of(Text value) {
        if (value.isEmpty()) {
            throw new IllegalArgumentException(NAME_VALUE_IS_REQUIRED);
        }
        return new Name(value);
    }

    @Nonnull
    public Text getText() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Name name = (Name) o;
        return Objects.equals(text, name.text);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(text);
    }
}
