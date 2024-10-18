package cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement;

import cm.xenonbyte.farmbyte.common.domain.annotation.DomainService;
import jakarta.annotation.Nonnull;

import static java.util.Objects.requireNonNull;

/**
 * @author bamk
 * @version 1.0
 * @since 13/10/2024
 */
@DomainService
public final class InventoryEmplacementDomainService implements InventoryEmplacementService {

    private final InventoryEmplacementRepository inventoryEmplacementRepository;

    public InventoryEmplacementDomainService(@Nonnull InventoryEmplacementRepository inventoryEmplacementRepository) {
        this.inventoryEmplacementRepository = requireNonNull(inventoryEmplacementRepository);
    }

    @Nonnull
    @Override
    public InventoryEmplacement createInventoryEmplacement(@Nonnull InventoryEmplacement inventoryEmplacement) {
        inventoryEmplacement.validateMandatoryFields();
        verifyParent(inventoryEmplacement.getParentId());
        verifyName(inventoryEmplacement);
        inventoryEmplacement.initializeWithDefaults();
        return inventoryEmplacementRepository.save(inventoryEmplacement);
    }

    @Nonnull
    @Override
    public InventoryEmplacement findById(@Nonnull InventoryEmplacementId inventoryEmplacementId) {
        return inventoryEmplacementRepository.findById(inventoryEmplacementId).orElseThrow(
                () -> new InventoryEmplacementNotFoundException(new String[]{inventoryEmplacementId.getValue().toString()})
        );
    }

    private void verifyName(@Nonnull InventoryEmplacement inventoryEmplacement) {
        if(inventoryEmplacement.getId() == null && inventoryEmplacementRepository.existsByNameIgnoreCase(inventoryEmplacement.getName())) {
            throw new InventoryEmplacementNameConflictException(new String[]{inventoryEmplacement.getName().getText().getValue()});
        }
    }

    private void verifyParent(@Nonnull InventoryEmplacementId parentId) {
        if(parentId != null && !inventoryEmplacementRepository.existsById(parentId)) {
            throw new InventoryEmplacementParentIdNotFoundException(new String[] {parentId.getValue().toString()});
        }
    }
}
