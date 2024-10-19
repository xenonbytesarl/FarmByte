package cm.xenonbyte.farmbyte.inventory.adapter.rest.api;

import cm.xenonbyte.farmbyte.common.domain.vo.Active;
import cm.xenonbyte.farmbyte.common.domain.vo.Direction;
import cm.xenonbyte.farmbyte.common.domain.vo.Keyword;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.PageInfo;
import cm.xenonbyte.farmbyte.common.domain.vo.Text;
import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.uom.view.CreateInventoryEmplacementViewRequest;
import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.uom.view.CreateInventoryEmplacementViewResponse;
import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.uom.view.FindInventoryEmplacementByIdViewResponse;
import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.uom.view.FindInventoryEmplacementsPageInfoViewResponse;
import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.uom.view.FindInventoryEmplacementsViewResponse;
import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.uom.view.SearchInventoryEmplacementsPageInfoViewResponse;
import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.uom.view.SearchInventoryEmplacementsViewResponse;
import cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacement;
import cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacementId;
import cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacementNameConflictException;
import cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacementNotFoundException;
import cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacementParentIdNotFoundException;
import cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacementService;
import cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacementType;
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

import static cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacementNameConflictException.INVENTORY_EMPLACEMENT_NAME_CONFLICT;
import static cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacementParentIdNotFoundException.INVENTORY_EMPLACEMENT_PARENT_ID_NOT_FOUND;
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
public final class InventoryEmplacementDomainRestApiAdapterTest {
    private InventoryEmplacementServiceRestApiAdapter inventoryEmplacementServiceRestApiAdapter;

    @Mock
    private InventoryEmplacementViewMapper inventoryEmplacementViewMapper;

    @Mock
    private InventoryEmplacementService inventoryEmplacementService;

    @BeforeEach
    void setUp() {
        inventoryEmplacementServiceRestApiAdapter = new InventoryEmplacementDomainServiceRestApiAdapter(
                inventoryEmplacementService,
                inventoryEmplacementViewMapper
        );
    }

    @Nested
    class CreateInventoryEmplacementDomainServiceRestApiAdapterTest {

        public static Stream<Arguments> createInventoryEmplacementMethodSourceArgs() {
            return Stream.of(
                    Arguments.of(
                            "Root Emplacement view",
                            CreateInventoryEmplacementViewRequest.TypeEnum.VIEW,
                            null,
                            UUID.fromString("01929bdf-e3e4-7022-8fab-89eca944e1fd")
                    ),
                    Arguments.of(
                            "Child Emplacement View",
                            CreateInventoryEmplacementViewRequest.TypeEnum.INTERNAL,
                            UUID.fromString("01929bdb-8dab-7131-a10f-803ea6bddaad"),
                            UUID.fromString("01929bdf-fb8f-7b88-aa94-4d601ae307a1")
                    )
            );
        }

