package cm.xenonbyte.farmbyte.catalog.adapter.rest.api.uom;


import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.RestApiBeanConfigTest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.CreateUomViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.CreateUomViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.FindUomByIdViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.FindUomsPageInfoViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.FindUomsViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.SearchUomsPageInfoViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.SearchUomsViewResponse;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomNameConflictException;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomNotFoundException;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomReferenceConflictException;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomType;
import cm.xenonbyte.farmbyte.common.adapter.api.messages.MessageUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
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

import static cm.xenonbyte.farmbyte.catalog.adapter.rest.api.uom.UomRestApi.UOMS_FIND_SUCCESSFULLY;
import static cm.xenonbyte.farmbyte.catalog.adapter.rest.api.uom.UomRestApi.UOM_FIND_SUCCESSFULLY;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.UOM_NAME_CONFLICT_EXCEPTION;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.UOM_NOT_FOUND_EXCEPTION;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.UOM_REFERENCE_CONFLICT_CATEGORY;
import static cm.xenonbyte.farmbyte.common.adapter.api.constant.CommonAdapterRestApi.ACCEPT_LANGUAGE;
import static cm.xenonbyte.farmbyte.common.adapter.api.constant.CommonAdapterRestApi.EN_LOCALE;
import static cm.xenonbyte.farmbyte.common.adapter.api.constant.CommonAdapterRestApi.VALIDATION_ERROR_OCCURRED_WHEN_PROCESSING_REQUEST;
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
 * @since 08/08/2024
 */
@ActiveProfiles("test")
@WebMvcTest(UomRestApi.class)
@ComponentScan(basePackages = "cm.xenonbyte.farmbyte.catalog.adapter.rest.api")
@ContextConfiguration(classes = {UomDomainServiceRestApiAdapter.class})
final class UomRestApiTest extends RestApiBeanConfigTest {

    public static final String UOM_PATH_URI = "/api/v1/catalog/uoms";
    @Autowired
    private MockMvc mockMvc;

    @Nested
    class CreateUomRestApiTest {
        static Stream<Arguments> createUomMethodSourceArgs() {
            return Stream.of(
                    Arguments.of(
                            UUID.randomUUID(),
                            "Unite",
                            null,
                            1.0,
                            CreateUomViewRequest.UomTypeEnum.REFERENCE,
                            CreateUomViewResponse.UomTypeEnum.REFERENCE
                    ),
                    Arguments.of(
                            UUID.randomUUID(),
                            "Carton de 5",
                            5.0,
                            5.0,
                            CreateUomViewRequest.UomTypeEnum.GREATER,
                            CreateUomViewResponse.UomTypeEnum.GREATER
                    ),
                    Arguments.of(
                            UUID.randomUUID(),
                            "Centimetre",
                            0.1,
                            0.1,
                            CreateUomViewRequest.UomTypeEnum.LOWER,
                            CreateUomViewResponse.UomTypeEnum.LOWER
                    )
            );
        }

