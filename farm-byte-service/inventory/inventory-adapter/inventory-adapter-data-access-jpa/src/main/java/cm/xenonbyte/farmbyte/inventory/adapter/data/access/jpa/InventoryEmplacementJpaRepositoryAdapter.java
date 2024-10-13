package cm.xenonbyte.farmbyte.inventory.adapter.data.access.jpa;

import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacement;
import cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacementId;
import cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacementRepository;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author bamk
 * @version 1.0
 * @since 13/10/2024
 */
@Slf4j
@Service
public final class InventoryEmplacementJpaRepositoryAdapter implements InventoryEmplacementRepository {
    @Override
    public boolean existsByParentId(@Nonnull InventoryEmplacementId parentId) {
        return false;
    }

    @Override
    public InventoryEmplacement save(@Nonnull InventoryEmplacement inventoryEmplacement) {
        return null;
    }

    @Override
    public boolean existsByName(@Nonnull Name name) {
        return false;
    }
}
