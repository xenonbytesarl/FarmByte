package cm.xenonbyte.farmbyte.stock.domain.core.stocklocation;

import cm.xenonbyte.farmbyte.common.domain.vo.Direction;
import cm.xenonbyte.farmbyte.common.domain.vo.Keyword;
import cm.xenonbyte.farmbyte.common.domain.vo.PageInfo;
import jakarta.annotation.Nonnull;

/**
 * @author bamk
 * @version 1.0
 * @since 13/10/2024
 */
public interface StockLocationService {
    @Nonnull
    StockLocation createStockLocation(@Nonnull StockLocation stockLocation);

    @Nonnull
    StockLocation findStockLocationById(@Nonnull StockLocationId stockLocationId);

    PageInfo<StockLocation> findStockLocations(Integer page, Integer size, String sortAttribute, Direction direction);

    PageInfo<StockLocation> searchStockLocations(Integer page, Integer size, String sortAttribute, Direction direction, Keyword keyword);

    @Nonnull StockLocation updateStockLocation(StockLocationId stockLocationId, @Nonnull StockLocation stockLocation);
}
