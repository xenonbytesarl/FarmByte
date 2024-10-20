package cm.xenonbyte.farmbyte.stock.adapter.rest.api;

import cm.xenonbyte.farmbyte.common.adapter.api.messages.MessageUtil;
import cm.xenonbyte.farmbyte.stock.adapter.rest.api.generated.stocklocation.StockLocationsApi;
import cm.xenonbyte.farmbyte.stock.adapter.rest.api.generated.stocklocation.view.CreateStockLocationApiViewResponse;
import cm.xenonbyte.farmbyte.stock.adapter.rest.api.generated.stocklocation.view.CreateStockLocationViewRequest;
import cm.xenonbyte.farmbyte.stock.adapter.rest.api.generated.stocklocation.view.CreateStockLocationViewResponse;
import cm.xenonbyte.farmbyte.stock.adapter.rest.api.generated.stocklocation.view.FindStockLocationByIdViewApiResponse;
import cm.xenonbyte.farmbyte.stock.adapter.rest.api.generated.stocklocation.view.FindStockLocationByIdViewResponse;
import cm.xenonbyte.farmbyte.stock.adapter.rest.api.generated.stocklocation.view.FindStockLocationsPageInfoViewResponse;
import cm.xenonbyte.farmbyte.stock.adapter.rest.api.generated.stocklocation.view.FindStockLocationsViewApiResponse;
import cm.xenonbyte.farmbyte.stock.adapter.rest.api.generated.stocklocation.view.SearchStockLocationsPageInfoViewResponse;
import cm.xenonbyte.farmbyte.stock.adapter.rest.api.generated.stocklocation.view.SearchStockLocationsViewApiResponse;
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
public class StockLocationRestApi implements StockLocationsApi {

    public static final String STOCK_LOCATION_CREATED_SUCCESSFULLY = "StockLocationRestApi.1";
    public static final String STOCK_LOCATION_FIND_SUCCESSFULLY = "StockLocationRestApi.2";
    public static final String STOCK_LOCATIONS_FIND_SUCCESSFULLY = "StockLocationRestApi.3";

    private final StockLocationServiceRestApiAdapter stockLocationServiceRestApiAdapter;

    public StockLocationRestApi(@Nonnull StockLocationServiceRestApiAdapter stockLocationServiceRestApiAdapter) {
        this.stockLocationServiceRestApiAdapter = Objects.requireNonNull(stockLocationServiceRestApiAdapter);
    }

    @Override
    public ResponseEntity<CreateStockLocationApiViewResponse> createStockLocation(
            String acceptLanguage, CreateStockLocationViewRequest createStockLocationViewRequest) {
        CreateStockLocationViewResponse createStockLocationViewResponse =
                stockLocationServiceRestApiAdapter.createStockLocation(createStockLocationViewRequest);
        return ResponseEntity.status(CREATED).body(
                new CreateStockLocationApiViewResponse()
                        .code(CREATED.value())
                        .status(CREATED.name())
                        .success(true)
                        .timestamp(ZonedDateTime.now().toString())
                        .message(MessageUtil.getMessage(STOCK_LOCATION_CREATED_SUCCESSFULLY, Locale.forLanguageTag(acceptLanguage), ""))
                        .data(of(BODY, createStockLocationViewResponse)));
    }

    @Override
    public ResponseEntity<FindStockLocationByIdViewApiResponse> findStockLocationById(String acceptLanguage, UUID stockLocationId) {
        FindStockLocationByIdViewResponse findStockLocationByIdViewResponse =
                stockLocationServiceRestApiAdapter.findStockLocationById(stockLocationId);
        return ResponseEntity.status(OK).body(
                new FindStockLocationByIdViewApiResponse()
                        .code(OK.value())
                        .status(OK.name())
                        .success(true)
                        .timestamp(ZonedDateTime.now().toString())
                        .message(MessageUtil.getMessage(STOCK_LOCATION_FIND_SUCCESSFULLY, Locale.forLanguageTag(acceptLanguage), ""))
                        .data(of(BODY, findStockLocationByIdViewResponse)));
    }

    @Override
    public ResponseEntity<FindStockLocationsViewApiResponse> findStockLocations(String acceptLanguage, Integer page, Integer size, String attribute, String direction) {
        FindStockLocationsPageInfoViewResponse findStockLocationsPageInfoViewResponse =
                stockLocationServiceRestApiAdapter.findStockLocations(page, size, attribute, direction);
        return ResponseEntity.status(OK).body(
                new FindStockLocationsViewApiResponse()
                        .code(OK.value())
                        .status(OK.name())
                        .success(true)
                        .timestamp(ZonedDateTime.now().toString())
                        .message(MessageUtil.getMessage(STOCK_LOCATIONS_FIND_SUCCESSFULLY, Locale.forLanguageTag(acceptLanguage), ""))
                        .data(of(BODY, findStockLocationsPageInfoViewResponse)));
    }

    @Override
    public ResponseEntity<SearchStockLocationsViewApiResponse> searchStockLocations(String acceptLanguage, Integer page, Integer size, String attribute, String direction, String keyword) {
        SearchStockLocationsPageInfoViewResponse searchStockLocationsPageInfoViewResponse =
                stockLocationServiceRestApiAdapter.searchStockLocations(page, size, attribute, direction, keyword);
        return ResponseEntity.status(OK).body(
                new SearchStockLocationsViewApiResponse()
                        .code(OK.value())
                        .status(OK.name())
                        .success(true)
                        .timestamp(ZonedDateTime.now().toString())
                        .message(MessageUtil.getMessage(STOCK_LOCATIONS_FIND_SUCCESSFULLY, Locale.forLanguageTag(acceptLanguage), ""))
                        .data(of(BODY, searchStockLocationsPageInfoViewResponse)));
    }
}
