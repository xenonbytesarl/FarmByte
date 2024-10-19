package cm.xenonbyte.farmbyte.inventory.adapter.rest.api;

import cm.xenonbyte.farmbyte.common.adapter.api.messages.MessageUtil;
import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.uom.InventoryEmplacementsApi;
import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.uom.view.CreateInventoryEmplacementApiViewResponse;
import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.uom.view.CreateInventoryEmplacementViewRequest;
import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.uom.view.CreateInventoryEmplacementViewResponse;
import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.uom.view.FindInventoryEmplacementByIdViewApiResponse;
import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.uom.view.FindInventoryEmplacementByIdViewResponse;
import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.uom.view.FindInventoryEmplacementsPageInfoViewResponse;
import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.uom.view.FindInventoryEmplacementsViewApiResponse;
import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.uom.view.SearchInventoryEmplacementsPageInfoViewResponse;
import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.uom.view.SearchInventoryEmplacementsViewApiResponse;
import jakarta.annotation.Nonnull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import static cm.xenonbyte.farmbyte.common.adapter.api.constant.CommonAdapterRestApi.BODY;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

/**
 * @author bamk
 * @version 1.0
 * @since 17/10/2024
 */
@RestController
public class InventoryEmplacementRestApi implements InventoryEmplacementsApi {

    public static final String INVENTORY_EMPLACEMENT_CREATED_SUCCESSFULLY = "InventoryEmplacementRestApi.1";
    public static final String INVENTORY_EMPLACEMENT_FIND_SUCCESSFULLY = "InventoryEmplacementRestApi.2";
    public static final String INVENTORY_EMPLACEMENTS_FIND_SUCCESSFULLY = "InventoryEmplacementRestApi.3";

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

    @Override
    public ResponseEntity<FindInventoryEmplacementByIdViewApiResponse> findInventoryEmplacementById(String acceptLanguage, UUID inventoryEmplacementId) {
        FindInventoryEmplacementByIdViewResponse findInventoryEmplacementByIdViewResponse =
                inventoryEmplacementServiceRestApiAdapter.findInventoryEmplacementById(inventoryEmplacementId);
        return ResponseEntity.status(OK).body(
                new FindInventoryEmplacementByIdViewApiResponse()
                        .code(OK.value())
                        .status(OK.name())
                        .success(true)
                        .timestamp(ZonedDateTime.now().toString())
                        .message(MessageUtil.getMessage(INVENTORY_EMPLACEMENT_FIND_SUCCESSFULLY, Locale.forLanguageTag(acceptLanguage), ""))
                        .data(of(BODY, findInventoryEmplacementByIdViewResponse)));
    }

    @Override
    public ResponseEntity<FindInventoryEmplacementsViewApiResponse> findInventoryEmplacements(String acceptLanguage, Integer page, Integer size, String attribute, String direction) {
        FindInventoryEmplacementsPageInfoViewResponse findInventoryEmplacementsPageInfoViewResponse =
                inventoryEmplacementServiceRestApiAdapter.findInventoryEmplacements(page, size, attribute, direction);
        return ResponseEntity.status(OK).body(
                new FindInventoryEmplacementsViewApiResponse()
                        .code(OK.value())
                        .status(OK.name())
                        .success(true)
                        .timestamp(ZonedDateTime.now().toString())
                        .message(MessageUtil.getMessage(INVENTORY_EMPLACEMENTS_FIND_SUCCESSFULLY, Locale.forLanguageTag(acceptLanguage), ""))
                        .data(of(BODY, findInventoryEmplacementsPageInfoViewResponse)));
    }

    @Override
    public ResponseEntity<SearchInventoryEmplacementsViewApiResponse> searchInventoryEmplacements(String acceptLanguage, Integer page, Integer size, String attribute, String direction, String keyword) {
        SearchInventoryEmplacementsPageInfoViewResponse searchInventoryEmplacementsPageInfoViewResponse =
                inventoryEmplacementServiceRestApiAdapter.searchInventoryEmplacements(page, size, attribute, direction, keyword);
        return ResponseEntity.status(OK).body(
                new SearchInventoryEmplacementsViewApiResponse()
                        .code(OK.value())
                        .status(OK.name())
                        .success(true)
                        .timestamp(ZonedDateTime.now().toString())
                        .message(MessageUtil.getMessage(INVENTORY_EMPLACEMENTS_FIND_SUCCESSFULLY, Locale.forLanguageTag(acceptLanguage), ""))
                        .data(of(BODY, searchInventoryEmplacementsPageInfoViewResponse)));
    }
}