        @ParameterizedTest
        @MethodSource("createInventoryEmplacementMethodSourceArgs")
        void should_create_root_or_child_inventory_emplacement(
                String name,
                CreateInventoryEmplacementViewRequest.TypeEnum type,
                UUID parentId,
                UUID inventoryEmplacementId
        ) {
            //Given
            CreateInventoryEmplacementViewRequest createInventoryEmplacementViewRequest = new CreateInventoryEmplacementViewRequest()
                    .name(name)
                    .type(type)
                    .parentId(parentId);

            InventoryEmplacement createInventoryEmplacementRequest = InventoryEmplacement.builder()
                    .name(Name.of(Text.of(name)))
                    .type(InventoryEmplacementType.valueOf(type.name()))
                    .parentId(parentId == null ? null : new InventoryEmplacementId(parentId))
                    .build();

            InventoryEmplacement createInventoryEmplacementResponse = InventoryEmplacement.builder()
                    .id(new InventoryEmplacementId(inventoryEmplacementId))
                    .name(Name.of(Text.of(name)))
                    .type(InventoryEmplacementType.valueOf(type.name()))
                    .parentId(parentId == null ? null : new InventoryEmplacementId(parentId))
                    .build();

            CreateInventoryEmplacementViewResponse createInventoryEmplacementViewResponse = new CreateInventoryEmplacementViewResponse()
                    .id(inventoryEmplacementId)
                    .name(name)
                    .type(CreateInventoryEmplacementViewResponse.TypeEnum.valueOf(type.name()))
                    .parentId(parentId);

            when(inventoryEmplacementViewMapper.toInventoryEmplacement(createInventoryEmplacementViewRequest))
                    .thenReturn(createInventoryEmplacementRequest);
            when(inventoryEmplacementService.createInventoryEmplacement(createInventoryEmplacementRequest))
                    .thenReturn(createInventoryEmplacementResponse);
            when(inventoryEmplacementViewMapper.toCreateInventoryEmplacementViewResponse(createInventoryEmplacementResponse))
                    .thenReturn(createInventoryEmplacementViewResponse);

            ArgumentCaptor<CreateInventoryEmplacementViewRequest> createInventoryEmplacementViewRequestArgumentCaptor =
                    ArgumentCaptor.forClass(CreateInventoryEmplacementViewRequest.class);
            ArgumentCaptor<InventoryEmplacement> inventoryEmplacementArgumentCaptor =
                    ArgumentCaptor.forClass(InventoryEmplacement.class);

            //Act
            CreateInventoryEmplacementViewResponse result = inventoryEmplacementServiceRestApiAdapter.createInventoryEmplacement(createInventoryEmplacementViewRequest);

            verify(inventoryEmplacementViewMapper, times(1))
                    .toInventoryEmplacement(createInventoryEmplacementViewRequestArgumentCaptor.capture());
            verify(inventoryEmplacementService, times(1))
                    .createInventoryEmplacement(inventoryEmplacementArgumentCaptor.capture());
            verify(inventoryEmplacementViewMapper, times(1))
                    .toCreateInventoryEmplacementViewResponse(inventoryEmplacementArgumentCaptor.capture());

            //Then
            assertThat(result).isNotNull().isEqualTo(createInventoryEmplacementViewResponse);

            assertThat(createInventoryEmplacementViewRequestArgumentCaptor.getValue()).isEqualTo(createInventoryEmplacementViewRequest);
            assertThat(inventoryEmplacementArgumentCaptor.getAllValues().getFirst()).isEqualTo(createInventoryEmplacementRequest);
            assertThat(inventoryEmplacementArgumentCaptor.getAllValues().getLast()).isEqualTo(createInventoryEmplacementResponse);
        }

        @Test
        void should_fail_when_create_inventory_emplacement_with_existing_name() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
            //Given
            String name = "Root Emplacement view";
            CreateInventoryEmplacementViewRequest.TypeEnum type = CreateInventoryEmplacementViewRequest.TypeEnum.VIEW;
            UUID parentId = null;
            CreateInventoryEmplacementViewRequest createInventoryEmplacementViewRequest = new CreateInventoryEmplacementViewRequest()
                    .name(name)
                    .type(type)
                    .parentId(parentId);

            InventoryEmplacement createInventoryEmplacementRequest = InventoryEmplacement.builder()
                    .name(Name.of(Text.of(name)))
                    .type(InventoryEmplacementType.valueOf(type.name()))
                    .parentId(parentId == null ? null : new InventoryEmplacementId(parentId))
                    .build();

            when(inventoryEmplacementViewMapper.toInventoryEmplacement(createInventoryEmplacementViewRequest))
                    .thenReturn(createInventoryEmplacementRequest);
            when(inventoryEmplacementService.createInventoryEmplacement(createInventoryEmplacementRequest))
                    .thenThrow(InventoryEmplacementNameConflictException.class.getConstructor(Object[].class).newInstance(new Object[]{new String[]{name}}));

            ArgumentCaptor<CreateInventoryEmplacementViewRequest> createInventoryEmplacementViewRequestArgumentCaptor =
                    ArgumentCaptor.forClass(CreateInventoryEmplacementViewRequest.class);
            ArgumentCaptor<InventoryEmplacement> inventoryEmplacementArgumentCaptor =
                    ArgumentCaptor.forClass(InventoryEmplacement.class);

            //Act + Then
            assertThatThrownBy(() -> inventoryEmplacementServiceRestApiAdapter.createInventoryEmplacement(createInventoryEmplacementViewRequest))
                .isInstanceOf(InventoryEmplacementNameConflictException.class)
                .hasMessage(INVENTORY_EMPLACEMENT_NAME_CONFLICT);

            verify(inventoryEmplacementViewMapper, times(1))
                    .toInventoryEmplacement(createInventoryEmplacementViewRequestArgumentCaptor.capture());
            verify(inventoryEmplacementService, times(1))
                    .createInventoryEmplacement(inventoryEmplacementArgumentCaptor.capture());

