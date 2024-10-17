package cm.xenonbyte.farmbyte.inventory.adapter.rest.api;

import cm.xenonbyte.farmbyte.common.adapter.api.messages.MessageUtil;
import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.uom.view.CreateInventoryEmplacementViewRequest;
import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.uom.view.CreateInventoryEmplacementViewResponse;
import cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacementNameConflictException;
import cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacementParentIdNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Locale;
import java.util.UUID;
import java.util.stream.Stream;

import static cm.xenonbyte.farmbyte.common.adapter.api.constant.CommonAdapterRestApi.ACCEPT_LANGUAGE;
import static cm.xenonbyte.farmbyte.common.adapter.api.constant.CommonAdapterRestApi.EN_LOCALE;
import static cm.xenonbyte.farmbyte.inventory.adapter.rest.api.InventoryEmplacementRestApi.INVENTORY_EMPLACEMENT_CREATED_SUCCESSFULLY;
import static cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacementNameConflictException.INVENTORY_EMPLACEMENT_NAME_CONFLICT;
import static cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacementParentIdNotFoundException.INVENTORY_EMPLACEMENT_PARENT_ID_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author bamk
 * @version 1.0
 * @since 17/10/2024
 */
@ActiveProfiles("test")
@WebMvcTest(InventoryEmplacementRestApi.class)
@ComponentScan(basePackages = "cm.xenonbyte.farmbyte.inventory.adapter.rest.api")
@ContextConfiguration(classes = {InventoryEmplacementDomainServiceRestApiAdapter.class})
public final class InventoryEmplacementRestApiTest extends RestApiBeanConfigTest {

    public static final String INVENTORY_EMPLACEMENT_PATH_URI = "/api/v1/inventory/inventory-emplacements";
    @Autowired
    private MockMvc mockMvc;

