package cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement;

import cm.xenonbyte.farmbyte.common.domain.vo.BaseId;
import jakarta.annotation.Nonnull;

import java.util.UUID;

/**
 * @author bamk
 * @version 1.0
 * @since 13/10/2024
 */
public final class InventoryEmplacementId extends BaseId<UUID> {
    public InventoryEmplacementId(@Nonnull UUID value) {
        super(value);
    }
}
