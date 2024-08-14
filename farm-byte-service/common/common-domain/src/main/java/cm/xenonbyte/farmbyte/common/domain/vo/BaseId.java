package cm.xenonbyte.farmbyte.common.domain.vo;

import jakarta.annotation.Nonnull;

import java.util.Objects;

import static cm.xenonbyte.farmbyte.common.domain.constant.CommonDomainConstant.BASE_ID_IS_REQUIRED;

/**
 * @author bamk
 * @version 1.0
 * @since 09/08/2024
 */
public abstract class BaseId<T> {
    private final T value;

    protected BaseId(@Nonnull T value) {
        this.value = Objects.requireNonNull(value,BASE_ID_IS_REQUIRED);
    }

    @Nonnull
    public T getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseId<?> baseId = (BaseId<?>) o;
        return Objects.equals(value, baseId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
