package cm.xenonbyte.farmbyte.stock.domain.core;

import cm.xenonbyte.farmbyte.common.domain.validation.InvalidFieldBadException;
import cm.xenonbyte.farmbyte.common.domain.vo.Active;
import cm.xenonbyte.farmbyte.common.domain.vo.Direction;
import cm.xenonbyte.farmbyte.common.domain.vo.Keyword;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.PageInfo;
import cm.xenonbyte.farmbyte.common.domain.vo.Text;
import cm.xenonbyte.farmbyte.stock.adapter.data.access.inmemory.InMemoryStockLocationRepository;
import cm.xenonbyte.farmbyte.stock.domain.core.constant.StockDomainConstant;
import cm.xenonbyte.farmbyte.stock.domain.core.stocklocation.StockLocation;
import cm.xenonbyte.farmbyte.stock.domain.core.stocklocation.StockLocationDomainService;
import cm.xenonbyte.farmbyte.stock.domain.core.stocklocation.StockLocationId;
import cm.xenonbyte.farmbyte.stock.domain.core.stocklocation.StockLocationNameConflictException;
import cm.xenonbyte.farmbyte.stock.domain.core.stocklocation.StockLocationNotFoundException;
import cm.xenonbyte.farmbyte.stock.domain.core.stocklocation.StockLocationParentIdNotFoundException;
import cm.xenonbyte.farmbyte.stock.domain.core.stocklocation.StockLocationRepository;
import cm.xenonbyte.farmbyte.stock.domain.core.stocklocation.StockLocationService;
import cm.xenonbyte.farmbyte.stock.domain.core.stocklocation.StockLocationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.UUID;
import java.util.stream.Stream;

import static cm.xenonbyte.farmbyte.common.domain.validation.InvalidFieldBadException.NOT_NULL_VALUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author bamk
 * @version 1.0
 * @since 13/10/2024
 */
public final class StockLocationTest {

    private StockLocationService stockLocationService;
    private StockLocationId parentId;
    StockLocationRepository stockLocationRepository;
    Name name;

    @BeforeEach
    void setUp() {
        stockLocationRepository = new InMemoryStockLocationRepository();
        stockLocationService = new StockLocationDomainService(stockLocationRepository);
        parentId = new StockLocationId(UUID.fromString("01928612-317e-7736-8ef4-3f259caa732a"));
        name = Name.of(Text.of("RootStockLocation"));

        stockLocationRepository.save(
          StockLocation.builder()
                  .id(parentId)
                  .name(name)
                  .type(StockLocationType.INTERNAL)
                  .active(Active.with(true))
                  .build()
        );
    }

    @Nested
    class CreateStockLocationDomainTest {


        @Test
        void should_create_inventory_emplacement_without_parent() {
            //Given
            StockLocation stockLocation = new StockLocation(
                    Name.of(Text.of("StockLocationName.1")),
                    StockLocationType.INTERNAL
            );

            //Act
            StockLocation result = stockLocationService.createStockLocation(stockLocation);

            //Then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isNotNull();
            assertThat(result.getName()).isEqualTo(stockLocation.getName());
            assertThat(result.getType()).isEqualTo(stockLocation.getType());
            assertThat(result.getActive().getValue()).isTrue();
        }

        @Test
        void should_create_inventory_emplacement_with_parent() {
            //Given
            StockLocation stockLocation = StockLocation.builder()
                    .name(Name.of(Text.of("StockLocationName.2")))
                    .type(StockLocationType.INTERNAL)
                    .parentId(parentId)
                    .build();

            //Act
            StockLocation result = stockLocationService.createStockLocation(stockLocation);

            //Then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isNotNull();
            assertThat(result.getName()).isEqualTo(stockLocation.getName());
            assertThat(result.getType()).isEqualTo(stockLocation.getType());
            assertThat(result.getActive().getValue()).isTrue();
            assertThat(result.getParentId()).isEqualTo(stockLocation.getParentId());
        }

        @Test
        void should_fail_when_create_inventory_emplacement_with_duplicate_name() {
            //Given
            StockLocation stockLocation = StockLocation.builder()
                    .name(name)
                    .type(StockLocationType.INTERNAL)
                    .parentId(parentId)
                    .build();

            //Act + Then
            assertThatThrownBy(() -> stockLocationService.createStockLocation(stockLocation))
                    .isInstanceOf(StockLocationNameConflictException.class)
                    .hasMessage(StockDomainConstant.STOCK_LOCATION_NAME_CONFLICT);
        }