            assertThat(createInventoryEmplacementViewRequestArgumentCaptor.getValue()).isEqualTo(createInventoryEmplacementViewRequest);
            assertThat(inventoryEmplacementArgumentCaptor.getAllValues().getFirst()).isEqualTo(createInventoryEmplacementRequest);
        }

        @Test
        void should_fail_when_create_inventory_emplacement_with_non_existing_parent_id() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
            //Given
            String name = "Root Emplacement view";
            CreateInventoryEmplacementViewRequest.TypeEnum type = CreateInventoryEmplacementViewRequest.TypeEnum.VIEW;
            UUID parentId = UUID.fromString("01929c5d-98bf-74dd-bbe7-275ed515fb49");
            CreateInventoryEmplacementViewRequest createInventoryEmplacementViewRequest = new CreateInventoryEmplacementViewRequest()
                    .name(name)
                    .type(type)
                    .parentId(parentId);

            InventoryEmplacement createInventoryEmplacementRequest = InventoryEmplacement.builder()
                    .name(Name.of(Text.of(name)))
                    .type(InventoryEmplacementType.valueOf(type.name()))
                    .parentId(parentId == null ? null : new InventoryEmplacementId(parentId))
                    .build();

            when(inventoryEmplacementViewMapper.toInventoryEmplacement(createInventoryEmplacementViewRequest))
                    .thenReturn(createInventoryEmplacementRequest);
            when(inventoryEmplacementService.createInventoryEmplacement(createInventoryEmplacementRequest))
                    .thenThrow(InventoryEmplacementParentIdNotFoundException.class.getConstructor(Object[].class).newInstance(new Object[]{new String[]{parentId.toString()}}));

            ArgumentCaptor<CreateInventoryEmplacementViewRequest> createInventoryEmplacementViewRequestArgumentCaptor =
                    ArgumentCaptor.forClass(CreateInventoryEmplacementViewRequest.class);
            ArgumentCaptor<InventoryEmplacement> inventoryEmplacementArgumentCaptor =
                    ArgumentCaptor.forClass(InventoryEmplacement.class);

            //Act + Then
            assertThatThrownBy(() -> inventoryEmplacementServiceRestApiAdapter.createInventoryEmplacement(createInventoryEmplacementViewRequest))
                    .isInstanceOf(InventoryEmplacementParentIdNotFoundException.class)
                    .hasMessage(INVENTORY_EMPLACEMENT_PARENT_ID_NOT_FOUND);

            verify(inventoryEmplacementViewMapper, times(1))
                    .toInventoryEmplacement(createInventoryEmplacementViewRequestArgumentCaptor.capture());
            verify(inventoryEmplacementService, times(1))
                    .createInventoryEmplacement(inventoryEmplacementArgumentCaptor.capture());

