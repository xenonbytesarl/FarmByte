package cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement;

import cm.xenonbyte.farmbyte.common.domain.vo.Direction;
import cm.xenonbyte.farmbyte.common.domain.vo.Keyword;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.PageInfo;
import jakarta.annotation.Nonnull;

import java.util.Optional;

/**
 * @author bamk
 * @version 1.0
 * @since 13/10/2024
 */
public interface InventoryEmplacementRepository {
    boolean existsById(@Nonnull InventoryEmplacementId inventoryEmplacementId);

    @Nonnull InventoryEmplacement save(@Nonnull InventoryEmplacement inventoryEmplacement);

    boolean existsByNameIgnoreCase(@Nonnull Name name);

    @Nonnull
    Optional<InventoryEmplacement> findById(@Nonnull InventoryEmplacementId inventoryEmplacementId);

    PageInfo<InventoryEmplacement> findAll(Integer page, Integer size, String sortAttribute, Direction direction);

    PageInfo<InventoryEmplacement> search(Integer page, Integer size, String sortAttribute, Direction direction, Keyword keyword);
}
