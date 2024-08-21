package cm.xenonbyte.farmbyte.catalog.domain.core.product;

import cm.xenonbyte.farmbyte.common.domain.vo.BaseId;
import jakarta.annotation.Nonnull;

import java.util.UUID;

/**
 * @author bamk
 * @version 1.0
 * @since 20/08/2024
 */
public final class ProductId extends BaseId<UUID> {
    public ProductId(@Nonnull UUID value) {
        super(value);
    }
}
