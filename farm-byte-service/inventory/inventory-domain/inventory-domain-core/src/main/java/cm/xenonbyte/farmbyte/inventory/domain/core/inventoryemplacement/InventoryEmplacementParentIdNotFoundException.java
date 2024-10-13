package cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement;

import cm.xenonbyte.farmbyte.common.domain.exception.BaseDomainNotFoundException;

/**
 * @author bamk
 * @version 1.0
 * @since 13/10/2024
 */
public final class InventoryEmplacementParentIdNotFoundException extends BaseDomainNotFoundException {
    public static final String INVENTORY_EMPLACEMENT_PARENT_ID_NOT_FOUND = "InventoryEmplacementParentIdNotFoundException.1";

    public InventoryEmplacementParentIdNotFoundException(Object[] args) {
        super(INVENTORY_EMPLACEMENT_PARENT_ID_NOT_FOUND, args);
    }
}
