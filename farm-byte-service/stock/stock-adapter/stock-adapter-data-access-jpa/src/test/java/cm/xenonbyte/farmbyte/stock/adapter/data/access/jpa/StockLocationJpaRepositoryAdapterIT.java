package cm.xenonbyte.farmbyte.stock.adapter.data.access.jpa;

import cm.xenonbyte.farmbyte.common.domain.vo.Active;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.Text;
import cm.xenonbyte.farmbyte.stock.adapter.access.test.StockLocationRepositoryTest;
import cm.xenonbyte.farmbyte.stock.domain.core.stocklocation.StockLocation;
import cm.xenonbyte.farmbyte.stock.domain.core.stocklocation.StockLocationId;
import cm.xenonbyte.farmbyte.stock.domain.core.stocklocation.StockLocationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.UUID;

/**
 * @author bamk
 * @version 1.0
 * @since 16/10/2024
 */
@DataJpaTest
@ActiveProfiles("test")
@ExtendWith(DatabaseSetupExtension.class)
@Import(JpaRepositoryAdapterTest.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {StockLocationJpaRepository.class, StockLocationJpaMapper.class})
public class StockLocationJpaRepositoryAdapterIT extends StockLocationRepositoryTest {

    @Autowired
    private StockLocationJpaMapper stockLocationJpaMapper;

    @Autowired
    private StockLocationJpaRepository stockLocationJpaRepository;

    @BeforeEach
    void setUp() {
        stockLocationRepository = new StockLocationJpaRepositoryAdapter(
                stockLocationJpaRepository, stockLocationJpaMapper);

        name = Name.of(Text.of("Internal Location 1"));

        parentId = new StockLocationId(UUID.fromString("019296f7-1e4b-7739-8f09-cf650590f0d7"));

        stockLocationId = new StockLocationId(UUID.fromString("0192a686-3f49-7f2d-b7eb-ebe08814b82a"));

        stockLocation = StockLocation.builder()
                .id(new StockLocationId(UUID.fromString("01929708-609d-7d64-ae29-1940b7e5f6f1")))
                .name(Name.of(Text.of("Internal Location 4")))
                .type(StockLocationType.INTERNAL)
                .parentId(parentId)
                .active(Active.with(true))
                .build();

        stockLocationToUpdate = StockLocation.builder()
                .id(stockLocationId)
                .name(Name.of(Text.of("Internal Location Update")))
                .type(StockLocationType.INTERNAL)
                .parentId(parentId)
                .active(Active.with(true))
                .build();
    }
}
