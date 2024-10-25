package cm.xenonbyte.farmbyte.stock.adapter.rest.api;

import cm.xenonbyte.farmbyte.common.adapter.api.messages.MessageUtil;
import cm.xenonbyte.farmbyte.stock.adapter.rest.api.generated.stocklocation.view.CreateStockLocationViewRequest;
import cm.xenonbyte.farmbyte.stock.adapter.rest.api.generated.stocklocation.view.CreateStockLocationViewResponse;
import cm.xenonbyte.farmbyte.stock.adapter.rest.api.generated.stocklocation.view.FindStockLocationByIdViewResponse;
import cm.xenonbyte.farmbyte.stock.adapter.rest.api.generated.stocklocation.view.FindStockLocationsPageInfoViewResponse;
import cm.xenonbyte.farmbyte.stock.adapter.rest.api.generated.stocklocation.view.FindStockLocationsViewResponse;
import cm.xenonbyte.farmbyte.stock.adapter.rest.api.generated.stocklocation.view.SearchStockLocationsPageInfoViewResponse;
import cm.xenonbyte.farmbyte.stock.adapter.rest.api.generated.stocklocation.view.SearchStockLocationsViewResponse;
import cm.xenonbyte.farmbyte.stock.adapter.rest.api.generated.stocklocation.view.UpdateStockLocationViewRequest;
import cm.xenonbyte.farmbyte.stock.adapter.rest.api.generated.stocklocation.view.UpdateStockLocationViewResponse;
import cm.xenonbyte.farmbyte.stock.domain.core.stocklocation.StockLocationNameConflictException;
import cm.xenonbyte.farmbyte.stock.domain.core.stocklocation.StockLocationNotFoundException;
import cm.xenonbyte.farmbyte.stock.domain.core.stocklocation.StockLocationParentIdNotFoundException;
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
import static cm.xenonbyte.farmbyte.stock.adapter.rest.api.StockLocationRestApi.STOCK_LOCATIONS_FIND_SUCCESSFULLY;
import static cm.xenonbyte.farmbyte.stock.adapter.rest.api.StockLocationRestApi.STOCK_LOCATION_CREATED_SUCCESSFULLY;
import static cm.xenonbyte.farmbyte.stock.adapter.rest.api.StockLocationRestApi.STOCK_LOCATION_FIND_SUCCESSFULLY;
import static cm.xenonbyte.farmbyte.stock.adapter.rest.api.StockLocationRestApi.STOCK_LOCATION_UPDATED_SUCCESSFULLY;
import static cm.xenonbyte.farmbyte.stock.domain.core.constant.StockDomainConstant.STOCK_LOCATION_ID_NOT_FOUND;
import static cm.xenonbyte.farmbyte.stock.domain.core.constant.StockDomainConstant.STOCK_LOCATION_NAME_CONFLICT;
import static cm.xenonbyte.farmbyte.stock.domain.core.constant.StockDomainConstant.STOCK_LOCATION_PARENT_ID_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author bamk
 * @version 1.0
 * @since 17/10/2024
 */
@ActiveProfiles("test")
@WebMvcTest(StockLocationRestApi.class)
@ComponentScan(basePackages = "cm.xenonbyte.farmbyte.stock.adapter.rest.api")
@ContextConfiguration(classes = {StockLocationDomainServiceRestApiAdapter.class})
public final class StockLocationRestApiTest extends RestApiBeanConfigTest {

    public static final String INVENTORY_EMPLACEMENT_PATH_URI = "/api/v1/stock/stock-locations";
    @Autowired
    private MockMvc mockMvc;

