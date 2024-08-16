package cm.xenonbyte.farmbyte.catalog.domain.core.productcategory;

import cm.xenonbyte.farmbyte.common.domain.vo.BaseId;
import jakarta.annotation.Nonnull;

import java.util.UUID;

/**
 * @author bamk
 * @version 1.0
 * @since 16/08/2024
 */
public final class ProductCategoryId extends BaseId<UUID> {
    public ProductCategoryId(@Nonnull UUID value) {
        super(value);
    }
}
