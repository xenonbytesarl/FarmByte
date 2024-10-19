package cm.xenonbyte.farmbyte.inventory.adapter.rest.api;

import cm.xenonbyte.farmbyte.common.adapter.api.messages.MessageUtil;
import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.uom.view.CreateInventoryEmplacementViewRequest;
import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.uom.view.CreateInventoryEmplacementViewResponse;
import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.uom.view.FindInventoryEmplacementByIdViewResponse;
import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.uom.view.FindInventoryEmplacementsPageInfoViewResponse;
import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.uom.view.FindInventoryEmplacementsViewResponse;
import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.uom.view.SearchInventoryEmplacementsPageInfoViewResponse;
import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.uom.view.SearchInventoryEmplacementsViewResponse;
import cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacementNameConflictException;
import cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacementNotFoundException;
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

import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Stream;

import static cm.xenonbyte.farmbyte.common.adapter.api.constant.CommonAdapterRestApi.ACCEPT_LANGUAGE;
import static cm.xenonbyte.farmbyte.common.adapter.api.constant.CommonAdapterRestApi.EN_LOCALE;
import static cm.xenonbyte.farmbyte.inventory.adapter.rest.api.InventoryEmplacementRestApi.INVENTORY_EMPLACEMENTS_FIND_SUCCESSFULLY;
import static cm.xenonbyte.farmbyte.inventory.adapter.rest.api.InventoryEmplacementRestApi.INVENTORY_EMPLACEMENT_CREATED_SUCCESSFULLY;
import static cm.xenonbyte.farmbyte.inventory.adapter.rest.api.InventoryEmplacementRestApi.INVENTORY_EMPLACEMENT_FIND_SUCCESSFULLY;
import static cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacementNameConflictException.INVENTORY_EMPLACEMENT_NAME_CONFLICT;
import static cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacementNotFoundException.INVENTORY_EMPLACEMENT_ID_NOT_FOUND;
import static cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacementParentIdNotFoundException.INVENTORY_EMPLACEMENT_PARENT_ID_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Nested
    class FindInventoryEmplacementByIdRestApiTest {
        @Test
        void should_success_when_find_uom_by_existing_uom_id() throws Exception {
            //Given
            UUID inventoryEmplacementUUID = UUID.fromString("0192a6b0-fd7e-753a-81bf-98a2a1ce9ab5");
            FindInventoryEmplacementByIdViewResponse findInventoryEmplacementByIdViewResponse =
                    new FindInventoryEmplacementByIdViewResponse()
                            .id(inventoryEmplacementUUID)
                            .name("Internal Inventory Emplacement")
                            .type(FindInventoryEmplacementByIdViewResponse.TypeEnum.INTERNAL)
                            .active(true);

            when(inventoryEmplacementDomainRestApiAdapter.findInventoryEmplacementById(inventoryEmplacementUUID))
                    .thenReturn(findInventoryEmplacementByIdViewResponse);
            ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);

            //Act
            mockMvc.perform(get(String.format(INVENTORY_EMPLACEMENT_PATH_URI + "/%s", inventoryEmplacementUUID))
                            .accept(APPLICATION_JSON)
                            .header(ACCEPT_LANGUAGE, EN_LOCALE)
                            .contentType(APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isNotEmpty())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.status").value("OK"))
                    .andExpect(jsonPath("$.message").value(MessageUtil.getMessage(INVENTORY_EMPLACEMENT_FIND_SUCCESSFULLY, Locale.forLanguageTag(EN_LOCALE), "")))
                    .andExpect(jsonPath("$.data").isMap())
                    .andExpect(jsonPath("$.data.content").isNotEmpty())
                    .andExpect(jsonPath("$.data.content.id").value(findInventoryEmplacementByIdViewResponse.getId().toString()))
                    .andExpect(jsonPath("$.data.content.name").value(findInventoryEmplacementByIdViewResponse.getName()))
                    .andExpect(jsonPath("$.data.content.type").value(findInventoryEmplacementByIdViewResponse.getType().toString()))
                    .andExpect(jsonPath("$.data.content.active").value(findInventoryEmplacementByIdViewResponse.getActive()));

            //Then
            verify(inventoryEmplacementDomainRestApiAdapter, times(1))
                    .findInventoryEmplacementById(uuidArgumentCaptor.capture());
            assertThat(uuidArgumentCaptor.getValue()).isEqualTo(inventoryEmplacementUUID);
        }

        @Test
        void should_fail_when_find_uom_by_existing_uom_id() throws Exception {
            //Given
            UUID inventoryEmplacementUUID = UUID.fromString("0192a71d-fcff-76e5-89c6-03197f99c981");

            when(inventoryEmplacementDomainRestApiAdapter.findInventoryEmplacementById(inventoryEmplacementUUID))
                    .thenThrow(InventoryEmplacementNotFoundException.class.getConstructor(Object[].class)
                            .newInstance(new Object[]{new String[]{inventoryEmplacementUUID.toString()}}));
            ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);

            //Act
            mockMvc.perform(get(String.format(INVENTORY_EMPLACEMENT_PATH_URI + "/%s", inventoryEmplacementUUID))
                            .accept(APPLICATION_JSON)
                            .header(ACCEPT_LANGUAGE, EN_LOCALE)
                            .contentType(APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$").isNotEmpty())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.code").value(404))
                    .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                    .andExpect(jsonPath("$.reason").value(MessageUtil.getMessage(INVENTORY_EMPLACEMENT_ID_NOT_FOUND, Locale.forLanguageTag(EN_LOCALE), inventoryEmplacementUUID.toString())));

            //Then
            verify(inventoryEmplacementDomainRestApiAdapter, times(1))
                    .findInventoryEmplacementById(uuidArgumentCaptor.capture());
            assertThat(uuidArgumentCaptor.getValue()).isEqualTo(inventoryEmplacementUUID);
        }
    }

    @Nested
    class FindInventoryEmplacementsRestApiTest {
        @Test
        void should_success_when_find_when_find_inventory_emplacements() throws Exception {
            //Given
            int page = 0;
            int size = 2;
            String attribute = "name";
            String direction = "ASC";
            FindInventoryEmplacementsPageInfoViewResponse inventoryEmplacementsPageInfoViewResponse =
                    new FindInventoryEmplacementsPageInfoViewResponse()
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

            when(inventoryEmplacementDomainRestApiAdapter.findInventoryEmplacements(page, size, attribute, direction))
                    .thenReturn(inventoryEmplacementsPageInfoViewResponse);
            ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
            ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

            //Act + Then
            mockMvc.perform(get(INVENTORY_EMPLACEMENT_PATH_URI)
                            .accept(APPLICATION_JSON)
                            .header(ACCEPT_LANGUAGE, EN_LOCALE)
                            .param("page", String.valueOf(page))
                            .param("size", String.valueOf(size))
                            .param("attribute", attribute)
                            .param("direction", direction)
                            .contentType(APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isNotEmpty())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.status").value("OK"))
                    .andExpect(jsonPath("$.message").value(MessageUtil.getMessage(INVENTORY_EMPLACEMENTS_FIND_SUCCESSFULLY, Locale.forLanguageTag(EN_LOCALE), "")))
                    .andExpect(jsonPath("$.data").isNotEmpty())
                    .andExpect(jsonPath("$.data.content.elements").isArray());

            //Then
            verify(inventoryEmplacementDomainRestApiAdapter, times(1))
                    .findInventoryEmplacements(
                            integerArgumentCaptor.capture(), integerArgumentCaptor.capture(),
                                stringArgumentCaptor.capture(), stringArgumentCaptor.capture());
            assertThat(integerArgumentCaptor.getAllValues().get(0)).isEqualTo(page);
            assertThat(integerArgumentCaptor.getAllValues().get(1)).isEqualTo(size);
            assertThat(stringArgumentCaptor.getAllValues().get(0)).isEqualTo(attribute);
            assertThat(stringArgumentCaptor.getAllValues().get(1)).isEqualTo(direction);

        }
    }

    @Nested
    class SearchInventoryEmplacementsRestApiTest {
        @Test
        void should_success_when_search_when_find_inventory_emplacements() throws Exception {
            //Given
            int page = 0;
            int size = 2;
            String attribute = "name";
            String direction = "ASC";
            String keyword = "int";

            SearchInventoryEmplacementsPageInfoViewResponse inventoryEmplacementsPageInfoViewResponse =
                    new SearchInventoryEmplacementsPageInfoViewResponse()
                    .first(true)
                    .last(false)
                    .pageSize(2)
                    .totalElements(5L)
                    .totalPages(3)
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

            when(inventoryEmplacementDomainRestApiAdapter.searchInventoryEmplacements(page, size, attribute, direction, keyword))
                    .thenReturn(inventoryEmplacementsPageInfoViewResponse);
            ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
            ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

            //Act + Then
            mockMvc.perform(get(INVENTORY_EMPLACEMENT_PATH_URI + "/search")
                            .accept(APPLICATION_JSON)
                            .header(ACCEPT_LANGUAGE, EN_LOCALE)
                            .param("page", String.valueOf(page))
                            .param("size", String.valueOf(size))
                            .param("attribute", attribute)
                            .param("direction", direction)
                            .param("keyword", keyword)
                            .contentType(APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isNotEmpty())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.status").value("OK"))
                    .andExpect(jsonPath("$.message").value(MessageUtil.getMessage(INVENTORY_EMPLACEMENTS_FIND_SUCCESSFULLY, Locale.forLanguageTag(EN_LOCALE), "")))
                    .andExpect(jsonPath("$.data").isNotEmpty())
                    .andExpect(jsonPath("$.data.content.elements").isArray());

            //Then
            verify(inventoryEmplacementDomainRestApiAdapter, times(1))
                    .searchInventoryEmplacements(
                            integerArgumentCaptor.capture(), integerArgumentCaptor.capture(),
                            stringArgumentCaptor.capture(), stringArgumentCaptor.capture(), stringArgumentCaptor.capture());
            assertThat(integerArgumentCaptor.getAllValues().get(0)).isEqualTo(page);
            assertThat(integerArgumentCaptor.getAllValues().get(1)).isEqualTo(size);
            assertThat(stringArgumentCaptor.getAllValues().get(0)).isEqualTo(attribute);
            assertThat(stringArgumentCaptor.getAllValues().get(1)).isEqualTo(direction);
            assertThat(stringArgumentCaptor.getAllValues().get(2)).isEqualTo(keyword);

        }
    }
}