        @Test
        void should_fail_when_create_inventory_emplacement_with_non_existing_parent_id() {
            //Given
            StockLocation stockLocation = StockLocation.builder()
                    .name(Name.of(Text.of("StockLocationName.3")))
                    .type(StockLocationType.INTERNAL)
                    .parentId(new StockLocationId(UUID.fromString("01928641-e6ee-7ef5-a883-ccb70de6e427")))
                    .build();

            //Act + Then
            assertThatThrownBy(() -> stockLocationService.createStockLocation(stockLocation))
                    .isInstanceOf(StockLocationParentIdNotFoundException.class)
                    .hasMessage(StockDomainConstant.STOCK_LOCATION_PARENT_ID_NOT_FOUND);
        }

        static Stream<Arguments> createStockLocationInvalidAttribute() {
            return Stream.of(
                    Arguments.of(null, StockLocationType.INTERNAL, InvalidFieldBadException.class, NOT_NULL_VALUE),
                    Arguments.of(Name.of(Text.of("StockLocationName.4")), null, InvalidFieldBadException.class, NOT_NULL_VALUE)

            );
        }

        @ParameterizedTest
        @MethodSource("createStockLocationInvalidAttribute")
        void should_fail_when_create_inventory_emplacement_with_a_least_of_one_invalid_attribute(
                Name name, StockLocationType type, Class<? extends RuntimeException> excepectedClass, String expectedMessage
        ) {
            //Given
            StockLocation stockLocation = StockLocation.builder()
                    .name(name)
                    .type(type)
                    .build();

            //Act + Then
            assertThatThrownBy(() -> stockLocationService.createStockLocation(stockLocation))
                    .isInstanceOf(excepectedClass)
                    .hasMessage(expectedMessage);
        }
    }

    @Nested
    class FindStockLocationByIdDomainTest {
        StockLocation stockLocation;
        StockLocationId stockLocationId;

        @BeforeEach
        void setUp() {
            stockLocationId = new StockLocationId(UUID.fromString("0192a1bd-5bec-7764-b49e-cc99bebb26e7"));
            stockLocation = StockLocation.builder()
                    .id(stockLocationId)
                    .name(Name.of(Text.of("StockLocationName.1")))
                    .type(StockLocationType.INTERNAL)
                    .build();

            stockLocationRepository.save(stockLocation);
        }

        @Test
        void should_success_when_find_inventory_emplacement_with_existing_id() {
            //Given + Act
            StockLocation result = stockLocationService.findStockLocationById(stockLocationId);
            //Then
            assertThat(result)
                    .isNotNull()
                    .isEqualTo(stockLocation);
        }

        @Test
        void should_fail_when_find_inventory_emplacement_with_non_existing_id() {
            //Given + Act + Then
            assertThatThrownBy(() -> stockLocationService.findStockLocationById(new StockLocationId(UUID.randomUUID())))
                    .isInstanceOf(StockLocationNotFoundException.class)
                    .hasMessage(StockDomainConstant.STOCK_LOCATION_ID_NOT_FOUND);
        }
    }

    @Nested
    class FindStockLocationsDomainTest {
        @BeforeEach
        void setUp() {

            stockLocationRepository.save(
                StockLocation.builder()
                    .id(new StockLocationId(UUID.fromString("0192a1cf-1850-7a51-ad51-f76c625aeec0")))
                    .name(Name.of(Text.of("StockLocationName.1")))
                    .type(StockLocationType.INTERNAL)
                    .build()
            );

            stockLocationRepository.save(
                    StockLocation.builder()
                            .id(new StockLocationId(UUID.fromString("0192a1cf-fbcb-7241-af27-62d110c3cedb")))
                            .name(Name.of(Text.of("StockLocationName.2")))
                            .type(StockLocationType.INTERNAL)
                            .build()
            );

            stockLocationRepository.save(
                    StockLocation.builder()
                            .id(new StockLocationId(UUID.fromString("0192a1d0-32e8-7c06-9924-36bd0e3149fd")))
                            .name(Name.of(Text.of("StockLocationName.3")))
                            .type(StockLocationType.CUSTOMER)
                            .build()
            );

            stockLocationRepository.save(
                    StockLocation.builder()
                            .id(new StockLocationId(UUID.fromString("0192a1d0-a26a-78c1-8e11-e3d8578bfeaf")))
                            .name(Name.of(Text.of("StockLocationName.4")))
                            .type(StockLocationType.SUPPLIER)
                            .build()
            );

            stockLocationRepository.save(
                    StockLocation.builder()
                            .id(new StockLocationId(UUID.fromString("0192a1d0-f7b1-75cd-8361-99a9e5a1c21f")))
                            .name(Name.of(Text.of("StockLocationName.5")))
                            .type(StockLocationType.TRANSIT)
                            .build()
            );
        }

