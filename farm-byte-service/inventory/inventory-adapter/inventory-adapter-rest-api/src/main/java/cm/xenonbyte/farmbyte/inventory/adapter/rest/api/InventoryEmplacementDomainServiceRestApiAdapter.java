package cm.xenonbyte.farmbyte.inventory.adapter.rest.api;

import cm.xenonbyte.farmbyte.common.domain.vo.Direction;
import cm.xenonbyte.farmbyte.common.domain.vo.Keyword;
import cm.xenonbyte.farmbyte.common.domain.vo.Text;
import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.inventoryemplacement.view.CreateInventoryEmplacementViewRequest;
import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.inventoryemplacement.view.CreateInventoryEmplacementViewResponse;
import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.inventoryemplacement.view.FindInventoryEmplacementByIdViewResponse;
import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.inventoryemplacement.view.FindInventoryEmplacementsPageInfoViewResponse;
import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.inventoryemplacement.view.SearchInventoryEmplacementsPageInfoViewResponse;
import cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacementId;
import cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacementService;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

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

    @Nonnull
    @Override
    public FindInventoryEmplacementByIdViewResponse findInventoryEmplacementById(@Nonnull UUID inventoryEmplacementId) {
        return inventoryEmplacementViewMapper.toFindInventoryEmplacementByIdViewResponse(
                inventoryEmplacementService.findInventoryEmplacementById(new InventoryEmplacementId(inventoryEmplacementId))
        );
    }

    @Nonnull
    @Override
    public FindInventoryEmplacementsPageInfoViewResponse findInventoryEmplacements(int page, int size, String attribute, String direction) {
        return inventoryEmplacementViewMapper.toFindInventoryEmplacementsPageInfoViewResponse(
                inventoryEmplacementService.findInventoryEmplacements(page, size, attribute, Direction.valueOf(direction))
        );
    }

    @Nonnull
    @Override
    public SearchInventoryEmplacementsPageInfoViewResponse searchInventoryEmplacements(int page, int size, String attribute, String direction, String keyword) {
        return inventoryEmplacementViewMapper.toSearchInventoryEmplacementsPageInfoViewResponse(
                inventoryEmplacementService.searchInventoryEmplacements(page, size, attribute, Direction.valueOf(direction), Keyword.of(Text.of(keyword)))
        );
    }
}
