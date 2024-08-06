package cm.xenonbyte.farmbyte.uom.vo;

import jakarta.annotation.Nonnull;

import java.util.Objects;
import java.util.UUID;

/**
 * @author bamk
 * @version 1.0
 * @since 06/08/2024
 */
public final class UomId {
    private final UUID id;

    private UomId(@Nonnull UUID id) {
        this.id = Objects.requireNonNull(id);
    }

    public static UomId of(@Nonnull UUID uuid) {
        return new UomId(Objects.requireNonNull(uuid));
    }

    @Nonnull
    public UUID getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UomId uomId = (UomId) o;
        return Objects.equals(id, uomId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
