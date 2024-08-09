package cm.xenonbyte.farmbyte.catalog.domain.core.uomcategory;

import cm.xenonbyte.farmbyte.common.domain.vo.BaseId;
import jakarta.annotation.Nonnull;

import java.util.UUID;

/**
 * @author bamk
 * @version 1.0
 * @since 06/08/2024
 */
public final class UomCategoryId extends BaseId<UUID> {

    public UomCategoryId(@Nonnull UUID value) {
        super(value);
    }

}
