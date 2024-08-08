package cm.xenonbyte.farmbyte.catalog.domain.core.uom;

import jakarta.annotation.Nonnull;

import java.util.Objects;
import java.util.UUID;

/**
 * @author bamk
 * @version 1.0
 * @since 06/08/2024
 */
public final class UomId {

    private final UUID identifier;

    public UomId(@Nonnull UUID identifier) {
        this.identifier = Objects.requireNonNull(identifier);
    }

    @Nonnull
    public static UomId of(@Nonnull UUID uuid) {
        return new UomId(Objects.requireNonNull(uuid));
    }

    @Nonnull
    public UUID getIdentifier() {
        return identifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UomId uomId = (UomId) o;
        return Objects.equals(identifier, uomId.identifier);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(identifier);
    }
}
