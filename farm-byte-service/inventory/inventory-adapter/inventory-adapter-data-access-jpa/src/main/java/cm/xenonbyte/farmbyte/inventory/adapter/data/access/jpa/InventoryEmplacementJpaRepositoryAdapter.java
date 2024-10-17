package cm.xenonbyte.farmbyte.inventory.adapter.data.access.jpa;

import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacement;
import cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacementId;
import cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacementRepository;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * @author bamk
 * @version 1.0
 * @since 13/10/2024
 */
@Slf4j
@Service
public class InventoryEmplacementJpaRepositoryAdapter implements InventoryEmplacementRepository {


    private final InventoryEmplacementRepositoryJpa inventoryEmplacementRepositoryJpa;
    private final InventoryEmplacementJpaMapper inventoryEmplacementJpaMapper;

    public InventoryEmplacementJpaRepositoryAdapter(
            final @Nonnull InventoryEmplacementRepositoryJpa inventoryEmplacementRepositoryJpa,
            final @Nonnull InventoryEmplacementJpaMapper inventoryEmplacementJpaMapper) {
        this.inventoryEmplacementRepositoryJpa = Objects.requireNonNull(inventoryEmplacementRepositoryJpa);
        this.inventoryEmplacementJpaMapper = Objects.requireNonNull(inventoryEmplacementJpaMapper);
    }


    @Override
    @Transactional(readOnly = true)
    public boolean existsByParentId(@Nonnull InventoryEmplacementId parentId) {
        return inventoryEmplacementRepositoryJpa.existsByParentJpa(
                InventoryEmplacementJpa.builder().id(parentId.getValue()).build());
    }

    @Override
    @Transactional
    public InventoryEmplacement save(@Nonnull InventoryEmplacement inventoryEmplacement) {
        return inventoryEmplacementJpaMapper.toInventoryEmplacement(
                inventoryEmplacementRepositoryJpa.save(
                        inventoryEmplacementJpaMapper.toInventoryEmplacementJpa(inventoryEmplacement)
                )
        );
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(@Nonnull Name name) {
        return inventoryEmplacementRepositoryJpa.existsByName(name.getText().getValue());
    }
}
