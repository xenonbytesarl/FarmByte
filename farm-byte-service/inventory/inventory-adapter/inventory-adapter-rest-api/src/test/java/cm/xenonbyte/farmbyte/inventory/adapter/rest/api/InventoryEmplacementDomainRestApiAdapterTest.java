package cm.xenonbyte.farmbyte.inventory.adapter.rest.api;

import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.Text;
import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.uom.view.CreateInventoryEmplacementViewRequest;
import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.uom.view.CreateInventoryEmplacementViewResponse;
import cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacement;
import cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacementId;
import cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacementNameConflictException;
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
}
