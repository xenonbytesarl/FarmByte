package cm.xenonbyte.farmbyte.stock.adapter.data.access.jpa;

import cm.xenonbyte.farmbyte.common.domain.vo.Direction;
import cm.xenonbyte.farmbyte.common.domain.vo.Keyword;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.PageInfo;
import cm.xenonbyte.farmbyte.stock.domain.core.stocklocation.StockLocation;
import cm.xenonbyte.farmbyte.stock.domain.core.stocklocation.StockLocationId;
import cm.xenonbyte.farmbyte.stock.domain.core.stocklocation.StockLocationRepository;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

/**
 * @author bamk
 * @version 1.0
 * @since 13/10/2024
 */
@Slf4j
@Service
public class StockLocationJpaRepositoryAdapter implements StockLocationRepository {


    private final StockLocationRepositoryJpa stockLocationRepositoryJpa;
    private final StockLocationJpaMapper stockLocationJpaMapper;

    public StockLocationJpaRepositoryAdapter(
            final @Nonnull StockLocationRepositoryJpa stockLocationRepositoryJpa,
            final @Nonnull StockLocationJpaMapper stockLocationJpaMapper) {
        this.stockLocationRepositoryJpa = Objects.requireNonNull(stockLocationRepositoryJpa);
        this.stockLocationJpaMapper = Objects.requireNonNull(stockLocationJpaMapper);
    }


    @Override
    @Transactional(readOnly = true)
    public boolean existsById(@Nonnull StockLocationId stockLocationId) {
        return stockLocationRepositoryJpa.existsById(stockLocationId.getValue());
    }

    @Override
    @Transactional
    public StockLocation save(@Nonnull StockLocation stockLocation) {
        return stockLocationJpaMapper.toStockLocation(
                stockLocationRepositoryJpa.save(
                        stockLocationJpaMapper.toStockLocationJpa(stockLocation)
                )
        );
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByNameIgnoreCase(@Nonnull Name name) {
        return stockLocationRepositoryJpa.existsByNameIgnoreCase(name.getText().getValue());
    }

    @Nonnull
    @Override
    public Optional<StockLocation> findById(@Nonnull StockLocationId stockLocationId) {
        return stockLocationRepositoryJpa.findById(stockLocationId.getValue())
                .map(stockLocationJpaMapper::toStockLocation);
    }

    @Override
    public PageInfo<StockLocation> findAll(Integer page, Integer size, String sortAttribute, Direction direction) {
        Sort.Direction sortDirection = Direction.ASC.equals(direction) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Page<StockLocationJpa> stockLocationJpaPage =
                stockLocationRepositoryJpa.findAll(PageRequest.of(page, size, sortDirection, sortAttribute));
        return new PageInfo<>(
                stockLocationJpaPage.isFirst(),
                stockLocationJpaPage.isLast(),
                stockLocationJpaPage.getSize(),
                stockLocationJpaPage.getTotalElements(),
                stockLocationJpaPage.getTotalPages(),
                stockLocationJpaPage.getContent().stream().map(stockLocationJpaMapper::toStockLocation).toList()
        );
    }

    @Override
    public PageInfo<StockLocation> search(Integer page, Integer size, String sortAttribute, Direction direction, Keyword keyword) {
        Sort.Direction sortDirection = Direction.ASC.equals(direction) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Page<StockLocationJpa> stockLocationJpaPage =
                stockLocationRepositoryJpa.search(PageRequest.of(page, size, sortDirection, sortAttribute), keyword.getText().getValue());
        return new PageInfo<>(
                stockLocationJpaPage.isFirst(),
                stockLocationJpaPage.isLast(),
                stockLocationJpaPage.getSize(),
                stockLocationJpaPage.getTotalElements(),
                stockLocationJpaPage.getTotalPages(),
                stockLocationJpaPage.getContent().stream().map(stockLocationJpaMapper::toStockLocation).toList()
        );
    }
}
