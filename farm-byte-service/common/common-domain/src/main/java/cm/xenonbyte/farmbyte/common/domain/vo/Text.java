package cm.xenonbyte.farmbyte.common.domain.vo;

import jakarta.annotation.Nonnull;

import java.util.Objects;

/**
 * @author bamk
 * @version 1.0
 * @since 20/08/2024
 */
public class Text {

    protected final String value;

    public Text(@Nonnull String value) {
        this.value = Objects.requireNonNull(value);
    }

    public static Text of(String value) {
        return new Text(value);
    }

    public Boolean isEmpty() {
        return value.isEmpty();
    }

    @Nonnull
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Text text = (Text) o;
        return Objects.equals(value, text.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }


    public Text concat(@Nonnull String value) {
        return new Text(this.value.concat(value));
    }

    public Text replace(@Nonnull String oldValue, @Nonnull String newValue) {
        return Text.of(this.value.replace(oldValue, newValue));
    }
}
