package cm.xenonbyte.farmbyte.stock.domain.core.stocklocation;

import cm.xenonbyte.farmbyte.common.domain.vo.Direction;
import cm.xenonbyte.farmbyte.common.domain.vo.Keyword;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.PageInfo;
import jakarta.annotation.Nonnull;

import java.util.Optional;

/**
 * @author bamk
 * @version 1.0
 * @since 13/10/2024
 */
public interface StockLocationRepository {
    boolean existsById(@Nonnull StockLocationId stockLocationId);

    @Nonnull
    StockLocation save(@Nonnull StockLocation stockLocation);

    boolean existsByNameIgnoreCase(@Nonnull Name name);

    @Nonnull
    Optional<StockLocation> findById(@Nonnull StockLocationId stockLocationId);

    PageInfo<StockLocation> findAll(Integer page, Integer size, String sortAttribute, Direction direction);

    PageInfo<StockLocation> search(Integer page, Integer size, String sortAttribute, Direction direction, Keyword keyword);

    @Nonnull StockLocation update(@Nonnull StockLocation oldStockLocation, @Nonnull StockLocation newStockLocation);

    Optional<StockLocation> findByName(@Nonnull Name name);
}
