package cm.xenonbyte.farmbyte.common.domain.vo;

import cm.xenonbyte.farmbyte.common.domain.validation.Assert;
import jakarta.annotation.Nonnull;

import java.util.Objects;

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
        Assert.field("Search keyword", text)
                .notNull();
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
