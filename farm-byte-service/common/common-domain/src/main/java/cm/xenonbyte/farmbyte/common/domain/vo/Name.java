package cm.xenonbyte.farmbyte.common.domain.vo;

import jakarta.annotation.Nonnull;

import java.util.Objects;

/**
 * @author bamk
 * @version 1.0
 * @since 06/08/2024
 */
public final class Name {

    private final String value;

    public Name(@Nonnull String value) {
        this.value = Objects.requireNonNull(value);
    }

    @Nonnull
    public static Name of(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        return new Name(value);
    }

    @Nonnull
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Name name = (Name) o;
        return Objects.equals(value, name.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
