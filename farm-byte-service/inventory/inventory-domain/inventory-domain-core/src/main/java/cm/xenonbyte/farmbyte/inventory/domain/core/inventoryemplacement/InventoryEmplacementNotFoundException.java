package cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement;

import cm.xenonbyte.farmbyte.common.domain.exception.BaseDomainNotFoundException;

/**
 * @author bamk
 * @version 1.0
 * @since 19/10/2024
 */
public final class InventoryEmplacementNotFoundException extends BaseDomainNotFoundException {
    public static final String INVENTORY_EMPLACEMENT_ID_NOT_FOUND = "InventoryEmplacementNotFoundException.1";

    public InventoryEmplacementNotFoundException(Object[] args) {
        super(INVENTORY_EMPLACEMENT_ID_NOT_FOUND, args);
    }
}
