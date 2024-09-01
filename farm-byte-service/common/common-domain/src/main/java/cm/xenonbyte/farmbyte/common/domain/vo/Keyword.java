package cm.xenonbyte.farmbyte.common.domain.vo;

import jakarta.annotation.Nonnull;

import java.util.Objects;

import static cm.xenonbyte.farmbyte.common.domain.constant.CommonDomainConstant.NAME_VALUE_IS_REQUIRED;

/**
 * @author bamk
 * @version 1.0
 * @since 30/08/2024
 */
public final class Keyword {
    private final Text text;

    public Keyword(@Nonnull Text text) {
        this.text = Objects.requireNonNull(text);
    }

    @Nonnull
    public static Keyword of(Text text) {
        if (text == null) {
            throw new IllegalArgumentException(NAME_VALUE_IS_REQUIRED);
        }
        return new Keyword(text);
    }

    @Nonnull
    public Text getText() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Keyword keyword = (Keyword) o;
        return Objects.equals(text, keyword.text);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(text);
    }
}
