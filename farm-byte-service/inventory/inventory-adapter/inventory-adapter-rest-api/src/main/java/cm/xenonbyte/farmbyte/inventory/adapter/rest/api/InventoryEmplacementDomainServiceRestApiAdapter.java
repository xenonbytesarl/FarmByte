package cm.xenonbyte.farmbyte.inventory.adapter.rest.api;

import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.uom.view.CreateInventoryEmplacementViewRequest;
import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.uom.view.CreateInventoryEmplacementViewResponse;
import cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacementService;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * @author bamk
 * @version 1.0
 * @since 17/10/2024
 */
@Slf4j
@Service
public class InventoryEmplacementDomainServiceRestApiAdapter implements InventoryEmplacementServiceRestApiAdapter {

    private final InventoryEmplacementService inventoryEmplacementService;
    private final InventoryEmplacementViewMapper inventoryEmplacementViewMapper;

    public InventoryEmplacementDomainServiceRestApiAdapter(
            @Nonnull InventoryEmplacementService inventoryEmplacementService,
            @Nonnull InventoryEmplacementViewMapper inventoryEmplacementViewMapper) {

        this.inventoryEmplacementService = Objects.requireNonNull(inventoryEmplacementService);
        this.inventoryEmplacementViewMapper = Objects.requireNonNull(inventoryEmplacementViewMapper);
    }

    @Nonnull
    @Override
    @Transactional
    public CreateInventoryEmplacementViewResponse createInventoryEmplacement(@Nonnull CreateInventoryEmplacementViewRequest createInventoryEmplacementViewRequest) {
        return inventoryEmplacementViewMapper.toCreateInventoryEmplacementViewResponse(
                inventoryEmplacementService.createInventoryEmplacement(
                        inventoryEmplacementViewMapper.toInventoryEmplacement(createInventoryEmplacementViewRequest)
                )
        );
    }
}
