package cm.xenonbyte.farmbyte.inventory.adapter.rest.api;

import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.uom.view.CreateInventoryEmplacementViewRequest;
import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.uom.view.CreateInventoryEmplacementViewResponse;
import jakarta.annotation.Nonnull;
import jakarta.validation.Valid;

/**
 * @author bamk
 * @version 1.0
 * @since 17/10/2024
 */
public interface InventoryEmplacementServiceRestApiAdapter {
    @Nonnull @Valid CreateInventoryEmplacementViewResponse createInventoryEmplacement(@Nonnull @Valid CreateInventoryEmplacementViewRequest createInventoryEmplacementViewRequest);
}
