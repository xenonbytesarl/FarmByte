package cm.xenonbyte.farmbyte.common.domain.entity;

import jakarta.annotation.Nonnull;

import java.util.Objects;

/**
 * @author bamk
 * @version 1.0
 * @since 09/08/2024
 */
public abstract class BaseEntity<ID> {
    private ID id;

    @Nonnull
    public ID getId() {
        return id;
    }

    public void setId(@Nonnull ID id) {
        this.id = Objects.requireNonNull(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity<?> that = (BaseEntity<?>) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