        @ParameterizedTest
        @MethodSource("createUomMethodSourceArgs")
        void should_create_uom_when_uom_type_is_reference(
                UUID uomCategoryId,
                String name,
                Double ratioRequest,
                Double ratioResponse,
                CreateUomViewRequest.UomTypeEnum uomTypeEnumRequest,
                CreateUomViewResponse.UomTypeEnum uomTypeEnumResponse
        ) throws Exception {

            //Given
            String message = "UomApiRest.1";
            CreateUomViewRequest createUomViewRequest = generateCreateUomViewRequest(uomCategoryId, name, ratioRequest, uomTypeEnumRequest);

            CreateUomViewResponse createUomViewResponse = generateCreateUomViewResponse(uomCategoryId, name, ratioResponse, uomTypeEnumResponse);

            when(uomDomainServiceRestApiAdapter.createUom(createUomViewRequest)).thenReturn(createUomViewResponse);
            ArgumentCaptor<CreateUomViewRequest> createUomViewRequestArgumentCaptor = ArgumentCaptor.forClass(CreateUomViewRequest.class);

            //When + Then
            mockMvc.perform(post(UOM_PATH_URI)
                            .accept(APPLICATION_JSON)
                            .contentType(APPLICATION_JSON)
                            .header(ACCEPT_LANGUAGE, EN_LOCALE)
                            .content(createUomViewRequestAsString(createUomViewRequest)))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$").isNotEmpty())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.code").value(201))
                    .andExpect(jsonPath("$.status").value("CREATED"))
                    .andExpect(jsonPath("$.data").isNotEmpty())
                    .andExpect(jsonPath("$.data.content.name").value(name))
                    .andExpect(jsonPath("$.data.content.ratio").value(ratioResponse))
                    .andExpect(jsonPath("$.data.content.active").value(true))
                    .andExpect(jsonPath("$.data.content.uomCategoryId").value(uomCategoryId.toString()))
                    .andExpect(jsonPath("$.data.content.id").isNotEmpty())
                    .andExpect(jsonPath("$.message").value(MessageUtil.getMessage(message, Locale.forLanguageTag(EN_LOCALE), "")));

