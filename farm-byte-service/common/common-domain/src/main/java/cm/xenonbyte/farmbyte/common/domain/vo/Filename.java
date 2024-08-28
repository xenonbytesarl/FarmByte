package cm.xenonbyte.farmbyte.common.domain.vo;

import jakarta.annotation.Nonnull;

import java.util.Objects;

import static cm.xenonbyte.farmbyte.common.domain.constant.CommonDomainConstant.NAME_VALUE_IS_REQUIRED;

/**
 * @author bamk
 * @version 1.0
 * @since 28/08/2024
 */
public final class Filename {

    private final Text text;

    public Filename(@Nonnull Text text) {
        this.text = Objects.requireNonNull(text);
    }

    @Nonnull
    public static Filename of(Text value) {
        if (value.isEmpty()) {
            throw new IllegalArgumentException(NAME_VALUE_IS_REQUIRED);
        }
        return new Filename(value);
    }

    @Nonnull
    public Text getText() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Filename name = (Filename) o;
        return Objects.equals(text, name.text);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(text);
    }
}