            assertThat(createInventoryEmplacementViewRequestArgumentCaptor.getValue()).isEqualTo(createInventoryEmplacementViewRequest);
            assertThat(inventoryEmplacementArgumentCaptor.getAllValues().getFirst()).isEqualTo(createInventoryEmplacementRequest);
        }
    }
    
    @Nested
    class FindInventoryEmplacementByIdDomainServiceRestApiAdapterTest {

        @Test
        void should_success_when_find_inventory_emplacement_by_id() {
            //Given
            UUID inventoryEmplacementUUID = UUID.fromString("0192a6b0-fd7e-753a-81bf-98a2a1ce9ab5");
            String name = "Internal Inventory Emplacement";

            InventoryEmplacementId inventoryEmplacementId = new InventoryEmplacementId(inventoryEmplacementUUID);
            InventoryEmplacement inventoryEmplacement = InventoryEmplacement.builder()
                    .id(inventoryEmplacementId)
                    .name(Name.of(Text.of(name)))
                    .type(InventoryEmplacementType.INTERNAL)
                    .active(Active.with(true))
                    .build();

            FindInventoryEmplacementByIdViewResponse findInventoryEmplacementByIdViewResponse =
                    new FindInventoryEmplacementByIdViewResponse()
                        .id(inventoryEmplacementUUID)
                        .name(name)
                        .type(FindInventoryEmplacementByIdViewResponse.TypeEnum.INTERNAL)
                        .active(true);

            when(inventoryEmplacementService.findInventoryEmplacementById(inventoryEmplacementId))
                    .thenReturn(inventoryEmplacement);
            when(inventoryEmplacementViewMapper.toFindInventoryEmplacementByIdViewResponse(inventoryEmplacement))
                    .thenReturn(findInventoryEmplacementByIdViewResponse);

            ArgumentCaptor<InventoryEmplacement> inventoryEmplacementArgumentCaptor =
                    ArgumentCaptor.forClass(InventoryEmplacement.class);

            ArgumentCaptor<InventoryEmplacementId> inventoryEmplacementIdArgumentCaptor =
                    ArgumentCaptor.forClass(InventoryEmplacementId.class);

            //Act
            FindInventoryEmplacementByIdViewResponse result = inventoryEmplacementServiceRestApiAdapter.findInventoryEmplacementById(inventoryEmplacementUUID);

            //Then
            assertThat(result).isNotNull().isEqualTo(findInventoryEmplacementByIdViewResponse);

            verify(inventoryEmplacementService, times(1))
                    .findInventoryEmplacementById(inventoryEmplacementIdArgumentCaptor.capture());
            verify(inventoryEmplacementViewMapper, times(1))
                    .toFindInventoryEmplacementByIdViewResponse(inventoryEmplacementArgumentCaptor.capture());

            assertThat(inventoryEmplacementArgumentCaptor.getValue()).isEqualTo(inventoryEmplacement);
            assertThat(inventoryEmplacementIdArgumentCaptor.getValue()).isEqualTo(inventoryEmplacementId);

        }

        @Test
        void should_fail_when_find_inventory_emplacement_with_non_existing_id() {
            //Given
            UUID inventoryEmplacementIdUUID = UUID.randomUUID();
            InventoryEmplacementId inventoryEmplacementId = new InventoryEmplacementId(inventoryEmplacementIdUUID);

            when(inventoryEmplacementService.findInventoryEmplacementById(inventoryEmplacementId)).thenThrow(InventoryEmplacementNotFoundException.class);
            ArgumentCaptor<InventoryEmplacementId> inventoryEmplacementIdArgumentCaptor = ArgumentCaptor.forClass(InventoryEmplacementId.class);

            //Act + Then
            assertThatThrownBy(() -> inventoryEmplacementServiceRestApiAdapter.findInventoryEmplacementById(inventoryEmplacementIdUUID))
                    .isInstanceOf(InventoryEmplacementNotFoundException.class);

            verify(inventoryEmplacementService, times(1)).findInventoryEmplacementById(inventoryEmplacementIdArgumentCaptor.capture());

            assertThat(inventoryEmplacementIdArgumentCaptor.getValue()).isEqualTo(inventoryEmplacementId);
        }
    }

    @Nested
    class FindInventoryEmplacementsDomainServiceRestApiAdapterTest {

        @Test
        void should_success_when_find_inventory_emplacements() {
            //Given

            int page = 1;
            int pageSize = 2;
            String attribute = "name";

            PageInfo<InventoryEmplacement> inventoryEmplacementsPageInfo =  new PageInfo<InventoryEmplacement>().with(
                    page, pageSize,
                    List.of(
                            InventoryEmplacement.builder()
                                    .name(Name.of(Text.of("Internal Emplacement 1")))
                                    .id(new InventoryEmplacementId(UUID.randomUUID()))
                                    .type(InventoryEmplacementType.INTERNAL)
                                    .build(),
                            InventoryEmplacement.builder()
                                    .name(Name.of(Text.of("Internal Emplacement 2")))
                                    .id(new InventoryEmplacementId(UUID.randomUUID()))
                                    .type(InventoryEmplacementType.INTERNAL)
                                    .build(),
                            InventoryEmplacement.builder()
                                    .name(Name.of(Text.of("Customer Emplacement 1")))
                                    .id(new InventoryEmplacementId(UUID.randomUUID()))
                                    .type(InventoryEmplacementType.CUSTOMER)
                                    .build(),
                            InventoryEmplacement.builder()
                                    .name(Name.of(Text.of("Supplier Emplacement 1")))
                                    .id(new InventoryEmplacementId(UUID.randomUUID()))
                                    .type(InventoryEmplacementType.SUPPLIER)
                                    .build(),
                            InventoryEmplacement.builder()
                                    .name(Name.of(Text.of("Transit Emplacement 1")))
                                    .id(new InventoryEmplacementId(UUID.randomUUID()))
                                    .type(InventoryEmplacementType.TRANSIT)
                                    .build()
                    )
            );

            FindInventoryEmplacementsPageInfoViewResponse inventoryEmplacementsPageInfoViewResponse = new FindInventoryEmplacementsPageInfoViewResponse()
                    .first(true)
                    .last(false)
                    .pageSize(2)
                    .totalElements(5L)
                    .totalPages(3)
                    .elements(
                            List.of(
                                    new FindInventoryEmplacementsViewResponse()
                                            .name("Internal Emplacement 1")
                                            .type(FindInventoryEmplacementsViewResponse.TypeEnum.INTERNAL)
                                            .id(UUID.randomUUID()),
                                    new FindInventoryEmplacementsViewResponse()
                                            .name("Internal Emplacement 2")
                                            .type(FindInventoryEmplacementsViewResponse.TypeEnum.INTERNAL)
                                            .id(UUID.randomUUID()),
                                    new FindInventoryEmplacementsViewResponse()
                                            .name("Customer Emplacement 1")
                                            .type(FindInventoryEmplacementsViewResponse.TypeEnum.CUSTOMER)
                                            .id(UUID.randomUUID()),
                                    new FindInventoryEmplacementsViewResponse()
                                            .name("Supplier Emplacement 1")
                                            .type(FindInventoryEmplacementsViewResponse.TypeEnum.SUPPLIER)
                                            .id(UUID.randomUUID()),
                                    new FindInventoryEmplacementsViewResponse()
                                            .name("Transit Emplacement 1")
                                            .type(FindInventoryEmplacementsViewResponse.TypeEnum.TRANSIT)
                                            .id(UUID.randomUUID())
                            )
                    );


            when(inventoryEmplacementService.findInventoryEmplacements(page, pageSize, "name", Direction.ASC)).thenReturn(inventoryEmplacementsPageInfo);
            when(inventoryEmplacementViewMapper.toFindInventoryEmplacementsPageInfoViewResponse(inventoryEmplacementsPageInfo)).thenReturn(inventoryEmplacementsPageInfoViewResponse);

            ArgumentCaptor<PageInfo> pageInfoArgumentCaptor = ArgumentCaptor.forClass(PageInfo.class);
            ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
            ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
            ArgumentCaptor<Direction> directionArgumentCaptor = ArgumentCaptor.forClass(Direction.class);

            //Act
            FindInventoryEmplacementsPageInfoViewResponse result =
                    inventoryEmplacementServiceRestApiAdapter.findInventoryEmplacements(page, pageSize, attribute, "ASC");

            //Then
            assertThat(result)
                    .isNotNull()
                    .isEqualTo(inventoryEmplacementsPageInfoViewResponse);

            verify(inventoryEmplacementService, times(1))
                    .findInventoryEmplacements(integerArgumentCaptor.capture(), integerArgumentCaptor.capture(),
                            stringArgumentCaptor.capture(), directionArgumentCaptor.capture());

            verify(inventoryEmplacementViewMapper, times(1))
                    .toFindInventoryEmplacementsPageInfoViewResponse(pageInfoArgumentCaptor.capture());

            assertThat(integerArgumentCaptor.getAllValues().get(0)).isEqualTo(page);
            assertThat(integerArgumentCaptor.getAllValues().get(1)).isEqualTo(pageSize);
            assertThat(stringArgumentCaptor.getAllValues().getFirst()).isEqualTo(attribute);
            assertThat(directionArgumentCaptor.getAllValues().getFirst()).isEqualTo(Direction.ASC);
            assertThat(pageInfoArgumentCaptor.getValue()).isEqualTo(inventoryEmplacementsPageInfo);
        }
    }


    @Nested
    class SearchInventoryEmplacementsDomainServiceRestApiAdapterTest {

        @Test
        void should_success_when_search_inventory_emplacement_with_existing_keyword() {
            //Given
            int page = 1;
            int pageSize = 2;
            String attribute = "name";
            String keyword = "int";

            PageInfo<InventoryEmplacement> inventoryEmplacementsPageInfo =  new PageInfo<InventoryEmplacement>().with(
                    page, pageSize,
                    List.of(
                            InventoryEmplacement.builder()
                                    .name(Name.of(Text.of("Internal Emplacement 1")))
                                    .id(new InventoryEmplacementId(UUID.randomUUID()))
                                    .type(InventoryEmplacementType.INTERNAL)
                                    .build(),
                            InventoryEmplacement.builder()
                                    .name(Name.of(Text.of("Internal Emplacement 2")))
                                    .id(new InventoryEmplacementId(UUID.randomUUID()))
                                    .type(InventoryEmplacementType.INTERNAL)
                                    .build(),
                            InventoryEmplacement.builder()
                                    .name(Name.of(Text.of("Customer Emplacement 1")))
                                    .id(new InventoryEmplacementId(UUID.randomUUID()))
                                    .type(InventoryEmplacementType.CUSTOMER)
                                    .build(),
                            InventoryEmplacement.builder()
                                    .name(Name.of(Text.of("Supplier Emplacement 1")))
                                    .id(new InventoryEmplacementId(UUID.randomUUID()))
                                    .type(InventoryEmplacementType.SUPPLIER)
                                    .build(),
                            InventoryEmplacement.builder()
                                    .name(Name.of(Text.of("Transit Emplacement 1")))
                                    .id(new InventoryEmplacementId(UUID.randomUUID()))
                                    .type(InventoryEmplacementType.TRANSIT)
                                    .build()
                    )
            );

            SearchInventoryEmplacementsPageInfoViewResponse inventoryEmplacementsPageInfoViewResponse = new SearchInventoryEmplacementsPageInfoViewResponse()
                    .first(true)
                    .last(false)
                    .pageSize(2)
                    .totalElements(5L)
                    .totalPages(1)
                    .elements(
                            List.of(
                                    new SearchInventoryEmplacementsViewResponse()
                                            .name("Internal Emplacement 1")
                                            .type(SearchInventoryEmplacementsViewResponse.TypeEnum.INTERNAL)
                                            .id(UUID.randomUUID()),
                                    new SearchInventoryEmplacementsViewResponse()
                                            .name("Internal Emplacement 2")
                                            .type(SearchInventoryEmplacementsViewResponse.TypeEnum.INTERNAL)
                                            .id(UUID.randomUUID()),
                                    new SearchInventoryEmplacementsViewResponse()
                                            .name("Customer Emplacement 1")
                                            .type(SearchInventoryEmplacementsViewResponse.TypeEnum.CUSTOMER)
                                            .id(UUID.randomUUID()),
                                    new SearchInventoryEmplacementsViewResponse()
                                            .name("Supplier Emplacement 1")
                                            .type(SearchInventoryEmplacementsViewResponse.TypeEnum.SUPPLIER)
                                            .id(UUID.randomUUID()),
                                    new SearchInventoryEmplacementsViewResponse()
                                            .name("Transit Emplacement 1")
                                            .type(SearchInventoryEmplacementsViewResponse.TypeEnum.TRANSIT)
                                            .id(UUID.randomUUID())
                            )
                    );


            when(inventoryEmplacementService.searchInventoryEmplacements(page, pageSize, "name", Direction.ASC, Keyword.of(Text.of(keyword)))).thenReturn(inventoryEmplacementsPageInfo);
            when(inventoryEmplacementViewMapper.toSearchInventoryEmplacementsPageInfoViewResponse(inventoryEmplacementsPageInfo)).thenReturn(inventoryEmplacementsPageInfoViewResponse);

            ArgumentCaptor<PageInfo> pageInfoArgumentCaptor = ArgumentCaptor.forClass(PageInfo.class);
            ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
            ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
            ArgumentCaptor<Direction> directionArgumentCaptor = ArgumentCaptor.forClass(Direction.class);
            ArgumentCaptor<Keyword> keywordArgumentCaptor = ArgumentCaptor.forClass(Keyword.class);

            //Act
            SearchInventoryEmplacementsPageInfoViewResponse result =
                    inventoryEmplacementServiceRestApiAdapter.searchInventoryEmplacements(page, pageSize, attribute, "ASC", keyword);

            //Then
            assertThat(result)
                    .isNotNull()
                    .isEqualTo(inventoryEmplacementsPageInfoViewResponse);

            verify(inventoryEmplacementService, times(1))
                    .searchInventoryEmplacements(integerArgumentCaptor.capture(), integerArgumentCaptor.capture(),
                            stringArgumentCaptor.capture(), directionArgumentCaptor.capture(), keywordArgumentCaptor.capture());

            verify(inventoryEmplacementViewMapper, times(1))
                    .toSearchInventoryEmplacementsPageInfoViewResponse(pageInfoArgumentCaptor.capture());

            assertThat(integerArgumentCaptor.getAllValues().get(0)).isEqualTo(page);
            assertThat(integerArgumentCaptor.getAllValues().get(1)).isEqualTo(pageSize);
            assertThat(stringArgumentCaptor.getAllValues().getFirst()).isEqualTo(attribute);
            assertThat(directionArgumentCaptor.getAllValues().getFirst()).isEqualTo(Direction.ASC);
            assertThat(keywordArgumentCaptor.getValue().getText().getValue()).isEqualTo(keyword);
            assertThat(pageInfoArgumentCaptor.getValue()).isEqualTo(inventoryEmplacementsPageInfo);
        }
    }


}
