package cm.xenonbyte.farmbyte.inventory.adapter.data.access.inmemory;

import cm.xenonbyte.farmbyte.common.domain.vo.Direction;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.PageInfo;
import cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacement;
import cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacementId;
import cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacementRepository;
import jakarta.annotation.Nonnull;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author bamk
 * @version 1.0
 * @since 13/10/2024
 */
public final class InMemoryInventoryEmplacementRepository implements InventoryEmplacementRepository {

    private final Map<InventoryEmplacementId, InventoryEmplacement> inventoryEmplacements = new LinkedHashMap<>();


    @Override
    public boolean existsById(@Nonnull InventoryEmplacementId parentId) {
        return inventoryEmplacements.containsKey(parentId);
    }

    @Override
    public InventoryEmplacement save(@Nonnull InventoryEmplacement inventoryEmplacement) {
        inventoryEmplacements.put(inventoryEmplacement.getId(), inventoryEmplacement);
        return inventoryEmplacement;
    }

    @Override
    public boolean existsByNameIgnoreCase(@Nonnull Name name) {
        return inventoryEmplacements.values().stream()
                .anyMatch(emplacement -> emplacement.getName().getText().getValue().equalsIgnoreCase(name.getText().getValue()));
    }

    @Nonnull
    @Override
    public Optional<InventoryEmplacement> findById(@Nonnull InventoryEmplacementId inventoryEmplacementId) {
        InventoryEmplacement inventoryEmplacement = inventoryEmplacements.get(inventoryEmplacementId);
        return inventoryEmplacement != null ? Optional.of(inventoryEmplacement) : Optional.empty();
    }

    @Override
    public PageInfo<InventoryEmplacement> findAll(Integer page, Integer size, String sortAttribute, Direction direction) {
        PageInfo<InventoryEmplacement> inventoryEmplacementPageInfo = new PageInfo<>();
        Comparator<InventoryEmplacement> comparing = Comparator.comparing((InventoryEmplacement a) -> a.getName().getText().getValue());
        return inventoryEmplacementPageInfo.with(
                page,
                size,
                inventoryEmplacements.values().stream()
                        .sorted(Direction.ASC.equals(direction) ? comparing : comparing.reversed())
                        .toList()
        );
    }
}