        static Stream<Arguments> findStockLocationsMethodSource() {
            return Stream.of(
                    Arguments.of(2, 2, "name", Direction.ASC, 3, 6l, 2,2),
                    Arguments.of(1, 2, "name", Direction.DSC, 3, 6l, 2,2)
            );
        }

        @ParameterizedTest
        @MethodSource("findStockLocationsMethodSource")
        void should_success_when_find_all_inventory_emplacements(
                Integer page,
                Integer size,
                String sortAttribute,
                Direction direction,
                Integer totalPages,
                Long totalElements,
                Integer pageSize,
                Integer contentSize
        ) {
            //Then
            PageInfo<StockLocation> result = stockLocationService.findStockLocations(page, size, sortAttribute, direction);

            assertThat(result.getTotalElements()).isEqualTo(totalElements);
            assertThat(result.getTotalPages()).isEqualTo(totalPages);
            assertThat(result.getPageSize()).isEqualTo(pageSize);
            assertThat(result.getElements()).hasSize(contentSize);
        }
    }

    @Nested
    class SearchStockLocationsDomainTest {
        @BeforeEach
        void setUp() {

            stockLocationRepository.save(
                    StockLocation.builder()
                            .id(new StockLocationId(UUID.fromString("0192a1cf-1850-7a51-ad51-f76c625aeec0")))
                            .name(Name.of(Text.of("StockLocationNameInternal.1")))
                            .type(StockLocationType.INTERNAL)
                            .build()
            );

            stockLocationRepository.save(
                    StockLocation.builder()
                            .id(new StockLocationId(UUID.fromString("0192a1cf-fbcb-7241-af27-62d110c3cedb")))
                            .name(Name.of(Text.of("StockLocationNameInternal.2")))
                            .type(StockLocationType.INTERNAL)
                            .build()
            );

            stockLocationRepository.save(
                    StockLocation.builder()
                            .id(new StockLocationId(UUID.fromString("0192a1d0-32e8-7c06-9924-36bd0e3149fd")))
                            .name(Name.of(Text.of("StockLocationNameCustomer.3")))
                            .type(StockLocationType.CUSTOMER)
                            .build()
            );

            stockLocationRepository.save(
                    StockLocation.builder()
                            .id(new StockLocationId(UUID.fromString("0192a1d0-a26a-78c1-8e11-e3d8578bfeaf")))
                            .name(Name.of(Text.of("StockLocationNameSupplier.4")))
                            .type(StockLocationType.SUPPLIER)
                            .build()
            );

            stockLocationRepository.save(
                    StockLocation.builder()
                            .id(new StockLocationId(UUID.fromString("0192a1d0-f7b1-75cd-8361-99a9e5a1c21f")))
                            .name(Name.of(Text.of("StockLocationNameTransit.5")))
                            .type(StockLocationType.TRANSIT)
                            .build()
            );
        }

        static Stream<Arguments> findStockLocationsMethodSource() {
            return Stream.of(
                    Arguments.of("Internal", 0, 2, "name", Direction.ASC, 1, 2l, 2,2),
                    Arguments.of("Customer", 0, 2, "name", Direction.DSC, 1, 1l, 2,1)
            );
        }

        @ParameterizedTest
        @MethodSource("findStockLocationsMethodSource")
        void should_success_when_find_all_inventory_emplacements(
                String keyword,
                Integer page,
                Integer size,
                String sortAttribute,
                Direction direction,
                Integer totalPages,
                Long totalElements,
                Integer pageSize,
                Integer contentSize
        ) {
            //Then
            PageInfo<StockLocation> result = stockLocationService.searchStockLocations(page, size, sortAttribute, direction, Keyword.of(Text.of(keyword)));

            assertThat(result).isNotNull();
            assertThat(result.getTotalElements()).isEqualTo(totalElements);
            assertThat(result.getTotalPages()).isEqualTo(totalPages);
            assertThat(result.getPageSize()).isEqualTo(pageSize);
            assertThat(result.getElements()).hasSize(contentSize);
        }
    }

}
