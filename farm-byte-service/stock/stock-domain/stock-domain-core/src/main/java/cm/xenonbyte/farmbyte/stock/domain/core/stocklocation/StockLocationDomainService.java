package cm.xenonbyte.farmbyte.stock.domain.core.stocklocation;

import cm.xenonbyte.farmbyte.common.domain.annotation.DomainService;
import cm.xenonbyte.farmbyte.common.domain.vo.Direction;
import cm.xenonbyte.farmbyte.common.domain.vo.Keyword;
import cm.xenonbyte.farmbyte.common.domain.vo.PageInfo;
import jakarta.annotation.Nonnull;

import static java.util.Objects.requireNonNull;

/**
 * @author bamk
 * @version 1.0
 * @since 13/10/2024
 */
@DomainService
public final class StockLocationDomainService implements StockLocationService {

    private final StockLocationRepository stockLocationRepository;

    public StockLocationDomainService(@Nonnull StockLocationRepository stockLocationRepository) {
        this.stockLocationRepository = requireNonNull(stockLocationRepository);
    }

    @Nonnull
    @Override
    public StockLocation createStockLocation(@Nonnull StockLocation stockLocation) {
        stockLocation.validateMandatoryFields();
        verifyParent(stockLocation.getParentId());
        verifyName(stockLocation);
        stockLocation.initializeWithDefaults();
        return stockLocationRepository.save(stockLocation);
    }

    @Nonnull
    @Override
    public StockLocation findStockLocationById(@Nonnull StockLocationId stockLocationId) {
        return stockLocationRepository.findById(stockLocationId).orElseThrow(
                () -> new StockLocationNotFoundException(new String[]{stockLocationId.getValue().toString()})
        );
    }

    @Override
    public PageInfo<StockLocation> findStockLocations(Integer page, Integer size, String sortAttribute, Direction direction) {
        return stockLocationRepository.findAll(page, size, sortAttribute, direction);
    }

    @Override
    public PageInfo<StockLocation> searchStockLocations(Integer page, Integer size, String sortAttribute, Direction direction, Keyword keyword) {
        return stockLocationRepository.search(page, size, sortAttribute, direction, keyword);
    }

    private void verifyName(@Nonnull StockLocation stockLocation) {
        if(stockLocation.getId() == null && stockLocationRepository.existsByNameIgnoreCase(stockLocation.getName())) {
            throw new StockLocationNameConflictException(new String[]{stockLocation.getName().getText().getValue()});
        }
    }

    private void verifyParent(@Nonnull StockLocationId parentId) {
        if(parentId != null && !stockLocationRepository.existsById(parentId)) {
            throw new StockLocationParentIdNotFoundException(new String[] {parentId.getValue().toString()});
        }
    }
}
