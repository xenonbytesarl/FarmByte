package cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement;

import jakarta.annotation.Nonnull;

/**
 * @author bamk
 * @version 1.0
 * @since 13/10/2024
 */
public interface InventoryEmplacementService {
    @Nonnull InventoryEmplacement createInventoryEmplacement(@Nonnull InventoryEmplacement inventoryEmplacement);
}
