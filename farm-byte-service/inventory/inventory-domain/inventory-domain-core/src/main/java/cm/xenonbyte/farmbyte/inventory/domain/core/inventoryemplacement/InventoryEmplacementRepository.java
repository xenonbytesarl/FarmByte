package cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement;

import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import jakarta.annotation.Nonnull;

/**
 * @author bamk
 * @version 1.0
 * @since 13/10/2024
 */
public interface InventoryEmplacementRepository {
    boolean existsByParentId(@Nonnull InventoryEmplacementId parentId);

    InventoryEmplacement save(@Nonnull InventoryEmplacement inventoryEmplacement);

    boolean existsByName(@Nonnull Name name);
}
