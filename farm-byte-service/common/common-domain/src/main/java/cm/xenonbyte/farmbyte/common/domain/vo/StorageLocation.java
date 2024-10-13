package cm.xenonbyte.farmbyte.common.domain.vo;

import cm.xenonbyte.farmbyte.common.domain.validation.Assert;
import jakarta.annotation.Nonnull;

import java.util.Objects;

import static cm.xenonbyte.farmbyte.common.domain.constant.CommonDomainConstant.ROOT_STORAGE_PATH;
import static cm.xenonbyte.farmbyte.common.domain.constant.CommonDomainConstant.SLASH;

/**
 * @author bamk
 * @version 1.0
 * @since 27/08/2024
 */
public final class StorageLocation {

    private final Text path;

    public StorageLocation(@Nonnull Text path) {
        this.path = Objects.requireNonNull(path);
    }

    public static StorageLocation of(@Nonnull Text location) {
        Assert.field("Storage location", location)
                .notNull()
                .notNull(location.getValue())
                .notEmpty();
        return new StorageLocation(location);
    }

    public static StorageLocation computeStoragePtah(String location) {
        return new StorageLocation(Text.of(ROOT_STORAGE_PATH).concat(SLASH).concat(location));
    }

    public Text getPath() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StorageLocation that = (StorageLocation) o;
        return Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(path);
    }
}
