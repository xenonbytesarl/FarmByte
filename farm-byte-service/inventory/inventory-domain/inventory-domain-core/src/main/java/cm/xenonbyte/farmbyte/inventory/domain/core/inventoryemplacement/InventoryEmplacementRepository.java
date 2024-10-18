package cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement;

import cm.xenonbyte.farmbyte.common.domain.vo.Name;
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
}
