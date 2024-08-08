package cm.xenonbyte.farmbyte.catalog.domain.core.uomcategory;

import jakarta.annotation.Nonnull;

import java.util.Objects;
import java.util.UUID;

/**
 * @author bamk
 * @version 1.0
 * @since 06/08/2024
 */
public final class UomCategoryId {
    private final UUID identifier;

    public UomCategoryId(@Nonnull UUID identifier) {
        this.identifier = Objects.requireNonNull(identifier);
    }

    @Nonnull
    public static UomCategoryId of(@Nonnull UUID uuid) {
        return new UomCategoryId(Objects.requireNonNull(uuid));
    }

    @Nonnull
    public UUID getIdentifier() {
        return identifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UomCategoryId that = (UomCategoryId) o;
        return Objects.equals(identifier, that.identifier);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(identifier);
    }
}