    @Nested
    class CreateStockLocationRestApiTest {

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
        ) throws Exception {
            //Given
            CreateStockLocationViewRequest createStockLocationViewRequest = new CreateStockLocationViewRequest()
                    .name(name)
                    .type(type)
                    .parentId(parentId);

            CreateStockLocationViewResponse createStockLocationViewResponse = new CreateStockLocationViewResponse()
                    .id(stockLocationId)
                    .name(name)
                    .type(CreateStockLocationViewResponse.TypeEnum.valueOf(type.name()))
                    .parentId(parentId);

            when(stockLocationDomainRestApiAdapter.createStockLocation(createStockLocationViewRequest))
                    .thenReturn(createStockLocationViewResponse);

            ArgumentCaptor<CreateStockLocationViewRequest> createStockLocationViewRequestArgumentCaptor =
                    ArgumentCaptor.forClass(CreateStockLocationViewRequest.class);

            //Act + Then
            mockMvc.perform(post(INVENTORY_EMPLACEMENT_PATH_URI)
                            .accept(APPLICATION_JSON)
                            .header(ACCEPT_LANGUAGE, EN_LOCALE)
                            .contentType(APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(createStockLocationViewRequest)))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$").isNotEmpty())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.code").value(201))
                    .andExpect(jsonPath("$.message").value(MessageUtil.getMessage(STOCK_LOCATION_CREATED_SUCCESSFULLY, Locale.forLanguageTag(EN_LOCALE), "")))
                    .andExpect(jsonPath("$.data").isNotEmpty())
                    .andExpect(jsonPath("$.data.content.name").value(createStockLocationViewResponse.getName()))
                    .andExpect(jsonPath("$.data.content.id").value(createStockLocationViewResponse.getId().toString()))
                    .andExpect(jsonPath("$.data.content.active").value(createStockLocationViewResponse.getActive()));

            verify(stockLocationDomainRestApiAdapter, times(1)).createStockLocation(
                    createStockLocationViewRequestArgumentCaptor.capture()
            );

            assertThat(createStockLocationViewRequestArgumentCaptor.getValue()).isEqualTo(createStockLocationViewRequest);
        }

        @Test
        void should_fail_when_create_inventory_emplacement_with_existing_name() throws Exception {
            //Given
            String name = "Root Location view";
            CreateStockLocationViewRequest.TypeEnum type = CreateStockLocationViewRequest.TypeEnum.VIEW;

            CreateStockLocationViewRequest createStockLocationViewRequest = new CreateStockLocationViewRequest()
                    .name(name)
                    .type(type)
                   .parentId(null);
            when(stockLocationDomainRestApiAdapter.createStockLocation(createStockLocationViewRequest))
                    .thenThrow(StockLocationNameConflictException.class.getConstructor(Object[].class).newInstance(new Object[]{new String[]{name}}));

            ArgumentCaptor<CreateStockLocationViewRequest> createStockLocationViewRequestArgumentCaptor =
                    ArgumentCaptor.forClass(CreateStockLocationViewRequest.class);

            //Act
            mockMvc.perform(post(INVENTORY_EMPLACEMENT_PATH_URI)
                            .accept(APPLICATION_JSON)
                            .header(ACCEPT_LANGUAGE, EN_LOCALE)
                            .contentType(APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(createStockLocationViewRequest)))
                    .andDo(print())
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$").isNotEmpty())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.code").value(409))
                    .andExpect(jsonPath("$.reason").value(MessageUtil.getMessage(STOCK_LOCATION_NAME_CONFLICT, Locale.forLanguageTag(EN_LOCALE), name)));

            verify(stockLocationDomainRestApiAdapter, times(1)).createStockLocation(
                    createStockLocationViewRequestArgumentCaptor.capture()
            );

            assertThat(createStockLocationViewRequestArgumentCaptor.getValue()).isEqualTo(createStockLocationViewRequest);

        }

        @Test
        void should_fail_when_create_inventory_emplacement_with_non_existing_parent_id() throws Exception {
            //Given
            String name = "Root Location view";
            CreateStockLocationViewRequest.TypeEnum type = CreateStockLocationViewRequest.TypeEnum.VIEW;

            UUID parentId = UUID.fromString("01929c99-7845-7571-bca3-11021268bdf5");
            CreateStockLocationViewRequest createStockLocationViewRequest = new CreateStockLocationViewRequest()
                    .name(name)
                    .type(type)
                    .parentId(parentId);
            when(stockLocationDomainRestApiAdapter.createStockLocation(createStockLocationViewRequest))
                    .thenThrow(StockLocationParentIdNotFoundException.class.getConstructor(Object[].class).newInstance(new Object[]{new String[]{parentId.toString()}}));

            ArgumentCaptor<CreateStockLocationViewRequest> createStockLocationViewRequestArgumentCaptor =
                    ArgumentCaptor.forClass(CreateStockLocationViewRequest.class);

            //Act
            mockMvc.perform(post(INVENTORY_EMPLACEMENT_PATH_URI)
                            .accept(APPLICATION_JSON)
                            .header(ACCEPT_LANGUAGE, EN_LOCALE)
                            .contentType(APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(createStockLocationViewRequest)))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$").isNotEmpty())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.code").value(404))
                    .andExpect(jsonPath("$.reason").value(MessageUtil.getMessage(STOCK_LOCATION_PARENT_ID_NOT_FOUND, Locale.forLanguageTag(EN_LOCALE), parentId.toString())));

            verify(stockLocationDomainRestApiAdapter, times(1)).createStockLocation(
                    createStockLocationViewRequestArgumentCaptor.capture()
            );

            assertThat(createStockLocationViewRequestArgumentCaptor.getValue()).isEqualTo(createStockLocationViewRequest);

        }
    }

    @Nested
    class FindStockLocationByIdRestApiTest {
        @Test
        void should_success_when_find_uom_by_existing_uom_id() throws Exception {
            //Given
            UUID stockLocationUUID = UUID.fromString("0192a6b0-fd7e-753a-81bf-98a2a1ce9ab5");
            FindStockLocationByIdViewResponse findStockLocationByIdViewResponse =
                    new FindStockLocationByIdViewResponse()
                            .id(stockLocationUUID)
                            .name("Internal Stock Location")
                            .type(FindStockLocationByIdViewResponse.TypeEnum.INTERNAL)
                            .active(true);

            when(stockLocationDomainRestApiAdapter.findStockLocationById(stockLocationUUID))
                    .thenReturn(findStockLocationByIdViewResponse);
            ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);

            //Act
            mockMvc.perform(get(String.format(INVENTORY_EMPLACEMENT_PATH_URI + "/%s", stockLocationUUID))
                            .accept(APPLICATION_JSON)
                            .header(ACCEPT_LANGUAGE, EN_LOCALE)
                            .contentType(APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isNotEmpty())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.status").value("OK"))
                    .andExpect(jsonPath("$.message").value(MessageUtil.getMessage(STOCK_LOCATION_FIND_SUCCESSFULLY, Locale.forLanguageTag(EN_LOCALE), "")))
                    .andExpect(jsonPath("$.data").isMap())
                    .andExpect(jsonPath("$.data.content").isNotEmpty())
                    .andExpect(jsonPath("$.data.content.id").value(findStockLocationByIdViewResponse.getId().toString()))
                    .andExpect(jsonPath("$.data.content.name").value(findStockLocationByIdViewResponse.getName()))
                    .andExpect(jsonPath("$.data.content.type").value(findStockLocationByIdViewResponse.getType().toString()))
                    .andExpect(jsonPath("$.data.content.active").value(findStockLocationByIdViewResponse.getActive()));

            //Then
            verify(stockLocationDomainRestApiAdapter, times(1))
                    .findStockLocationById(uuidArgumentCaptor.capture());
            assertThat(uuidArgumentCaptor.getValue()).isEqualTo(stockLocationUUID);
        }

        @Test
        void should_fail_when_find_uom_by_existing_uom_id() throws Exception {
            //Given
            UUID stockLocationUUID = UUID.fromString("0192a71d-fcff-76e5-89c6-03197f99c981");

            when(stockLocationDomainRestApiAdapter.findStockLocationById(stockLocationUUID))
                    .thenThrow(StockLocationNotFoundException.class.getConstructor(Object[].class)
                            .newInstance(new Object[]{new String[]{stockLocationUUID.toString()}}));
            ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);

            //Act
            mockMvc.perform(get(String.format(INVENTORY_EMPLACEMENT_PATH_URI + "/%s", stockLocationUUID))
                            .accept(APPLICATION_JSON)
                            .header(ACCEPT_LANGUAGE, EN_LOCALE)
                            .contentType(APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$").isNotEmpty())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.code").value(404))
                    .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                    .andExpect(jsonPath("$.reason").value(MessageUtil.getMessage(STOCK_LOCATION_ID_NOT_FOUND, Locale.forLanguageTag(EN_LOCALE), stockLocationUUID.toString())));

            //Then
            verify(stockLocationDomainRestApiAdapter, times(1))
                    .findStockLocationById(uuidArgumentCaptor.capture());
            assertThat(uuidArgumentCaptor.getValue()).isEqualTo(stockLocationUUID);
        }
    }

    @Nested
    class FindStockLocationsRestApiTest {
        @Test
        void should_success_when_find_when_find_inventory_emplacements() throws Exception {
            //Given
            int page = 0;
            int size = 2;
            String attribute = "name";
            String direction = "ASC";
            FindStockLocationsPageInfoViewResponse stockLocationsPageInfoViewResponse =
                    new FindStockLocationsPageInfoViewResponse()
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

            when(stockLocationDomainRestApiAdapter.findStockLocations(page, size, attribute, direction))
                    .thenReturn(stockLocationsPageInfoViewResponse);
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
                    .andExpect(jsonPath("$.message").value(MessageUtil.getMessage(STOCK_LOCATIONS_FIND_SUCCESSFULLY, Locale.forLanguageTag(EN_LOCALE), "")))
                    .andExpect(jsonPath("$.data").isNotEmpty())
                    .andExpect(jsonPath("$.data.content.elements").isArray());

            //Then
            verify(stockLocationDomainRestApiAdapter, times(1))
                    .findStockLocations(
                            integerArgumentCaptor.capture(), integerArgumentCaptor.capture(),
                                stringArgumentCaptor.capture(), stringArgumentCaptor.capture());
            assertThat(integerArgumentCaptor.getAllValues().get(0)).isEqualTo(page);
            assertThat(integerArgumentCaptor.getAllValues().get(1)).isEqualTo(size);
            assertThat(stringArgumentCaptor.getAllValues().get(0)).isEqualTo(attribute);
            assertThat(stringArgumentCaptor.getAllValues().get(1)).isEqualTo(direction);

        }
    }

    @Nested
    class SearchStockLocationsRestApiTest {
        @Test
        void should_success_when_search_when_find_inventory_emplacements() throws Exception {
            //Given
            int page = 0;
            int size = 2;
            String attribute = "name";
            String direction = "ASC";
            String keyword = "int";

            SearchStockLocationsPageInfoViewResponse stockLocationsPageInfoViewResponse =
                    new SearchStockLocationsPageInfoViewResponse()
                    .first(true)
                    .last(false)
                    .pageSize(2)
                    .totalElements(5L)
                    .totalPages(3)
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

            when(stockLocationDomainRestApiAdapter.searchStockLocations(page, size, attribute, direction, keyword))
                    .thenReturn(stockLocationsPageInfoViewResponse);
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
                    .andExpect(jsonPath("$.message").value(MessageUtil.getMessage(STOCK_LOCATIONS_FIND_SUCCESSFULLY, Locale.forLanguageTag(EN_LOCALE), "")))
                    .andExpect(jsonPath("$.data").isNotEmpty())
                    .andExpect(jsonPath("$.data.content.elements").isArray());

            //Then
            verify(stockLocationDomainRestApiAdapter, times(1))
                    .searchStockLocations(
                            integerArgumentCaptor.capture(), integerArgumentCaptor.capture(),
                            stringArgumentCaptor.capture(), stringArgumentCaptor.capture(), stringArgumentCaptor.capture());
            assertThat(integerArgumentCaptor.getAllValues().get(0)).isEqualTo(page);
            assertThat(integerArgumentCaptor.getAllValues().get(1)).isEqualTo(size);
            assertThat(stringArgumentCaptor.getAllValues().get(0)).isEqualTo(attribute);
            assertThat(stringArgumentCaptor.getAllValues().get(1)).isEqualTo(direction);
            assertThat(stringArgumentCaptor.getAllValues().get(2)).isEqualTo(keyword);

        }
    }

    @Test
    void should_update_stock_location_by_id() throws Exception {
        //Given
        UUID stockLocationUUID = UUID.fromString("0192c540-3469-7da3-89e6-c8e65c701697");
        String name = "Internal Location Update";

        UpdateStockLocationViewRequest updateStockLocationViewRequest = new UpdateStockLocationViewRequest()
                .id(stockLocationUUID)
                .name(name)
                .type(UpdateStockLocationViewRequest.TypeEnum.INTERNAL)
                .active(true);

        UpdateStockLocationViewResponse updateStockLocationViewResponse = new UpdateStockLocationViewResponse()
                .id(stockLocationUUID)
                .name(name)
                .type(UpdateStockLocationViewResponse.TypeEnum.INTERNAL)
                .active(true);

        when(stockLocationDomainRestApiAdapter.updateStockLocation(stockLocationUUID, updateStockLocationViewRequest))
                .thenReturn(updateStockLocationViewResponse);

        ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);

        ArgumentCaptor<UpdateStockLocationViewRequest> updateStockLocationViewRequestArgumentCaptor =
                ArgumentCaptor.forClass(UpdateStockLocationViewRequest.class);

        //Act + Then
        mockMvc.perform(put(INVENTORY_EMPLACEMENT_PATH_URI + "/" + stockLocationUUID)
                        .accept(APPLICATION_JSON)
                        .header(ACCEPT_LANGUAGE, EN_LOCALE)
                        .contentType(APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateStockLocationViewRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value(MessageUtil.getMessage(STOCK_LOCATION_UPDATED_SUCCESSFULLY, Locale.forLanguageTag(EN_LOCALE), "")))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.content.name").value(updateStockLocationViewResponse.getName()))
                .andExpect(jsonPath("$.data.content.id").value(updateStockLocationViewResponse.getId().toString()))
                .andExpect(jsonPath("$.data.content.active").value(updateStockLocationViewResponse.getActive()));

        verify(stockLocationDomainRestApiAdapter, times(1))
                .updateStockLocation(uuidArgumentCaptor.capture(), updateStockLocationViewRequestArgumentCaptor.capture());

        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(stockLocationUUID);
        assertThat(updateStockLocationViewRequestArgumentCaptor.getValue()).isEqualTo(updateStockLocationViewRequest);
    }

    @Test
    void should_fail_when_update_stock_location_with_existing_name() throws Exception {
        //Given
        UUID stockLocationUUID = UUID.fromString("0192c540-3469-7da3-89e6-c8e65c701697");
        String name = "Internal Location Update";

        UpdateStockLocationViewRequest updateStockLocationViewRequest = new UpdateStockLocationViewRequest()
                .id(stockLocationUUID)
                .name(name)
                .type(UpdateStockLocationViewRequest.TypeEnum.INTERNAL)
                .active(true);
        when(stockLocationDomainRestApiAdapter.updateStockLocation(stockLocationUUID, updateStockLocationViewRequest))
                .thenThrow(StockLocationNameConflictException.class.getConstructor(Object[].class).newInstance(new Object[]{new String[]{name}}));

        ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);

        ArgumentCaptor<UpdateStockLocationViewRequest> updateStockLocationViewRequestArgumentCaptor =
                ArgumentCaptor.forClass(UpdateStockLocationViewRequest.class);


        //Act + Then
        mockMvc.perform(put(INVENTORY_EMPLACEMENT_PATH_URI + "/" + stockLocationUUID)
                        .accept(APPLICATION_JSON)
                        .header(ACCEPT_LANGUAGE, EN_LOCALE)
                        .contentType(APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateStockLocationViewRequest)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(409))
                .andExpect(jsonPath("$.reason").value(MessageUtil.getMessage(STOCK_LOCATION_NAME_CONFLICT, Locale.forLanguageTag(EN_LOCALE), name)));

        verify(stockLocationDomainRestApiAdapter, times(1))
                .updateStockLocation(uuidArgumentCaptor.capture(), updateStockLocationViewRequestArgumentCaptor.capture());

        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(stockLocationUUID);
        assertThat(updateStockLocationViewRequestArgumentCaptor.getValue()).isEqualTo(updateStockLocationViewRequest);

    }

    @Test
    void should_fail_when_update_stock_location_with_non_existing_parent_id() throws Exception {
        //Given
        UUID stockLocationUUID = UUID.fromString("0192c540-3469-7da3-89e6-c8e65c701697");
        UUID parentUUID = UUID.fromString("0192c567-690a-7130-88e4-5e6bd55956b1");
        String name = "Internal Location Update";

        UpdateStockLocationViewRequest updateStockLocationViewRequest = new UpdateStockLocationViewRequest()
                .id(stockLocationUUID)
                .name(name)
                .type(UpdateStockLocationViewRequest.TypeEnum.INTERNAL)
                .parentId(parentUUID)
                .active(true);
        when(stockLocationDomainRestApiAdapter.updateStockLocation(stockLocationUUID, updateStockLocationViewRequest))
                .thenThrow(StockLocationParentIdNotFoundException.class.getConstructor(Object[].class)
                        .newInstance(new Object[]{new String[]{parentUUID.toString()}}));

        ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);

        ArgumentCaptor<UpdateStockLocationViewRequest> updateStockLocationViewRequestArgumentCaptor =
                ArgumentCaptor.forClass(UpdateStockLocationViewRequest.class);


        //Act + Then
        mockMvc.perform(put(INVENTORY_EMPLACEMENT_PATH_URI + "/" + stockLocationUUID)
                        .accept(APPLICATION_JSON)
                        .header(ACCEPT_LANGUAGE, EN_LOCALE)
                        .contentType(APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateStockLocationViewRequest)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.reason").value(MessageUtil.getMessage(STOCK_LOCATION_PARENT_ID_NOT_FOUND, Locale.forLanguageTag(EN_LOCALE), parentUUID.toString())));

        verify(stockLocationDomainRestApiAdapter, times(1))
                .updateStockLocation(uuidArgumentCaptor.capture(), updateStockLocationViewRequestArgumentCaptor.capture());

        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(stockLocationUUID);
        assertThat(updateStockLocationViewRequestArgumentCaptor.getValue()).isEqualTo(updateStockLocationViewRequest);

    }
}
