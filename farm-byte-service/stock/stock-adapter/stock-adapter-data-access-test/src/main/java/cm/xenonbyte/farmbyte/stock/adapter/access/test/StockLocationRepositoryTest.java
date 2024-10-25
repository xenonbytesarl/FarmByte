package cm.xenonbyte.farmbyte.stock.adapter.access.test;

import cm.xenonbyte.farmbyte.common.domain.vo.Direction;
import cm.xenonbyte.farmbyte.common.domain.vo.Keyword;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.PageInfo;
import cm.xenonbyte.farmbyte.common.domain.vo.Text;
import cm.xenonbyte.farmbyte.stock.domain.core.stocklocation.StockLocation;
import cm.xenonbyte.farmbyte.stock.domain.core.stocklocation.StockLocationId;
import cm.xenonbyte.farmbyte.stock.domain.core.stocklocation.StockLocationRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author bamk
 * @version 1.0
 * @since 16/10/2024
 */
public abstract class StockLocationRepositoryTest {

    protected StockLocationRepository stockLocationRepository;
    protected Name name;
    protected StockLocationId parentId;
    protected StockLocation stockLocation;
    protected StockLocation stockLocationToUpdate;
    protected StockLocationId stockLocationId;

    @Nested
    class CreateStockLocationRepositoryTest {
        @Test
        void should_return_false_when_find_inventory_emplacement_with_non_existing_name() {

            //Given + Act
            Boolean result = stockLocationRepository.existsByNameIgnoreCase(Name.of(Text.of("Fake Location Name 1")));
            //Then
            assertThat(result).isFalse();
        }

        @Test
        void should_return_true_when_find_inventory_emplacement_with_existing_emplacement() {
            //Given + Act
            Boolean result = stockLocationRepository.existsByNameIgnoreCase(name);
            //Then
            assertThat(result).isTrue();
        }

        @Test
        void should_return_false_when_find_inventory_emplacement_with_non_parent_id() {
            //Given
            StockLocationId fakeStockLocationId = new StockLocationId(UUID.fromString("019296a1-9551-7ae3-9fce-25503bc7b5ab"));
            //Act
            Boolean result = stockLocationRepository.existsById(fakeStockLocationId);
            //Then
            assertThat(result).isFalse();
        }

        @Test
        void should_return_true_when_find_inventory_emplacement_with_existing_parent_id() {
            //Given + Act
            Boolean result = stockLocationRepository.existsById(parentId);
            //Then
            assertThat(result).isTrue();
        }

        @Test
        void should_create_inventory_emplacement() {
            //Given + Act
            StockLocation result = stockLocationRepository.save(stockLocation);

            //Then
            assertThat(result).isNotNull().isEqualTo(stockLocation);
        }
    }

    @Nested
    class FindStockLocationByIdRepositoryTest {

        @Test
        void should_success_when_find_inventory_emplacement_by_id() {
            //Given + Act
            Optional<StockLocation> result = stockLocationRepository.findById(stockLocationId);

            //Then
            assertThat(result.isPresent()).isTrue();
        }

        @Test
        void should_fail_when_find_inventory_emplacement_by_id() {
            //Given + Act
            Optional<StockLocation> result =
                    stockLocationRepository.findById(new StockLocationId(UUID.fromString("0192a66b-2ca4-771f-8b70-d57d4b66f797")));

            //Then
            assertThat(result.isPresent()).isFalse();
        }
    }

    @Nested
    class FindStockLocationsRepositoryTest {
        @Test
        void should_success_when_find_inventory_emplacements() {
            //Given
            Integer page = 0;
            Integer size = 2;
            String sortAttribute = "name";
            Direction direction = Direction.ASC;

            //Act
            PageInfo<StockLocation> result =
                    stockLocationRepository.findAll(page, size, sortAttribute, direction);

            //Then
            assertThat(result.getTotalElements()).isGreaterThan(0);
            assertThat(result.getTotalPages()).isGreaterThan(0);
            assertThat(result.getElements().size()).isGreaterThan(0);
            assertThat(result.getTotalPages()).isGreaterThan(0);
        }
    }

    @Nested
    class SearchStockLocationsRepositoryTest {
        @Test
        void should_success_when_find_inventory_emplacements() {
            //Given
            Integer page = 0;
            Integer size = 2;
            String sortAttribute = "name";
            Direction direction = Direction.ASC;
            Keyword keyword = Keyword.of(Text.of("int"));

            //Act
            PageInfo<StockLocation> result =
                    stockLocationRepository.search(page, size, sortAttribute, direction, keyword);

            //Then
            assertThat(result.getTotalElements()).isGreaterThan(0);
            assertThat(result.getTotalPages()).isGreaterThan(0);
            assertThat(result.getElements().size()).isGreaterThan(0);
            assertThat(result.getTotalPages()).isGreaterThan(0);
        }

        @Test
        void should_empty_when_find_inventory_emplacements() {
            //Given
            Integer page = 0;
            Integer size = 2;
            String sortAttribute = "name";
            Direction direction = Direction.ASC;
            Keyword keyword = Keyword.of(Text.of("xxxx"));

            //Act
            PageInfo<StockLocation> result =
                    stockLocationRepository.search(page, size, sortAttribute, direction, keyword);

            //Then
            assertThat(result.getTotalElements()).isEqualTo(0);
            assertThat(result.getTotalPages()).isEqualTo(0);
            assertThat(result.getElements().size()).isEqualTo(0);
            assertThat(result.getTotalPages()).isEqualTo(0);
        }
    }

    @Nested
    class UpdateStockLocationRepositoryTest {
        @Test
        void should_true_when_find_stock_location_by_name() {
            //Given + Act
            Optional<StockLocation> result = stockLocationRepository.findByName(name);
            //Then
            assertThat(result.isPresent()).isTrue();
        }

        @Test
        void should_false_when_find_stock_location_by_name() {
            //Given + Act
            Optional<StockLocation> result = stockLocationRepository.findByName(Name.of(Text.of("Fake Stock Location Name")));
            //Then
            assertThat(result.isPresent()).isFalse();
        }

        @Test
        void should_update_stock_location() {
            //Given +  Act
            StockLocation result = stockLocationRepository.update(stockLocation, stockLocationToUpdate);

            //Act
            assertThat(result).isNotNull().isEqualTo(stockLocationToUpdate);
        }
    }
}
