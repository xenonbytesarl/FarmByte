package cm.xenonbyte.farmbyte.inventory.adapter.rest.api;

import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.uom.view.CreateInventoryEmplacementViewRequest;
import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.uom.view.CreateInventoryEmplacementViewResponse;
import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.uom.view.FindInventoryEmplacementByIdViewResponse;
import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.uom.view.FindInventoryEmplacementsPageInfoViewResponse;
import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.uom.view.SearchInventoryEmplacementsPageInfoViewResponse;
import jakarta.annotation.Nonnull;
import jakarta.validation.Valid;

import java.util.UUID;

/**
 * @author bamk
 * @version 1.0
 * @since 17/10/2024
 */
public interface InventoryEmplacementServiceRestApiAdapter {
    @Nonnull @Valid CreateInventoryEmplacementViewResponse createInventoryEmplacement(@Nonnull @Valid CreateInventoryEmplacementViewRequest createInventoryEmplacementViewRequest);

    @Nonnull @Valid FindInventoryEmplacementByIdViewResponse findInventoryEmplacementById(@Nonnull UUID inventoryEmplacementId);

    @Nonnull @Valid FindInventoryEmplacementsPageInfoViewResponse findInventoryEmplacements(int page, int size, String attribute, String direction);

    @Nonnull @Valid SearchInventoryEmplacementsPageInfoViewResponse searchInventoryEmplacements(int page, int size, String attribute, String direction, String keyword);
}