            verify(uomDomainServiceRestApiAdapter, times(1)).createUom(createUomViewRequestArgumentCaptor.capture());
            assertThat(createUomViewRequestArgumentCaptor.getValue()).isEqualTo(createUomViewRequest);

        }


        static Stream<Arguments> createUomThrowExceptionMethodSourceArgs() {
            return Stream.of(
                    Arguments.of(
                            UUID.randomUUID(),
                            "Unit",
                            null,
                            CreateUomViewRequest.UomTypeEnum.GREATER,
                            "Uom.1"
                    ),
                    Arguments.of(
                            UUID.randomUUID(),
                            "Carton de 10",
                            0.8,
                            CreateUomViewRequest.UomTypeEnum.GREATER,
                            "Uom.2"
                    ),
                    Arguments.of(
                            UUID.randomUUID(),
                            "Carton de 10",
                            2.0,
                            CreateUomViewRequest.UomTypeEnum.LOWER,
                            "Uom.3"
                    )
            );
        }


        @ParameterizedTest
        @MethodSource("createUomThrowExceptionMethodSourceArgs")
        void should_throw_exception_when_create_uom_with_type_and_ratio_are_not_compatible(
                UUID uomCategoryId,
                String name,
                Double ratioRequest,
                CreateUomViewRequest.UomTypeEnum uomTypeEnumRequest,
                String exceptionMessage
        ) throws Exception {

            //Given
            CreateUomViewRequest createUomViewRequest = generateCreateUomViewRequest(uomCategoryId, name, ratioRequest, uomTypeEnumRequest);

            when(uomDomainServiceRestApiAdapter.createUom(createUomViewRequest)).thenThrow(new IllegalArgumentException(exceptionMessage));

            ArgumentCaptor<CreateUomViewRequest> createUomViewRequestArgumentCaptor = ArgumentCaptor.forClass(CreateUomViewRequest.class);

            //When + Then
            mockMvc.perform(post(UOM_PATH_URI)
                            .accept(APPLICATION_JSON)
                            .contentType(APPLICATION_JSON)
                            .header(ACCEPT_LANGUAGE, EN_LOCALE)
                            .content(createUomViewRequestAsString(createUomViewRequest)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$").isNotEmpty())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.code").value(400))
                    .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                    .andExpect(jsonPath("$.reason").value(MessageUtil.getMessage(exceptionMessage, Locale.forLanguageTag(EN_LOCALE),"")));

            verify(uomDomainServiceRestApiAdapter, times(1)).createUom(createUomViewRequestArgumentCaptor.capture());
            assertThat(createUomViewRequestArgumentCaptor.getValue()).isEqualTo(createUomViewRequest);

        }

        @Test
        void should_create_uom_when_create_two_uom_with_uom_type_as_greater_for_the_same_category() throws Exception {
            String name = "Unite";
            UUID uomCategoryId = UUID.randomUUID();
            CreateUomViewRequest createUomViewRequest = generateCreateUomViewRequest(
                    uomCategoryId,
                    name,
                    null,
                    CreateUomViewRequest.UomTypeEnum.REFERENCE
            );

            when(uomDomainServiceRestApiAdapter.createUom(createUomViewRequest)).thenThrow(new UomReferenceConflictException());

            //Act + Then
            mockMvc.perform(post(UOM_PATH_URI)
                            .accept(APPLICATION_JSON)
                            .contentType(APPLICATION_JSON)
                            .header(ACCEPT_LANGUAGE, EN_LOCALE)
                            .content(createUomViewRequestAsString(createUomViewRequest)))
                    .andDo(print())
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$").isNotEmpty())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.code").value(409))
                    .andExpect(jsonPath("$.status").value("CONFLICT"))
                    .andExpect(jsonPath("$.reason").value(MessageUtil.getMessage(UOM_REFERENCE_CONFLICT_CATEGORY,  Locale.forLanguageTag(EN_LOCALE),"")));

        }


        @Test
        void should_throw_exception_when_create_two_uom_with_same_name() throws Exception {
            String name = "Unite";
            UUID uomCategoryId = UUID.randomUUID();
            CreateUomViewRequest createUomViewRequest = generateCreateUomViewRequest(
                    uomCategoryId,
                    name,
                    null,
                    CreateUomViewRequest.UomTypeEnum.REFERENCE
            );

            when(uomDomainServiceRestApiAdapter.createUom(createUomViewRequest)).thenThrow(new UomNameConflictException(new Object[]{name}));

            //Act + Then
            mockMvc.perform(post(UOM_PATH_URI)
                            .accept(APPLICATION_JSON)
                            .contentType(APPLICATION_JSON)
                            .header(ACCEPT_LANGUAGE, EN_LOCALE)
                            .content(createUomViewRequestAsString(createUomViewRequest)))
                    .andDo(print())
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$").isNotEmpty())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.code").value(409))
                    .andExpect(jsonPath("$.status").value("CONFLICT"))
                    .andExpect(jsonPath("$.reason").value(MessageUtil.getMessage(UOM_NAME_CONFLICT_EXCEPTION, Locale.forLanguageTag(EN_LOCALE), name)));

        }

        @Test
        void should_throw_exception_when_a_least_of_one_require_attribute_is_not_present() throws Exception {
            UUID uomCategoryId = UUID.randomUUID();
            CreateUomViewRequest createUomViewRequest = generateCreateUomViewRequest(
                    uomCategoryId,
                    null,
                    null,
                    null
            );

            //Act + Then
            mockMvc.perform(post(UOM_PATH_URI)
                            .accept(APPLICATION_JSON)
                            .contentType(APPLICATION_JSON)
                            .header(ACCEPT_LANGUAGE, EN_LOCALE)
                            .content(createUomViewRequestAsString(createUomViewRequest)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$").isNotEmpty())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.code").value(400))
                    .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                    .andExpect(jsonPath("$.reason").value(MessageUtil.getMessage(VALIDATION_ERROR_OCCURRED_WHEN_PROCESSING_REQUEST, Locale.forLanguageTag(EN_LOCALE), "")))
                    .andExpect(jsonPath("$.error").isNotEmpty())
                    .andExpect(jsonPath("$.error[0].message").isNotEmpty())
                    .andExpect(jsonPath("$.error[0].field").isNotEmpty());
        }
    }

    @Nested
    class FindUomByIdRestApiTest {
        @Test
        void should_success_when_find_uom_by_existing_uom_id() throws Exception {
            //Given
            UUID uomIdUUID = UUID.fromString("0191b400-bb9a-7a1e-99cf-443187ee09f6");
            FindUomByIdViewResponse findUomByIdViewResponse = new FindUomByIdViewResponse()
                    .id(uomIdUUID)
                    .name("Piece")
                    .uomType(FindUomByIdViewResponse.UomTypeEnum.REFERENCE)
                    .ratio(1D)
                    .uomCategoryId(UUID.randomUUID())
                    .active(true);

            when(uomDomainServiceRestApiAdapter.findUomById(uomIdUUID)).thenReturn(findUomByIdViewResponse);
            ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);

            //Act
            mockMvc.perform(get(String.format(UOM_PATH_URI + "/%s", uomIdUUID))
                            .accept(APPLICATION_JSON)
                            .header(ACCEPT_LANGUAGE, EN_LOCALE)
                            .contentType(APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isNotEmpty())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.status").value("OK"))
                    .andExpect(jsonPath("$.message").value(MessageUtil.getMessage(UOM_FIND_SUCCESSFULLY, Locale.forLanguageTag(EN_LOCALE), "")))
                    .andExpect(jsonPath("$.data").isMap())
                    .andExpect(jsonPath("$.data.content").isNotEmpty())
                    .andExpect(jsonPath("$.data.content.id").value(findUomByIdViewResponse.getId().toString()))
                    .andExpect(jsonPath("$.data.content.name").value(findUomByIdViewResponse.getName()))
                    .andExpect(jsonPath("$.data.content.uomType").value(findUomByIdViewResponse.getUomType().toString()))
                    .andExpect(jsonPath("$.data.content.ratio").value(findUomByIdViewResponse.getRatio()))
                    .andExpect(jsonPath("$.data.content.uomCategoryId").value(findUomByIdViewResponse.getUomCategoryId().toString()))
                    .andExpect(jsonPath("$.data.content.active").value(findUomByIdViewResponse.getActive()));

            //Then
            verify(uomDomainServiceRestApiAdapter, times(1)).findUomById(uuidArgumentCaptor.capture());
            assertThat(uuidArgumentCaptor.getValue()).isEqualTo(uomIdUUID);
        }

        @Test
        void should_fail_when_find_uom_with_non_existing_id() throws Exception {
            //Given
            UUID uomIdUUID = UUID.fromString("0191b400-bb9a-7a1e-99cf-443187ee09f6");

            when(uomDomainServiceRestApiAdapter.findUomById(uomIdUUID)).thenThrow(UomNotFoundException.class.getConstructor(Object[].class).newInstance(new Object[]{new String[] {uomIdUUID.toString()}}));
            ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);

            //Act
            mockMvc.perform(get(String.format(UOM_PATH_URI + "/%s", uomIdUUID))
                            .accept(APPLICATION_JSON)
                            .header(ACCEPT_LANGUAGE, EN_LOCALE)
                            .contentType(APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$").isNotEmpty())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.code").value(404))
                    .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                    .andExpect(jsonPath("$.reason").value(MessageUtil.getMessage(UOM_NOT_FOUND_EXCEPTION, Locale.forLanguageTag(EN_LOCALE), uomIdUUID.toString())));

            verify(uomDomainServiceRestApiAdapter, times(1)).findUomById(uuidArgumentCaptor.capture());
            assertThat(uuidArgumentCaptor.getValue()).isEqualTo(uomIdUUID);
        }
    }

    @Nested
    class FindUomsRestApiTest {

        @Test
        void should_success_when_find_uoms() throws Exception {
            int page = 0;
            int size = 2;
            String attribute = "name";
            String direction = "ASC";
            FindUomsPageInfoViewResponse findUomPageInfoViewResponse = new FindUomsPageInfoViewResponse()
                    .first(true)
                    .last(false)
                    .pageSize(2)
                    .totalElements(5L)
                    .totalPages(3)
                    .content(
                            List.of(
                                    new FindUomsViewResponse()
                                            .id(UUID.randomUUID())
                                            .uomType(FindUomsViewResponse.UomTypeEnum.valueOf(UomType.REFERENCE.name()))
                                            .name("piece")
                                            .uomCategoryId(UUID.randomUUID()),
                                    new FindUomsViewResponse()
                                            .id(UUID.randomUUID())
                                            .uomType(FindUomsViewResponse.UomTypeEnum.valueOf(UomType.REFERENCE.name()))
                                            .name("metre")
                                            .uomCategoryId(UUID.randomUUID()),
                                    new FindUomsViewResponse()
                                            .id(UUID.randomUUID())
                                            .uomType(FindUomsViewResponse.UomTypeEnum.valueOf(UomType.REFERENCE.name()))
                                            .name("heure")
                                            .uomCategoryId(UUID.randomUUID()),
                                    new FindUomsViewResponse()
                                            .id(UUID.randomUUID())
                                            .uomType(FindUomsViewResponse.UomTypeEnum.valueOf(UomType.REFERENCE.name()))
                                            .name("litre")
                                            .uomCategoryId(UUID.randomUUID()),
                                    new FindUomsViewResponse()
                                            .id(UUID.randomUUID())
                                            .uomType(FindUomsViewResponse.UomTypeEnum.valueOf(UomType.REFERENCE.name()))
                                            .name("gramme")
                                            .uomCategoryId(UUID.randomUUID())
                            )
                    );

            when(uomDomainServiceRestApiAdapter.findUoms(page, size, attribute, direction)).thenReturn(findUomPageInfoViewResponse);
            ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
            ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

            //Act + Then
            mockMvc.perform(get(UOM_PATH_URI )
                            .accept(APPLICATION_JSON)
                            .header(ACCEPT_LANGUAGE, EN_LOCALE)
                            .header("page", page)
                            .header("size", size)
                            .header("attribute", attribute)
                            .header("direction", direction)
                            .contentType(APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isNotEmpty())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.status").value("OK"))
                    .andExpect(jsonPath("$.message").value(MessageUtil.getMessage(UOMS_FIND_SUCCESSFULLY, Locale.forLanguageTag(EN_LOCALE), "")))
                    .andExpect(jsonPath("$.data").isNotEmpty())
                    .andExpect(jsonPath("$.data.content.content").isArray());

            //Then
            verify(uomDomainServiceRestApiAdapter, times(1)).findUoms(integerArgumentCaptor.capture(), integerArgumentCaptor.capture(), stringArgumentCaptor.capture(), stringArgumentCaptor.capture());
            assertThat(integerArgumentCaptor.getAllValues().get(0)).isEqualTo(page);
            assertThat(integerArgumentCaptor.getAllValues().get(1)).isEqualTo(size);
            assertThat(stringArgumentCaptor.getAllValues().get(0)).isEqualTo(attribute);
            assertThat(stringArgumentCaptor.getAllValues().get(1)).isEqualTo(direction);
        }
    }

    @Nested
    class SearchUomsRestApiTest {

        @Test
        void should_success_when_search_uoms_with_keyword() throws Exception {
            //Given
            int page = 0;
            int size = 2;
            String attribute = "name";
            String direction = "ASC";
            String keyword = "e";

            SearchUomsPageInfoViewResponse searchUomPageInfoViewResponse = new SearchUomsPageInfoViewResponse()
                    .first(true)
                    .last(false)
                    .pageSize(2)
                    .totalElements(5L)
                    .totalPages(3)
                    .content(
                            List.of(
                                    new SearchUomsViewResponse()
                                            .id(UUID.randomUUID())
                                            .uomType(SearchUomsViewResponse.UomTypeEnum.valueOf(UomType.REFERENCE.name()))
                                            .name("piece")
                                            .uomCategoryId(UUID.randomUUID()),
                                    new SearchUomsViewResponse()
                                            .id(UUID.randomUUID())
                                            .uomType(SearchUomsViewResponse.UomTypeEnum.valueOf(UomType.REFERENCE.name()))
                                            .name("metre")
                                            .uomCategoryId(UUID.randomUUID()),
                                    new SearchUomsViewResponse()
                                            .id(UUID.randomUUID())
                                            .uomType(SearchUomsViewResponse.UomTypeEnum.valueOf(UomType.REFERENCE.name()))
                                            .name("heure")
                                            .uomCategoryId(UUID.randomUUID()),
                                    new SearchUomsViewResponse()
                                            .id(UUID.randomUUID())
                                            .uomType(SearchUomsViewResponse.UomTypeEnum.valueOf(UomType.REFERENCE.name()))
                                            .name("litre")
                                            .uomCategoryId(UUID.randomUUID()),
                                    new SearchUomsViewResponse()
                                            .id(UUID.randomUUID())
                                            .uomType(SearchUomsViewResponse.UomTypeEnum.valueOf(UomType.REFERENCE.name()))
                                            .name("gramme")
                                            .uomCategoryId(UUID.randomUUID())
                            )
                    );

            when(uomDomainServiceRestApiAdapter.searchUoms(page, size, attribute, direction, keyword)).thenReturn(searchUomPageInfoViewResponse);
            ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
            ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

            //Act + Then
            mockMvc.perform(get(UOM_PATH_URI + "/search")
                            .accept(APPLICATION_JSON)
                            .header(ACCEPT_LANGUAGE, EN_LOCALE)
                            .header("page", page)
                            .header("size", size)
                            .header("attribute", attribute)
                            .header("direction", direction)
                            .header("keyword", keyword)
                            .contentType(APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isNotEmpty())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.status").value("OK"))
                    .andExpect(jsonPath("$.message").value(MessageUtil.getMessage(UOMS_FIND_SUCCESSFULLY, Locale.forLanguageTag(EN_LOCALE), "")))
                    .andExpect(jsonPath("$.data").isNotEmpty())
                    .andExpect(jsonPath("$.data.content.content").isArray());

            //Then
            verify(uomDomainServiceRestApiAdapter, times(1)).searchUoms(integerArgumentCaptor.capture(), integerArgumentCaptor.capture(),
                    stringArgumentCaptor.capture(), stringArgumentCaptor.capture(), stringArgumentCaptor.capture());
            assertThat(integerArgumentCaptor.getAllValues().get(0)).isEqualTo(page);
            assertThat(integerArgumentCaptor.getAllValues().get(1)).isEqualTo(size);
            assertThat(stringArgumentCaptor.getAllValues().get(0)).isEqualTo(attribute);
            assertThat(stringArgumentCaptor.getAllValues().get(1)).isEqualTo(direction);
            assertThat(stringArgumentCaptor.getAllValues().get(2)).isEqualTo(keyword);
        }
    }

    private static CreateUomViewResponse generateCreateUomViewResponse(UUID uomCategoryId, String name, Double ratio, CreateUomViewResponse.UomTypeEnum uomTypeEnumResponse) {
        return new CreateUomViewResponse()
                .id(UUID.randomUUID())
                .uomCategoryId(uomCategoryId)
                .uomType(CreateUomViewResponse.UomTypeEnum.REFERENCE)
                .name(name)
                .ratio(ratio)
                .active(true)
                .uomType(uomTypeEnumResponse);
    }

    private static CreateUomViewRequest generateCreateUomViewRequest(UUID uomCategoryId, String name, Double ratioRequest, CreateUomViewRequest.UomTypeEnum uomTypeEnumRequest) {
        return new CreateUomViewRequest()
                .uomCategoryId(uomCategoryId)
                .name(name)
                .uomType(CreateUomViewRequest.UomTypeEnum.REFERENCE)
                .ratio(ratioRequest)
                .uomType(uomTypeEnumRequest);
    }

    private String createUomViewRequestAsString(CreateUomViewRequest createUomViewRequest) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(createUomViewRequest);
    }
}
