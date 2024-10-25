package cm.xenonbyte.farmbyte.stock.domain.core.stocklocation;

import cm.xenonbyte.farmbyte.common.domain.annotation.DomainService;
import cm.xenonbyte.farmbyte.common.domain.vo.Direction;
import cm.xenonbyte.farmbyte.common.domain.vo.Keyword;
import cm.xenonbyte.farmbyte.common.domain.vo.PageInfo;
import jakarta.annotation.Nonnull;

import java.util.Optional;

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

    @Nonnull
    @Override
    public StockLocation updateStockLocation(StockLocationId stockLocationId, @Nonnull StockLocation stockLocationToUpdate) {
        stockLocationToUpdate.validateMandatoryFields();
        Optional<StockLocation> optionalStockLocation = stockLocationRepository.findById(stockLocationId);
        if(optionalStockLocation.isPresent()) {
            verifyParent(stockLocationToUpdate.getParentId());
            verifyName(stockLocationToUpdate);
            return stockLocationRepository.update(optionalStockLocation.get(), stockLocationToUpdate);
        }
       throw new StockLocationNotFoundException(new String[]{stockLocationId.getValue().toString()});
    }

    private void verifyName(@Nonnull StockLocation stockLocation) {
        if(isStockLocationExistByNameWhenCreate(stockLocation)) {
            throw new StockLocationNameConflictException(new String[]{stockLocation.getName().getText().getValue()});
        }
        
        Optional<StockLocation> optionalStockLocation = stockLocationRepository.findByName(stockLocation.getName());
        
        if(isStockLocationExistsByNameWhenUpdate(stockLocation, optionalStockLocation)) {
            throw new StockLocationNameConflictException(new String[]{stockLocation.getName().getText().getValue()});
        }
    }

    private static boolean isStockLocationExistsByNameWhenUpdate(StockLocation stockLocation, Optional<StockLocation> optionalStockLocation) {
        return stockLocation.getId() != null && optionalStockLocation.isPresent() && !optionalStockLocation.get().getId().equals(stockLocation.getId());
    }

    private boolean isStockLocationExistByNameWhenCreate(StockLocation stockLocation) {
        return stockLocation.getId() == null && stockLocationRepository.existsByNameIgnoreCase(stockLocation.getName());
    }

    private void verifyParent(@Nonnull StockLocationId parentId) {
        if(parentId != null && !stockLocationRepository.existsById(parentId)) {
            throw new StockLocationParentIdNotFoundException(new String[] {parentId.getValue().toString()});
        }
    }
}
