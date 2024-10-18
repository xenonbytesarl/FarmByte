package cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement;

import cm.xenonbyte.farmbyte.common.domain.vo.Direction;
import cm.xenonbyte.farmbyte.common.domain.vo.Keyword;
import cm.xenonbyte.farmbyte.common.domain.vo.PageInfo;
import jakarta.annotation.Nonnull;

/**
 * @author bamk
 * @version 1.0
 * @since 13/10/2024
 */
public interface InventoryEmplacementService {
    @Nonnull InventoryEmplacement createInventoryEmplacement(@Nonnull InventoryEmplacement inventoryEmplacement);

    @Nonnull InventoryEmplacement findById(@Nonnull InventoryEmplacementId inventoryEmplacementId);

    PageInfo<InventoryEmplacement> findInventoryEmplacements(Integer page, Integer size, String sortAttribute, Direction direction);

    PageInfo<InventoryEmplacement> searchInventoryEmplacements(Integer page, Integer size, String sortAttribute, Direction direction, Keyword keyword);
}
