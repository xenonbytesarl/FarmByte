package cm.xenonbyte.farmbyte.stock.data.access.inmemory;

import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.Text;
import cm.xenonbyte.farmbyte.stock.adapter.access.test.StockLocationRepositoryTest;
import cm.xenonbyte.farmbyte.stock.adapter.data.access.inmemory.InMemoryStockLocationRepository;
import cm.xenonbyte.farmbyte.stock.domain.core.stocklocation.StockLocation;
import cm.xenonbyte.farmbyte.stock.domain.core.stocklocation.StockLocationId;
import cm.xenonbyte.farmbyte.stock.domain.core.stocklocation.StockLocationType;
import org.junit.jupiter.api.BeforeEach;

import java.util.UUID;

/**
 * @author bamk
 * @version 1.0
 * @since 16/10/2024
 */
public final class StockLocationInMemoryRepositoryAdapterTest extends StockLocationRepositoryTest {

    @BeforeEach
    void setUp() {
        stockLocationRepository = new InMemoryStockLocationRepository();

        name = Name.of(Text.of("Root View Location"));

        parentId = new StockLocationId(UUID.fromString("019296b0-857b-71bd-ab43-1f855b4ccef5"));

        stockLocation = stockLocationRepository.save(
                StockLocation.builder()
                        .id(parentId)
                        .type(StockLocationType.INTERNAL)
                        .name(name)
                        .build());

        stockLocationToUpdate = StockLocation.builder()
                .id(stockLocationId)
                .name(Name.of(Text.of("First internal Location Update")))
                .type(StockLocationType.INTERNAL)
                .parentId(parentId)
                .build();

        stockLocationRepository.save(
                StockLocation.builder()
                    .id(new StockLocationId(UUID.fromString("019296b2-1138-7fcd-b732-aa676eb469fd")))
                    .name(Name.of(Text.of("First internal Location")))
                    .type(StockLocationType.INTERNAL)
                    .parentId(parentId)
                    .build());

        stockLocationId = new StockLocationId(UUID.fromString("0192a671-0151-712b-a583-8fab7cf9490d"));

        stockLocationRepository.save(
                StockLocation.builder()
                    .id(stockLocationId)
                    .name(Name.of(Text.of("Second internal Location")))
                    .type(StockLocationType.INTERNAL)
                    .parentId(parentId)
                    .build());

        stockLocationRepository.save(
                StockLocation.builder()
                        .id(new StockLocationId(UUID.fromString("0192a672-046b-7598-a4c7-43e05f81ffc2")))
                        .name(Name.of(Text.of("Third internal Location")))
                        .type(StockLocationType.INTERNAL)
                        .parentId(parentId)
                        .build());


    }
}