    @Nested
    class CreateInventoryEmplacementRestApiTest {

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
        ) throws Exception {
            //Given
            CreateInventoryEmplacementViewRequest createInventoryEmplacementViewRequest = new CreateInventoryEmplacementViewRequest()
                    .name(name)
                    .type(type)
                    .parentId(parentId);

            CreateInventoryEmplacementViewResponse createInventoryEmplacementViewResponse = new CreateInventoryEmplacementViewResponse()
                    .id(inventoryEmplacementId)
                    .name(name)
                    .type(CreateInventoryEmplacementViewResponse.TypeEnum.valueOf(type.name()))
                    .parentId(parentId);

            when(inventoryEmplacementDomainRestApiAdapter.createInventoryEmplacement(createInventoryEmplacementViewRequest))
                    .thenReturn(createInventoryEmplacementViewResponse);

            ArgumentCaptor<CreateInventoryEmplacementViewRequest> createInventoryEmplacementViewRequestArgumentCaptor =
                    ArgumentCaptor.forClass(CreateInventoryEmplacementViewRequest.class);

            //Act + Then
            mockMvc.perform(post(INVENTORY_EMPLACEMENT_PATH_URI)
                            .accept(APPLICATION_JSON)
                            .header(ACCEPT_LANGUAGE, EN_LOCALE)
                            .contentType(APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(createInventoryEmplacementViewRequest)))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$").isNotEmpty())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.code").value(201))
                    .andExpect(jsonPath("$.message").value(MessageUtil.getMessage(INVENTORY_EMPLACEMENT_CREATED_SUCCESSFULLY, Locale.forLanguageTag(EN_LOCALE), "")))
                    .andExpect(jsonPath("$.data").isNotEmpty())
                    .andExpect(jsonPath("$.data.content.name").value(createInventoryEmplacementViewResponse.getName()))
                    .andExpect(jsonPath("$.data.content.id").value(createInventoryEmplacementViewResponse.getId().toString()))
                    .andExpect(jsonPath("$.data.content.active").value(createInventoryEmplacementViewResponse.getActive()));

            verify(inventoryEmplacementDomainRestApiAdapter, times(1)).createInventoryEmplacement(
                    createInventoryEmplacementViewRequestArgumentCaptor.capture()
            );

            assertThat(createInventoryEmplacementViewRequestArgumentCaptor.getValue()).isEqualTo(createInventoryEmplacementViewRequest);
        }

        @Test
        void should_fail_when_create_inventory_emplacement_with_existing_name() throws Exception {
            //Given
            String name = "Root Emplacement view";
            CreateInventoryEmplacementViewRequest.TypeEnum type = CreateInventoryEmplacementViewRequest.TypeEnum.VIEW;

            CreateInventoryEmplacementViewRequest createInventoryEmplacementViewRequest = new CreateInventoryEmplacementViewRequest()
                    .name(name)
                    .type(type)
                   .parentId(null);
            when(inventoryEmplacementDomainRestApiAdapter.createInventoryEmplacement(createInventoryEmplacementViewRequest))
                    .thenThrow(InventoryEmplacementNameConflictException.class.getConstructor(Object[].class).newInstance(new Object[]{new String[]{name}}));

            ArgumentCaptor<CreateInventoryEmplacementViewRequest> createInventoryEmplacementViewRequestArgumentCaptor =
                    ArgumentCaptor.forClass(CreateInventoryEmplacementViewRequest.class);

            //Act
            mockMvc.perform(post(INVENTORY_EMPLACEMENT_PATH_URI)
                            .accept(APPLICATION_JSON)
                            .header(ACCEPT_LANGUAGE, EN_LOCALE)
                            .contentType(APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(createInventoryEmplacementViewRequest)))
                    .andDo(print())
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$").isNotEmpty())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.code").value(409))
                    .andExpect(jsonPath("$.reason").value(MessageUtil.getMessage(INVENTORY_EMPLACEMENT_NAME_CONFLICT, Locale.forLanguageTag(EN_LOCALE), name)));

            verify(inventoryEmplacementDomainRestApiAdapter, times(1)).createInventoryEmplacement(
                    createInventoryEmplacementViewRequestArgumentCaptor.capture()
            );

            assertThat(createInventoryEmplacementViewRequestArgumentCaptor.getValue()).isEqualTo(createInventoryEmplacementViewRequest);

        }

        @Test
        void should_fail_when_create_inventory_emplacement_with_non_existing_parent_id() throws Exception {
            //Given
            String name = "Root Emplacement view";
            CreateInventoryEmplacementViewRequest.TypeEnum type = CreateInventoryEmplacementViewRequest.TypeEnum.VIEW;

            UUID parentId = UUID.fromString("01929c99-7845-7571-bca3-11021268bdf5");
            CreateInventoryEmplacementViewRequest createInventoryEmplacementViewRequest = new CreateInventoryEmplacementViewRequest()
                    .name(name)
                    .type(type)
                    .parentId(parentId);
            when(inventoryEmplacementDomainRestApiAdapter.createInventoryEmplacement(createInventoryEmplacementViewRequest))
                    .thenThrow(InventoryEmplacementParentIdNotFoundException.class.getConstructor(Object[].class).newInstance(new Object[]{new String[]{parentId.toString()}}));

            ArgumentCaptor<CreateInventoryEmplacementViewRequest> createInventoryEmplacementViewRequestArgumentCaptor =
                    ArgumentCaptor.forClass(CreateInventoryEmplacementViewRequest.class);

            //Act
            mockMvc.perform(post(INVENTORY_EMPLACEMENT_PATH_URI)
                            .accept(APPLICATION_JSON)
                            .header(ACCEPT_LANGUAGE, EN_LOCALE)
                            .contentType(APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(createInventoryEmplacementViewRequest)))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$").isNotEmpty())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.code").value(404))
                    .andExpect(jsonPath("$.reason").value(MessageUtil.getMessage(INVENTORY_EMPLACEMENT_PARENT_ID_NOT_FOUND, Locale.forLanguageTag(EN_LOCALE), parentId.toString())));

            verify(inventoryEmplacementDomainRestApiAdapter, times(1)).createInventoryEmplacement(
                    createInventoryEmplacementViewRequestArgumentCaptor.capture()
            );

            assertThat(createInventoryEmplacementViewRequestArgumentCaptor.getValue()).isEqualTo(createInventoryEmplacementViewRequest);

        }
    }
}
