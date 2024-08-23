package cm.xenonbyte.farmbyte.common.domain.vo;

import jakarta.annotation.Nonnull;

import java.util.Objects;

import static cm.xenonbyte.farmbyte.common.domain.constant.CommonDomainConstant.REFERENCE_VALUE_IS_REQUIRED;

/**
 * @author bamk
 * @version 1.0
 * @since 21/08/2024
 */
public final class Reference {

    
    private final Text text;

    public Reference(@Nonnull Text text) {
        this.text = Objects.requireNonNull(text);
    }
    
    public static Reference of(@Nonnull Text value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException(REFERENCE_VALUE_IS_REQUIRED);
        }
        return new Reference(value);
    }

    public Text getText() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reference reference = (Reference) o;
        return Objects.equals(text, reference.text);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(text);
    }
}
