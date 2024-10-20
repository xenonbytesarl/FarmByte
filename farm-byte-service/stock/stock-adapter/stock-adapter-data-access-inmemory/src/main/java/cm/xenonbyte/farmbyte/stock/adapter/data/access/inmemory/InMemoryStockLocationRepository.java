package cm.xenonbyte.farmbyte.stock.adapter.data.access.inmemory;

import cm.xenonbyte.farmbyte.common.domain.vo.Direction;
import cm.xenonbyte.farmbyte.common.domain.vo.Keyword;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.PageInfo;
import cm.xenonbyte.farmbyte.stock.domain.core.stocklocation.StockLocation;
import cm.xenonbyte.farmbyte.stock.domain.core.stocklocation.StockLocationId;
import cm.xenonbyte.farmbyte.stock.domain.core.stocklocation.StockLocationRepository;
import jakarta.annotation.Nonnull;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * @author bamk
 * @version 1.0
 * @since 13/10/2024
 */
public final class InMemoryStockLocationRepository implements StockLocationRepository {

    private final Map<StockLocationId, StockLocation> stockLocations = new LinkedHashMap<>();


    @Override
    public boolean existsById(@Nonnull StockLocationId parentId) {
        return stockLocations.containsKey(parentId);
    }

    @Override
    public StockLocation save(@Nonnull StockLocation stockLocation) {
        stockLocations.put(stockLocation.getId(), stockLocation);
        return stockLocation;
    }

    @Override
    public boolean existsByNameIgnoreCase(@Nonnull Name name) {
        return stockLocations.values().stream()
                .anyMatch(emplacement -> emplacement.getName().getText().getValue().equalsIgnoreCase(name.getText().getValue()));
    }

    @Nonnull
    @Override
    public Optional<StockLocation> findById(@Nonnull StockLocationId stockLocationId) {
        StockLocation stockLocation = stockLocations.get(stockLocationId);
        return stockLocation != null ? Optional.of(stockLocation) : Optional.empty();
    }

    @Override
    public PageInfo<StockLocation> findAll(Integer page, Integer size, String sortAttribute, Direction direction) {
        PageInfo<StockLocation> stockLocationPageInfo = new PageInfo<>();
        Comparator<StockLocation> comparing = Comparator.comparing((StockLocation a) -> a.getName().getText().getValue());
        return stockLocationPageInfo.with(
                page,
                size,
                stockLocations.values().stream()
                        .sorted(Direction.ASC.equals(direction) ? comparing : comparing.reversed())
                        .toList()
        );
    }

    @Override
    public PageInfo<StockLocation> search(Integer page, Integer size, String sortAttribute, Direction direction, Keyword keyword) {
        Predicate<StockLocation> stockLocationNammePredicate = stockLocation -> stockLocation.getName().getText().getValue().toLowerCase().contains(keyword.getText().getValue().toLowerCase());
        Comparator<StockLocation> comparing = Comparator.comparing((StockLocation a) -> a.getName().getText().getValue());
        PageInfo<StockLocation> stockLocationPageInfo = new PageInfo<>();
        return stockLocationPageInfo.with(
                page,
                size,
                stockLocations.values().stream()
                        .filter(stockLocationNammePredicate)
                        .sorted(Direction.ASC.equals(direction) ? comparing: comparing.reversed())
                        .toList()
        );
    }
}
