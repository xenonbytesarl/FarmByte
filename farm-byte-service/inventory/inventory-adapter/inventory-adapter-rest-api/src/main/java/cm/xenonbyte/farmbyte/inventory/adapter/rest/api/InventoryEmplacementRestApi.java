package cm.xenonbyte.farmbyte.inventory.adapter.rest.api;

import cm.xenonbyte.farmbyte.common.adapter.api.messages.MessageUtil;
import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.uom.InventoryEmplacementsApi;
import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.uom.view.CreateInventoryEmplacementApiViewResponse;
import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.uom.view.CreateInventoryEmplacementViewRequest;
import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.uom.view.CreateInventoryEmplacementViewResponse;
import jakarta.annotation.Nonnull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.Objects;

import static cm.xenonbyte.farmbyte.common.adapter.api.constant.CommonAdapterRestApi.BODY;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.CREATED;

/**
 * @author bamk
 * @version 1.0
 * @since 17/10/2024
 */
@RestController
public class InventoryEmplacementRestApi implements InventoryEmplacementsApi {

    public static final String INVENTORY_EMPLACEMENT_CREATED_SUCCESSFULLY = "InventoryEmplacementRestApi.1";

    private final InventoryEmplacementServiceRestApiAdapter inventoryEmplacementServiceRestApiAdapter;

    public InventoryEmplacementRestApi(@Nonnull InventoryEmplacementServiceRestApiAdapter inventoryEmplacementServiceRestApiAdapter) {
        this.inventoryEmplacementServiceRestApiAdapter = Objects.requireNonNull(inventoryEmplacementServiceRestApiAdapter);
    }

    @Override
    public ResponseEntity<CreateInventoryEmplacementApiViewResponse> createInventoryEmplacement(
            String acceptLanguage, CreateInventoryEmplacementViewRequest createInventoryEmplacementViewRequest) {
        CreateInventoryEmplacementViewResponse createInventoryEmplacementViewResponse =
                inventoryEmplacementServiceRestApiAdapter.createInventoryEmplacement(createInventoryEmplacementViewRequest);
        return ResponseEntity.status(CREATED).body(
                new CreateInventoryEmplacementApiViewResponse()
                        .code(CREATED.value())
                        .status(CREATED.name())
                        .success(true)
                        .timestamp(ZonedDateTime.now().toString())
                        .message(MessageUtil.getMessage(INVENTORY_EMPLACEMENT_CREATED_SUCCESSFULLY, Locale.forLanguageTag(acceptLanguage), ""))
                        .data(of(BODY, createInventoryEmplacementViewResponse)));
    }
}
