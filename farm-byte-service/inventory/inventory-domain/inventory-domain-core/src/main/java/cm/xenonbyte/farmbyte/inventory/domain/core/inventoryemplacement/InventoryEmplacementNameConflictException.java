package cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement;

import cm.xenonbyte.farmbyte.common.domain.exception.BaseDomainConflictException;

/**
 * @author bamk
 * @version 1.0
 * @since 13/10/2024
 */
public final class InventoryEmplacementNameConflictException extends BaseDomainConflictException {
    public static final String INVENTORY_EMPLACEMENT_NAME_CONFLICT = "InventoryEmplacementNameConflictException.1";

    public InventoryEmplacementNameConflictException(Object[] args) {
        super(INVENTORY_EMPLACEMENT_NAME_CONFLICT, args);
    }
}
