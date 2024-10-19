package cm.xenonbyte.farmbyte.inventory.adapter.data.access.jpa;

import cm.xenonbyte.farmbyte.common.domain.vo.Direction;
import cm.xenonbyte.farmbyte.common.domain.vo.Keyword;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.PageInfo;
import cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacement;
import cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacementId;
import cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacementRepository;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

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
    public boolean existsById(@Nonnull InventoryEmplacementId inventoryEmplacementId) {
        return inventoryEmplacementRepositoryJpa.existsById(inventoryEmplacementId.getValue());
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
    public boolean existsByNameIgnoreCase(@Nonnull Name name) {
        return inventoryEmplacementRepositoryJpa.existsByNameIgnoreCase(name.getText().getValue());
    }

    @Nonnull
    @Override
    public Optional<InventoryEmplacement> findById(@Nonnull InventoryEmplacementId inventoryEmplacementId) {
        return inventoryEmplacementRepositoryJpa.findById(inventoryEmplacementId.getValue())
                .map(inventoryEmplacementJpaMapper::toInventoryEmplacement);
    }

    @Override
    public PageInfo<InventoryEmplacement> findAll(Integer page, Integer size, String sortAttribute, Direction direction) {
        Sort.Direction sortDirection = Direction.ASC.equals(direction) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Page<InventoryEmplacementJpa> inventoryEmplacementJpaPage =
                inventoryEmplacementRepositoryJpa.findAll(PageRequest.of(page, size, sortDirection, sortAttribute));
        return new PageInfo<>(
                inventoryEmplacementJpaPage.isFirst(),
                inventoryEmplacementJpaPage.isLast(),
                inventoryEmplacementJpaPage.getSize(),
                inventoryEmplacementJpaPage.getTotalElements(),
                inventoryEmplacementJpaPage.getTotalPages(),
                inventoryEmplacementJpaPage.getContent().stream().map(inventoryEmplacementJpaMapper::toInventoryEmplacement).toList()
        );
    }

    @Override
    public PageInfo<InventoryEmplacement> search(Integer page, Integer size, String sortAttribute, Direction direction, Keyword keyword) {
        Sort.Direction sortDirection = Direction.ASC.equals(direction) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Page<InventoryEmplacementJpa> inventoryEmplacementJpaPage =
                inventoryEmplacementRepositoryJpa.search(PageRequest.of(page, size, sortDirection, sortAttribute), keyword.getText().getValue());
        return new PageInfo<>(
                inventoryEmplacementJpaPage.isFirst(),
                inventoryEmplacementJpaPage.isLast(),
                inventoryEmplacementJpaPage.getSize(),
                inventoryEmplacementJpaPage.getTotalElements(),
                inventoryEmplacementJpaPage.getTotalPages(),
                inventoryEmplacementJpaPage.getContent().stream().map(inventoryEmplacementJpaMapper::toInventoryEmplacement).toList()
        );
    }
}
