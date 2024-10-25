package cm.xenonbyte.farmbyte.stock.adapter.rest.api;

import cm.xenonbyte.farmbyte.stock.adapter.rest.api.generated.stocklocation.view.CreateStockLocationViewRequest;
import cm.xenonbyte.farmbyte.stock.adapter.rest.api.generated.stocklocation.view.CreateStockLocationViewResponse;
import cm.xenonbyte.farmbyte.stock.adapter.rest.api.generated.stocklocation.view.FindStockLocationByIdViewResponse;
import cm.xenonbyte.farmbyte.stock.adapter.rest.api.generated.stocklocation.view.FindStockLocationsPageInfoViewResponse;
import cm.xenonbyte.farmbyte.stock.adapter.rest.api.generated.stocklocation.view.SearchStockLocationsPageInfoViewResponse;
import cm.xenonbyte.farmbyte.stock.adapter.rest.api.generated.stocklocation.view.UpdateStockLocationViewRequest;
import cm.xenonbyte.farmbyte.stock.adapter.rest.api.generated.stocklocation.view.UpdateStockLocationViewResponse;
import jakarta.annotation.Nonnull;
import jakarta.validation.Valid;

import java.util.UUID;

/**
 * @author bamk
 * @version 1.0
 * @since 17/10/2024
 */
public interface StockLocationServiceRestApiAdapter {
    @Nonnull @Valid
    CreateStockLocationViewResponse createStockLocation(@Nonnull @Valid CreateStockLocationViewRequest createStockLocationViewRequest);

    @Nonnull @Valid
    FindStockLocationByIdViewResponse findStockLocationById(@Nonnull UUID stockLocationId);

    @Nonnull @Valid
    FindStockLocationsPageInfoViewResponse findStockLocations(int page, int size, String attribute, String direction);

    @Nonnull @Valid
    SearchStockLocationsPageInfoViewResponse searchStockLocations(int page, int size, String attribute, String direction, String keyword);

    @Nonnull @Valid UpdateStockLocationViewResponse updateStockLocation(@Nonnull UUID stockLocationId, @Nonnull UpdateStockLocationViewRequest updateStockLocationViewRequest);
}
