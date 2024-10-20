package cm.xenonbyte.farmbyte.stock.adapter.rest.api;

import cm.xenonbyte.farmbyte.common.domain.vo.Direction;
import cm.xenonbyte.farmbyte.common.domain.vo.Keyword;
import cm.xenonbyte.farmbyte.common.domain.vo.Text;
import cm.xenonbyte.farmbyte.stock.adapter.rest.api.generated.stocklocation.view.CreateStockLocationViewRequest;
import cm.xenonbyte.farmbyte.stock.adapter.rest.api.generated.stocklocation.view.CreateStockLocationViewResponse;
import cm.xenonbyte.farmbyte.stock.adapter.rest.api.generated.stocklocation.view.FindStockLocationByIdViewResponse;
import cm.xenonbyte.farmbyte.stock.adapter.rest.api.generated.stocklocation.view.FindStockLocationsPageInfoViewResponse;
import cm.xenonbyte.farmbyte.stock.adapter.rest.api.generated.stocklocation.view.SearchStockLocationsPageInfoViewResponse;
import cm.xenonbyte.farmbyte.stock.domain.core.stocklocation.StockLocationId;
import cm.xenonbyte.farmbyte.stock.domain.core.stocklocation.StockLocationService;
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
public class StockLocationDomainServiceRestApiAdapter implements StockLocationServiceRestApiAdapter {

    private final StockLocationService stockLocationService;
    private final StockLocationViewMapper stockLocationViewMapper;

    public StockLocationDomainServiceRestApiAdapter(
            @Nonnull StockLocationService stockLocationService,
            @Nonnull StockLocationViewMapper stockLocationViewMapper) {

        this.stockLocationService = Objects.requireNonNull(stockLocationService);
        this.stockLocationViewMapper = Objects.requireNonNull(stockLocationViewMapper);
    }

    @Nonnull
    @Override
    @Transactional
    public CreateStockLocationViewResponse createStockLocation(@Nonnull CreateStockLocationViewRequest createStockLocationViewRequest) {
        return stockLocationViewMapper.toCreateStockLocationViewResponse(
                stockLocationService.createStockLocation(
                        stockLocationViewMapper.toStockLocation(createStockLocationViewRequest)
                )
        );
    }

    @Nonnull
    @Override
    public FindStockLocationByIdViewResponse findStockLocationById(@Nonnull UUID stockLocationId) {
        return stockLocationViewMapper.toFindStockLocationByIdViewResponse(
                stockLocationService.findStockLocationById(new StockLocationId(stockLocationId))
        );
    }

    @Nonnull
    @Override
    public FindStockLocationsPageInfoViewResponse findStockLocations(int page, int size, String attribute, String direction) {
        return stockLocationViewMapper.toFindStockLocationsPageInfoViewResponse(
                stockLocationService.findStockLocations(page, size, attribute, Direction.valueOf(direction))
        );
    }

    @Nonnull
    @Override
    public SearchStockLocationsPageInfoViewResponse searchStockLocations(int page, int size, String attribute, String direction, String keyword) {
        return stockLocationViewMapper.toSearchStockLocationsPageInfoViewResponse(
                stockLocationService.searchStockLocations(page, size, attribute, Direction.valueOf(direction), Keyword.of(Text.of(keyword)))
        );
    }
}
