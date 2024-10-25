package cm.xenonbyte.farmbyte.stock.adapter.rest.api;

import cm.xenonbyte.farmbyte.common.domain.vo.Active;
import cm.xenonbyte.farmbyte.common.domain.vo.Direction;
import cm.xenonbyte.farmbyte.common.domain.vo.Keyword;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.PageInfo;
import cm.xenonbyte.farmbyte.common.domain.vo.Text;
import cm.xenonbyte.farmbyte.stock.adapter.rest.api.generated.stocklocation.view.CreateStockLocationViewRequest;
import cm.xenonbyte.farmbyte.stock.adapter.rest.api.generated.stocklocation.view.CreateStockLocationViewResponse;
import cm.xenonbyte.farmbyte.stock.adapter.rest.api.generated.stocklocation.view.FindStockLocationByIdViewResponse;
import cm.xenonbyte.farmbyte.stock.adapter.rest.api.generated.stocklocation.view.FindStockLocationsPageInfoViewResponse;
import cm.xenonbyte.farmbyte.stock.adapter.rest.api.generated.stocklocation.view.FindStockLocationsViewResponse;
import cm.xenonbyte.farmbyte.stock.adapter.rest.api.generated.stocklocation.view.SearchStockLocationsPageInfoViewResponse;
import cm.xenonbyte.farmbyte.stock.adapter.rest.api.generated.stocklocation.view.SearchStockLocationsViewResponse;
import cm.xenonbyte.farmbyte.stock.adapter.rest.api.generated.stocklocation.view.UpdateStockLocationViewRequest;
import cm.xenonbyte.farmbyte.stock.adapter.rest.api.generated.stocklocation.view.UpdateStockLocationViewResponse;
import cm.xenonbyte.farmbyte.stock.domain.core.stocklocation.StockLocation;
import cm.xenonbyte.farmbyte.stock.domain.core.stocklocation.StockLocationId;
import cm.xenonbyte.farmbyte.stock.domain.core.stocklocation.StockLocationNameConflictException;
import cm.xenonbyte.farmbyte.stock.domain.core.stocklocation.StockLocationNotFoundException;
import cm.xenonbyte.farmbyte.stock.domain.core.stocklocation.StockLocationParentIdNotFoundException;
import cm.xenonbyte.farmbyte.stock.domain.core.stocklocation.StockLocationService;
import cm.xenonbyte.farmbyte.stock.domain.core.stocklocation.StockLocationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static cm.xenonbyte.farmbyte.stock.domain.core.constant.StockDomainConstant.STOCK_LOCATION_NAME_CONFLICT;
import static cm.xenonbyte.farmbyte.stock.domain.core.constant.StockDomainConstant.STOCK_LOCATION_PARENT_ID_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author bamk
 * @version 1.0
 * @since 17/10/2024
 */
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public final class StockLocationDomainRestApiAdapterTest {
    private StockLocationServiceRestApiAdapter stockLocationServiceRestApiAdapter;

    @Mock
    private StockLocationViewMapper stockLocationViewMapper;

    @Mock
    private StockLocationService stockLocationService;

    @BeforeEach
    void setUp() {
        stockLocationServiceRestApiAdapter = new StockLocationDomainServiceRestApiAdapter(
                stockLocationService,
                stockLocationViewMapper
        );
    }

    @Nested
    class CreateStockLocationDomainServiceRestApiAdapterTest {

        public static Stream<Arguments> createStockLocationMethodSourceArgs() {
            return Stream.of(
                    Arguments.of(
                            "Root Location view",
                            CreateStockLocationViewRequest.TypeEnum.VIEW,
                            null,
                            UUID.fromString("01929bdf-e3e4-7022-8fab-89eca944e1fd")
                    ),
                    Arguments.of(
                            "Child Location View",
                            CreateStockLocationViewRequest.TypeEnum.INTERNAL,
                            UUID.fromString("01929bdb-8dab-7131-a10f-803ea6bddaad"),
                            UUID.fromString("01929bdf-fb8f-7b88-aa94-4d601ae307a1")
                    )
            );
        }

        @ParameterizedTest
        @MethodSource("createStockLocationMethodSourceArgs")
        void should_create_root_or_child_inventory_emplacement(
                String name,
                CreateStockLocationViewRequest.TypeEnum type,
                UUID parentId,
                UUID stockLocationId
        ) {
            //Given
            CreateStockLocationViewRequest createStockLocationViewRequest = new CreateStockLocationViewRequest()
                    .name(name)
                    .type(type)
                    .parentId(parentId);

            StockLocation createStockLocationRequest = StockLocation.builder()
                    .name(Name.of(Text.of(name)))
                    .type(StockLocationType.valueOf(type.name()))
                    .parentId(parentId == null ? null : new StockLocationId(parentId))
                    .build();

            StockLocation createStockLocationResponse = StockLocation.builder()
                    .id(new StockLocationId(stockLocationId))
                    .name(Name.of(Text.of(name)))
                    .type(StockLocationType.valueOf(type.name()))
                    .parentId(parentId == null ? null : new StockLocationId(parentId))
                    .build();

            CreateStockLocationViewResponse createStockLocationViewResponse = new CreateStockLocationViewResponse()
                    .id(stockLocationId)
                    .name(name)
                    .type(CreateStockLocationViewResponse.TypeEnum.valueOf(type.name()))
                    .parentId(parentId);

            when(stockLocationViewMapper.toStockLocation(createStockLocationViewRequest))
                    .thenReturn(createStockLocationRequest);
            when(stockLocationService.createStockLocation(createStockLocationRequest))
                    .thenReturn(createStockLocationResponse);
            when(stockLocationViewMapper.toCreateStockLocationViewResponse(createStockLocationResponse))
                    .thenReturn(createStockLocationViewResponse);

            ArgumentCaptor<CreateStockLocationViewRequest> createStockLocationViewRequestArgumentCaptor =
                    ArgumentCaptor.forClass(CreateStockLocationViewRequest.class);
            ArgumentCaptor<StockLocation> stockLocationArgumentCaptor =
                    ArgumentCaptor.forClass(StockLocation.class);

            //Act
            CreateStockLocationViewResponse result = stockLocationServiceRestApiAdapter.createStockLocation(createStockLocationViewRequest);

            verify(stockLocationViewMapper, times(1))
                    .toStockLocation(createStockLocationViewRequestArgumentCaptor.capture());
            verify(stockLocationService, times(1))
                    .createStockLocation(stockLocationArgumentCaptor.capture());
            verify(stockLocationViewMapper, times(1))
                    .toCreateStockLocationViewResponse(stockLocationArgumentCaptor.capture());

            //Then
            assertThat(result).isNotNull().isEqualTo(createStockLocationViewResponse);

            assertThat(createStockLocationViewRequestArgumentCaptor.getValue()).isEqualTo(createStockLocationViewRequest);
            assertThat(stockLocationArgumentCaptor.getAllValues().getFirst()).isEqualTo(createStockLocationRequest);
            assertThat(stockLocationArgumentCaptor.getAllValues().getLast()).isEqualTo(createStockLocationResponse);
        }

        @Test
        void should_fail_when_create_inventory_emplacement_with_existing_name() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
            //Given
            String name = "Root Location view";
            CreateStockLocationViewRequest.TypeEnum type = CreateStockLocationViewRequest.TypeEnum.VIEW;
            UUID parentId = null;
            CreateStockLocationViewRequest createStockLocationViewRequest = new CreateStockLocationViewRequest()
                    .name(name)
                    .type(type)
                    .parentId(parentId);

            StockLocation createStockLocationRequest = StockLocation.builder()
                    .name(Name.of(Text.of(name)))
                    .type(StockLocationType.valueOf(type.name()))
                    .parentId(parentId == null ? null : new StockLocationId(parentId))
                    .build();

            when(stockLocationViewMapper.toStockLocation(createStockLocationViewRequest))
                    .thenReturn(createStockLocationRequest);
            when(stockLocationService.createStockLocation(createStockLocationRequest))
                    .thenThrow(StockLocationNameConflictException.class.getConstructor(Object[].class).newInstance(new Object[]{new String[]{name}}));

            ArgumentCaptor<CreateStockLocationViewRequest> createStockLocationViewRequestArgumentCaptor =
                    ArgumentCaptor.forClass(CreateStockLocationViewRequest.class);
            ArgumentCaptor<StockLocation> stockLocationArgumentCaptor =
                    ArgumentCaptor.forClass(StockLocation.class);

            //Act + Then
            assertThatThrownBy(() -> stockLocationServiceRestApiAdapter.createStockLocation(createStockLocationViewRequest))
                .isInstanceOf(StockLocationNameConflictException.class)
                .hasMessage(STOCK_LOCATION_NAME_CONFLICT);

            verify(stockLocationViewMapper, times(1))
                    .toStockLocation(createStockLocationViewRequestArgumentCaptor.capture());
            verify(stockLocationService, times(1))
                    .createStockLocation(stockLocationArgumentCaptor.capture());

            assertThat(createStockLocationViewRequestArgumentCaptor.getValue()).isEqualTo(createStockLocationViewRequest);
            assertThat(stockLocationArgumentCaptor.getAllValues().getFirst()).isEqualTo(createStockLocationRequest);
        }

        @Test
        void should_fail_when_create_inventory_emplacement_with_non_existing_parent_id() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
            //Given
            String name = "Root Location view";
            CreateStockLocationViewRequest.TypeEnum type = CreateStockLocationViewRequest.TypeEnum.VIEW;
            UUID parentId = UUID.fromString("01929c5d-98bf-74dd-bbe7-275ed515fb49");
            CreateStockLocationViewRequest createStockLocationViewRequest = new CreateStockLocationViewRequest()
                    .name(name)
                    .type(type)
                    .parentId(parentId);

            StockLocation createStockLocationRequest = StockLocation.builder()
                    .name(Name.of(Text.of(name)))
                    .type(StockLocationType.valueOf(type.name()))
                    .parentId(parentId == null ? null : new StockLocationId(parentId))
                    .build();

            when(stockLocationViewMapper.toStockLocation(createStockLocationViewRequest))
                    .thenReturn(createStockLocationRequest);
            when(stockLocationService.createStockLocation(createStockLocationRequest))
                    .thenThrow(StockLocationParentIdNotFoundException.class.getConstructor(Object[].class).newInstance(new Object[]{new String[]{parentId.toString()}}));

            ArgumentCaptor<CreateStockLocationViewRequest> createStockLocationViewRequestArgumentCaptor =
                    ArgumentCaptor.forClass(CreateStockLocationViewRequest.class);
            ArgumentCaptor<StockLocation> stockLocationArgumentCaptor =
                    ArgumentCaptor.forClass(StockLocation.class);

            //Act + Then
            assertThatThrownBy(() -> stockLocationServiceRestApiAdapter.createStockLocation(createStockLocationViewRequest))
                    .isInstanceOf(StockLocationParentIdNotFoundException.class)
                    .hasMessage(STOCK_LOCATION_PARENT_ID_NOT_FOUND);

            verify(stockLocationViewMapper, times(1))
                    .toStockLocation(createStockLocationViewRequestArgumentCaptor.capture());
            verify(stockLocationService, times(1))
                    .createStockLocation(stockLocationArgumentCaptor.capture());

            assertThat(createStockLocationViewRequestArgumentCaptor.getValue()).isEqualTo(createStockLocationViewRequest);
            assertThat(stockLocationArgumentCaptor.getAllValues().getFirst()).isEqualTo(createStockLocationRequest);
        }
    }
    
    @Nested
    class FindStockLocationByIdDomainServiceRestApiAdapterTest {

        @Test
        void should_success_when_find_inventory_emplacement_by_id() {
            //Given
            UUID stockLocationUUID = UUID.fromString("0192a6b0-fd7e-753a-81bf-98a2a1ce9ab5");
            String name = "Internal Stock Location";

            StockLocationId stockLocationId = new StockLocationId(stockLocationUUID);
            StockLocation stockLocation = StockLocation.builder()
                    .id(stockLocationId)
                    .name(Name.of(Text.of(name)))
                    .type(StockLocationType.INTERNAL)
                    .active(Active.with(true))
                    .build();

            FindStockLocationByIdViewResponse findStockLocationByIdViewResponse =
                    new FindStockLocationByIdViewResponse()
                        .id(stockLocationUUID)
                        .name(name)
                        .type(FindStockLocationByIdViewResponse.TypeEnum.INTERNAL)
                        .active(true);

            when(stockLocationService.findStockLocationById(stockLocationId))
                    .thenReturn(stockLocation);
            when(stockLocationViewMapper.toFindStockLocationByIdViewResponse(stockLocation))
                    .thenReturn(findStockLocationByIdViewResponse);

            ArgumentCaptor<StockLocation> stockLocationArgumentCaptor =
                    ArgumentCaptor.forClass(StockLocation.class);

            ArgumentCaptor<StockLocationId> stockLocationIdArgumentCaptor =
                    ArgumentCaptor.forClass(StockLocationId.class);

            //Act
            FindStockLocationByIdViewResponse result = stockLocationServiceRestApiAdapter.findStockLocationById(stockLocationUUID);

            //Then
            assertThat(result).isNotNull().isEqualTo(findStockLocationByIdViewResponse);

            verify(stockLocationService, times(1))
                    .findStockLocationById(stockLocationIdArgumentCaptor.capture());
            verify(stockLocationViewMapper, times(1))
                    .toFindStockLocationByIdViewResponse(stockLocationArgumentCaptor.capture());

            assertThat(stockLocationArgumentCaptor.getValue()).isEqualTo(stockLocation);
            assertThat(stockLocationIdArgumentCaptor.getValue()).isEqualTo(stockLocationId);

        }

        @Test
        void should_fail_when_find_inventory_emplacement_with_non_existing_id() {
            //Given
            UUID stockLocationIdUUID = UUID.randomUUID();
            StockLocationId stockLocationId = new StockLocationId(stockLocationIdUUID);

            when(stockLocationService.findStockLocationById(stockLocationId)).thenThrow(StockLocationNotFoundException.class);
            ArgumentCaptor<StockLocationId> stockLocationIdArgumentCaptor = ArgumentCaptor.forClass(StockLocationId.class);

            //Act + Then
            assertThatThrownBy(() -> stockLocationServiceRestApiAdapter.findStockLocationById(stockLocationIdUUID))
                    .isInstanceOf(StockLocationNotFoundException.class);

            verify(stockLocationService, times(1)).findStockLocationById(stockLocationIdArgumentCaptor.capture());

            assertThat(stockLocationIdArgumentCaptor.getValue()).isEqualTo(stockLocationId);
        }
    }

    @Nested
    class FindStockLocationsDomainServiceRestApiAdapterTest {

        @Test
        void should_success_when_find_inventory_emplacements() {
            //Given

            int page = 1;
            int pageSize = 2;
            String attribute = "name";

            PageInfo<StockLocation> stockLocationsPageInfo =  new PageInfo<StockLocation>().with(
                    page, pageSize,
                    List.of(
                            StockLocation.builder()
                                    .name(Name.of(Text.of("Internal Location 1")))
                                    .id(new StockLocationId(UUID.randomUUID()))
                                    .type(StockLocationType.INTERNAL)
                                    .build(),
                            StockLocation.builder()
                                    .name(Name.of(Text.of("Internal Location 2")))
                                    .id(new StockLocationId(UUID.randomUUID()))
                                    .type(StockLocationType.INTERNAL)
                                    .build(),
                            StockLocation.builder()
                                    .name(Name.of(Text.of("Customer Location 1")))
                                    .id(new StockLocationId(UUID.randomUUID()))
                                    .type(StockLocationType.CUSTOMER)
                                    .build(),
                            StockLocation.builder()
                                    .name(Name.of(Text.of("Supplier Location 1")))
                                    .id(new StockLocationId(UUID.randomUUID()))
                                    .type(StockLocationType.SUPPLIER)
                                    .build(),
                            StockLocation.builder()
                                    .name(Name.of(Text.of("Transit Location 1")))
                                    .id(new StockLocationId(UUID.randomUUID()))
                                    .type(StockLocationType.TRANSIT)
                                    .build()
                    )
            );

            FindStockLocationsPageInfoViewResponse stockLocationsPageInfoViewResponse = new FindStockLocationsPageInfoViewResponse()
                    .first(true)
                    .last(false)
                    .pageSize(2)
                    .totalElements(5L)
                    .totalPages(3)
                    .elements(
                            List.of(
                                    new FindStockLocationsViewResponse()
                                            .name("Internal Location 1")
                                            .type(FindStockLocationsViewResponse.TypeEnum.INTERNAL)
                                            .id(UUID.randomUUID()),
                                    new FindStockLocationsViewResponse()
                                            .name("Internal Location 2")
                                            .type(FindStockLocationsViewResponse.TypeEnum.INTERNAL)
                                            .id(UUID.randomUUID()),
                                    new FindStockLocationsViewResponse()
                                            .name("Customer Location 1")
                                            .type(FindStockLocationsViewResponse.TypeEnum.CUSTOMER)
                                            .id(UUID.randomUUID()),
                                    new FindStockLocationsViewResponse()
                                            .name("Supplier Location 1")
                                            .type(FindStockLocationsViewResponse.TypeEnum.SUPPLIER)
                                            .id(UUID.randomUUID()),
                                    new FindStockLocationsViewResponse()
                                            .name("Transit Location 1")
                                            .type(FindStockLocationsViewResponse.TypeEnum.TRANSIT)
                                            .id(UUID.randomUUID())
                            )
                    );


            when(stockLocationService.findStockLocations(page, pageSize, "name", Direction.ASC)).thenReturn(stockLocationsPageInfo);
            when(stockLocationViewMapper.toFindStockLocationsPageInfoViewResponse(stockLocationsPageInfo)).thenReturn(stockLocationsPageInfoViewResponse);

            ArgumentCaptor<PageInfo> pageInfoArgumentCaptor = ArgumentCaptor.forClass(PageInfo.class);
            ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
            ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
            ArgumentCaptor<Direction> directionArgumentCaptor = ArgumentCaptor.forClass(Direction.class);

            //Act
            FindStockLocationsPageInfoViewResponse result =
                    stockLocationServiceRestApiAdapter.findStockLocations(page, pageSize, attribute, "ASC");

            //Then
            assertThat(result)
                    .isNotNull()
                    .isEqualTo(stockLocationsPageInfoViewResponse);

            verify(stockLocationService, times(1))
                    .findStockLocations(integerArgumentCaptor.capture(), integerArgumentCaptor.capture(),
                            stringArgumentCaptor.capture(), directionArgumentCaptor.capture());

            verify(stockLocationViewMapper, times(1))
                    .toFindStockLocationsPageInfoViewResponse(pageInfoArgumentCaptor.capture());

            assertThat(integerArgumentCaptor.getAllValues().get(0)).isEqualTo(page);
            assertThat(integerArgumentCaptor.getAllValues().get(1)).isEqualTo(pageSize);
            assertThat(stringArgumentCaptor.getAllValues().getFirst()).isEqualTo(attribute);
            assertThat(directionArgumentCaptor.getAllValues().getFirst()).isEqualTo(Direction.ASC);
            assertThat(pageInfoArgumentCaptor.getValue()).isEqualTo(stockLocationsPageInfo);
        }
    }


    @Nested
    class SearchStockLocationsDomainServiceRestApiAdapterTest {

        @Test
        void should_success_when_search_inventory_emplacement_with_existing_keyword() {
            //Given
            int page = 1;
            int pageSize = 2;
            String attribute = "name";
            String keyword = "int";

            PageInfo<StockLocation> stockLocationsPageInfo =  new PageInfo<StockLocation>().with(
                    page, pageSize,
                    List.of(
                            StockLocation.builder()
                                    .name(Name.of(Text.of("Internal Location 1")))
                                    .id(new StockLocationId(UUID.randomUUID()))
                                    .type(StockLocationType.INTERNAL)
                                    .build(),
                            StockLocation.builder()
                                    .name(Name.of(Text.of("Internal Location 2")))
                                    .id(new StockLocationId(UUID.randomUUID()))
                                    .type(StockLocationType.INTERNAL)
                                    .build(),
                            StockLocation.builder()
                                    .name(Name.of(Text.of("Customer Location 1")))
                                    .id(new StockLocationId(UUID.randomUUID()))
                                    .type(StockLocationType.CUSTOMER)
                                    .build(),
                            StockLocation.builder()
                                    .name(Name.of(Text.of("Supplier Location 1")))
                                    .id(new StockLocationId(UUID.randomUUID()))
                                    .type(StockLocationType.SUPPLIER)
                                    .build(),
                            StockLocation.builder()
                                    .name(Name.of(Text.of("Transit Location 1")))
                                    .id(new StockLocationId(UUID.randomUUID()))
                                    .type(StockLocationType.TRANSIT)
                                    .build()
                    )
            );

            SearchStockLocationsPageInfoViewResponse stockLocationsPageInfoViewResponse = new SearchStockLocationsPageInfoViewResponse()
                    .first(true)
                    .last(false)
                    .pageSize(2)
                    .totalElements(5L)
                    .totalPages(1)
                    .elements(
                            List.of(
                                    new SearchStockLocationsViewResponse()
                                            .name("Internal Location 1")
                                            .type(SearchStockLocationsViewResponse.TypeEnum.INTERNAL)
                                            .id(UUID.randomUUID()),
                                    new SearchStockLocationsViewResponse()
                                            .name("Internal Location 2")
                                            .type(SearchStockLocationsViewResponse.TypeEnum.INTERNAL)
                                            .id(UUID.randomUUID()),
                                    new SearchStockLocationsViewResponse()
                                            .name("Customer Location 1")
                                            .type(SearchStockLocationsViewResponse.TypeEnum.CUSTOMER)
                                            .id(UUID.randomUUID()),
                                    new SearchStockLocationsViewResponse()
                                            .name("Supplier Location 1")
                                            .type(SearchStockLocationsViewResponse.TypeEnum.SUPPLIER)
                                            .id(UUID.randomUUID()),
                                    new SearchStockLocationsViewResponse()
                                            .name("Transit Location 1")
                                            .type(SearchStockLocationsViewResponse.TypeEnum.TRANSIT)
                                            .id(UUID.randomUUID())
                            )
                    );


            when(stockLocationService.searchStockLocations(page, pageSize, "name", Direction.ASC, Keyword.of(Text.of(keyword)))).thenReturn(stockLocationsPageInfo);
            when(stockLocationViewMapper.toSearchStockLocationsPageInfoViewResponse(stockLocationsPageInfo)).thenReturn(stockLocationsPageInfoViewResponse);

            ArgumentCaptor<PageInfo> pageInfoArgumentCaptor = ArgumentCaptor.forClass(PageInfo.class);
            ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
            ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
            ArgumentCaptor<Direction> directionArgumentCaptor = ArgumentCaptor.forClass(Direction.class);
            ArgumentCaptor<Keyword> keywordArgumentCaptor = ArgumentCaptor.forClass(Keyword.class);

            //Act
            SearchStockLocationsPageInfoViewResponse result =
                    stockLocationServiceRestApiAdapter.searchStockLocations(page, pageSize, attribute, "ASC", keyword);

            //Then
            assertThat(result)
                    .isNotNull()
                    .isEqualTo(stockLocationsPageInfoViewResponse);

            verify(stockLocationService, times(1))
                    .searchStockLocations(integerArgumentCaptor.capture(), integerArgumentCaptor.capture(),
                            stringArgumentCaptor.capture(), directionArgumentCaptor.capture(), keywordArgumentCaptor.capture());

            verify(stockLocationViewMapper, times(1))
                    .toSearchStockLocationsPageInfoViewResponse(pageInfoArgumentCaptor.capture());

            assertThat(integerArgumentCaptor.getAllValues().get(0)).isEqualTo(page);
            assertThat(integerArgumentCaptor.getAllValues().get(1)).isEqualTo(pageSize);
            assertThat(stringArgumentCaptor.getAllValues().getFirst()).isEqualTo(attribute);
            assertThat(directionArgumentCaptor.getAllValues().getFirst()).isEqualTo(Direction.ASC);
            assertThat(keywordArgumentCaptor.getValue().getText().getValue()).isEqualTo(keyword);
            assertThat(pageInfoArgumentCaptor.getValue()).isEqualTo(stockLocationsPageInfo);
        }
    }

    @Nested
    class UpdateStockLocationDomainServiceRestApiAdapterTest {
        @Test
        void should_success_when_update_stock_location_by_id() {
            //Given
            UUID stockLocationUUID = UUID.fromString("0192c540-3469-7da3-89e6-c8e65c701697");
            StockLocationId stockLocationId = new StockLocationId(stockLocationUUID);
            String name = "Internal Location Update";

            UpdateStockLocationViewRequest updateStockLocationViewRequest = new UpdateStockLocationViewRequest()
                    .id(stockLocationUUID)
                    .name(name)
                    .type(UpdateStockLocationViewRequest.TypeEnum.INTERNAL)
                    .active(true);

            StockLocation stockLocation = StockLocation.builder()
                    .id(stockLocationId)
                    .name(Name.of(Text.of(name)))
                    .type(StockLocationType.valueOf(updateStockLocationViewRequest.getType().name()))
                    .active(Active.with(true))
                    .build();

            UpdateStockLocationViewResponse updateStockLocationViewResponse = new UpdateStockLocationViewResponse()
                    .id(stockLocationUUID)
                    .name(name)
                    .type(UpdateStockLocationViewResponse.TypeEnum.INTERNAL)
                    .active(true);

            when(stockLocationViewMapper.toStockLocation(updateStockLocationViewRequest)).thenReturn(stockLocation);
            when(stockLocationService.updateStockLocation(stockLocationId, stockLocation)).thenReturn(stockLocation);
            when(stockLocationViewMapper.toUpdateStockLocationViewResponse(stockLocation)).thenReturn(updateStockLocationViewResponse);

            ArgumentCaptor<StockLocationId> stockLocationIdArgumentCaptor =
                    ArgumentCaptor.forClass(StockLocationId.class);

            ArgumentCaptor<UpdateStockLocationViewRequest> updateStockLocationViewRequestArgumentCaptor =
                    ArgumentCaptor.forClass(UpdateStockLocationViewRequest.class);

            ArgumentCaptor<StockLocation> stockLocationArgumentCaptor = ArgumentCaptor.forClass(StockLocation.class);

            //Act
            UpdateStockLocationViewResponse result =
                    stockLocationServiceRestApiAdapter.updateStockLocation(stockLocationUUID, updateStockLocationViewRequest);

            //Then
            assertThat(result).isNotNull()
                    .isEqualTo(updateStockLocationViewResponse);

            verify(stockLocationViewMapper, times(1))
                    .toStockLocation(updateStockLocationViewRequestArgumentCaptor.capture());

            verify(stockLocationService, times(1))
                    .updateStockLocation(stockLocationIdArgumentCaptor.capture(), stockLocationArgumentCaptor.capture());

            verify(stockLocationViewMapper, times(1))
                    .toUpdateStockLocationViewResponse(stockLocationArgumentCaptor.capture());


            assertThat(updateStockLocationViewRequestArgumentCaptor.getValue()).isEqualTo(updateStockLocationViewRequest);
            assertThat(stockLocationIdArgumentCaptor.getValue()).isEqualTo(stockLocationId);
            assertThat(stockLocationArgumentCaptor.getAllValues().getFirst()).isEqualTo(stockLocation);
            assertThat(stockLocationArgumentCaptor.getAllValues().getLast()).isEqualTo(stockLocation);

        }

        @Test
        void should_fail_when_update_stock_location_name_with_existing_name()
                throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
            //Given
            UUID stockLocationUUID = UUID.fromString("0192c540-3469-7da3-89e6-c8e65c701697");
            StockLocationId stockLocationId = new StockLocationId(stockLocationUUID);
            String name = "Internal Location Update";

            UpdateStockLocationViewRequest updateStockLocationViewRequest = new UpdateStockLocationViewRequest()
                    .id(stockLocationUUID)
                    .name(name)
                    .type(UpdateStockLocationViewRequest.TypeEnum.INTERNAL)
                    .active(true);

            StockLocation stockLocation = StockLocation.builder()
                    .id(stockLocationId)
                    .name(Name.of(Text.of(name)))
                    .type(StockLocationType.valueOf(updateStockLocationViewRequest.getType().name()))
                    .active(Active.with(true))
                    .build();

            when(stockLocationViewMapper.toStockLocation(updateStockLocationViewRequest)).thenReturn(stockLocation);
            when(stockLocationService.updateStockLocation(stockLocationId, stockLocation)).thenThrow(
                    StockLocationNameConflictException.class.getConstructor(Object[].class).newInstance(
                            new Object[]{new String[]{name}}
                    )
            );

            assertThatThrownBy(() -> stockLocationServiceRestApiAdapter.updateStockLocation(stockLocationUUID, updateStockLocationViewRequest))
                    .isInstanceOf(StockLocationNameConflictException.class)
                    .hasMessage(STOCK_LOCATION_NAME_CONFLICT);
        }

        @Test
        void should_fail_when_update_stock_location_name_with_non_existing_parent_id()
                throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
            //Given
            UUID stockLocationUUID = UUID.fromString("0192c540-3469-7da3-89e6-c8e65c701697");
            StockLocationId stockLocationId = new StockLocationId(stockLocationUUID);
            String name = "Internal Location Update";
            UUID parentUUID = UUID.fromString("0192c555-aa14-7113-a3be-579e47351102");

            UpdateStockLocationViewRequest updateStockLocationViewRequest = new UpdateStockLocationViewRequest()
                    .id(stockLocationUUID)
                    .name(name)
                    .parentId(parentUUID)
                    .type(UpdateStockLocationViewRequest.TypeEnum.INTERNAL)
                    .active(true);

            StockLocation stockLocation = StockLocation.builder()
                    .id(stockLocationId)
                    .name(Name.of(Text.of(name)))
                    .type(StockLocationType.valueOf(updateStockLocationViewRequest.getType().name()))
                    .active(Active.with(true))
                    .build();

            when(stockLocationViewMapper.toStockLocation(updateStockLocationViewRequest)).thenReturn(stockLocation);
            when(stockLocationService.updateStockLocation(stockLocationId, stockLocation)).thenThrow(
                    StockLocationParentIdNotFoundException.class.getConstructor(Object[].class).newInstance(
                            new Object[]{new String[]{parentUUID.toString()}}
                    )
            );

            assertThatThrownBy(() -> stockLocationServiceRestApiAdapter.updateStockLocation(stockLocationUUID, updateStockLocationViewRequest))
                    .isInstanceOf(StockLocationParentIdNotFoundException.class)
                    .hasMessage(STOCK_LOCATION_PARENT_ID_NOT_FOUND);
        }
    }


}
