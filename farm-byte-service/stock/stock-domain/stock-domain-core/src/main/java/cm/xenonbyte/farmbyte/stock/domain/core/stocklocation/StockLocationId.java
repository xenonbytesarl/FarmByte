package cm.xenonbyte.farmbyte.stock.domain.core.stocklocation;

import cm.xenonbyte.farmbyte.common.domain.vo.BaseId;
import jakarta.annotation.Nonnull;

import java.util.UUID;

/**
 * @author bamk
 * @version 1.0
 * @since 13/10/2024
 */
public final class StockLocationId extends BaseId<UUID> {
    public StockLocationId(@Nonnull UUID value) {
        super(value);
    }
}
