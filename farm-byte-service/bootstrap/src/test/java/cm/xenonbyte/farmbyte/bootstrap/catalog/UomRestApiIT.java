package cm.xenonbyte.farmbyte.bootstrap.catalog;

import cm.xenonbyte.farmbyte.bootstrap.DatabaseSetupExtension;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.ApiErrorResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.CreateUomViewApiResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.CreateUomViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.CreateUomViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.FindUomByIdViewApiResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.FindUomByIdViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.FindUomsPageInfoViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.FindUomsViewApiResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.SearchUomsPageInfoViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.SearchUomsViewApiResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.UpdateUomViewApiResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.UpdateUomViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.UpdateUomViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.uom.UomServiceRestApiAdapter;
import cm.xenonbyte.farmbyte.common.adapter.api.messages.MessageUtil;
import jakarta.annotation.Nonnull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.net.URISyntaxException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import static cm.xenonbyte.farmbyte.catalog.adapter.rest.api.uom.UomRestApi.UOMS_FIND_SUCCESSFULLY;
import static cm.xenonbyte.farmbyte.catalog.adapter.rest.api.uom.UomRestApi.UOM_CREATED_SUCCESSFULLY;
import static cm.xenonbyte.farmbyte.catalog.adapter.rest.api.uom.UomRestApi.UOM_FIND_SUCCESSFULLY;
import static cm.xenonbyte.farmbyte.catalog.adapter.rest.api.uom.UomRestApi.UOM_UPDATED_SUCCESSFULLY;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.UOM_CATEGORY_NOT_FOUND_EXCEPTION;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.UOM_NAME_CONFLICT_EXCEPTION;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.UOM_NOT_FOUND_EXCEPTION;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.UOM_REFERENCE_CONFLICT_CATEGORY;
import static cm.xenonbyte.farmbyte.common.adapter.api.constant.CommonAdapterRestApi.BODY;
import static cm.xenonbyte.farmbyte.common.adapter.api.constant.CommonAdapterRestApi.EN_LOCALE;
import static cm.xenonbyte.farmbyte.common.adapter.api.constant.CommonAdapterRestApi.VALIDATION_ERROR_OCCURRED_WHEN_PROCESSING_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.MediaType.APPLICATION_JSON;

/**
 * @author bamk
 * @version 1.0
 * @since 28/08/2024
 */
@DirtiesContext
@ActiveProfiles("test")
@ExtendWith({DatabaseSetupExtension.class, SpringExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = {"classpath:application.yml", "classpath:application-test.yml"})
public class UomRestApiIT {

    public static final String FIND_URL_PARAM = "?page={page}&size={size}&attribute={attribute}&direction={direction}";
    public static final String SEARCH_URL_PARAM = FIND_URL_PARAM + "&keyword={keyword}";

    @LocalServerPort
    private int port;

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    private UomServiceRestApiAdapter uomServiceRestApiAdapter;

    String BASE_URL;

    @BeforeEach
    void setUp() {
        BASE_URL = "http://localhost:" + port + "/api/v1/catalog/uoms";
    }

    @Nested
    class CreateUomRestApiIT {

        static Stream<Arguments> createUomMethodSourceArgs() {
            return Stream.of(
                    Arguments.of(
                            UUID.fromString("01919905-c273-7aaa-8965-ef4ed404e4b9"),
                            "Minute",
                            60.0,
                            CreateUomViewRequest.UomTypeEnum.GREATER
                    ),
                    Arguments.of(
                            UUID.fromString("01912c0f-2fcf-705b-ae59-d79d159f3ad0"),
                            "Carton de 10",
                            10.0,
                            CreateUomViewRequest.UomTypeEnum.GREATER
                    ),
                    Arguments.of(
                            UUID.fromString("01912c2e-b52d-7b85-9c12-85af49fc7798"),
                            "Centimetre",
                            0.1,
                            CreateUomViewRequest.UomTypeEnum.LOWER
                    )
            );
        }

        @ParameterizedTest
        @MethodSource("createUomMethodSourceArgs")
        void should_create_uom_when_uom_type_is_reference(
                UUID uomCategoryId,
                String name,
                Double ratioRequest,
                CreateUomViewRequest.UomTypeEnum uomTypeEnumRequest
        ) throws Exception {

            //Given
            CreateUomViewRequest createUomViewRequest = generateCreateUomViewRequest(uomCategoryId, name, ratioRequest, uomTypeEnumRequest);
            HttpEntity<CreateUomViewRequest> request = new HttpEntity<>(createUomViewRequest, getHttpHeaders());

            //Act
            ResponseEntity<CreateUomViewApiResponse> response = restTemplate.exchange(BASE_URL, POST, request, CreateUomViewApiResponse.class);

            //Then
            assertThat(response.getStatusCode().value()).isEqualTo(201);
            assertThat(response.getBody().getSuccess()).isTrue();
            assertThat(response.getBody().getData().get(BODY)).isNotNull().isInstanceOf(CreateUomViewResponse.class);
            assertThat(response.getBody().getData().get(BODY).getId()).isNotNull().isInstanceOf(UUID.class);
            assertThat(response.getBody().getData().get(BODY).getActive()).isNotNull().isInstanceOf(Boolean.class);
            assertThat(response.getBody().getMessage()).isEqualTo(MessageUtil.getMessage(UOM_CREATED_SUCCESSFULLY, Locale.forLanguageTag(EN_LOCALE), ""));

        }

        static Stream<Arguments> createUomThrowExceptionMethodSourceArgs() {
            return Stream.of(
                    Arguments.of(
                            UUID.fromString("01919905-c273-7aaa-8965-ef4ed404e4b9"),
                            "Unite",
                            null,
                            CreateUomViewRequest.UomTypeEnum.GREATER,
                            "Uom.1",
                            400
                    ),
                    Arguments.of(
                            UUID.fromString("01919905-c273-7aaa-8965-ef4ed404e4b9"),
                            "Carton de 10",
                            0.8,
                            CreateUomViewRequest.UomTypeEnum.GREATER,
                            "Uom.2",
                            400
                    ),
                    Arguments.of(
                            UUID.fromString("01919905-c273-7aaa-8965-ef4ed404e4b9"),
                            "Carton de 10",
                            2.0,
                            CreateUomViewRequest.UomTypeEnum.LOWER,
                            "Uom.3",
                            400
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
                String exceptionMessage,
                int code
        ) throws Exception {

            //Given
            CreateUomViewRequest createUomViewRequest = generateCreateUomViewRequest(uomCategoryId, name, ratioRequest, uomTypeEnumRequest);
            HttpEntity<CreateUomViewRequest> request = new HttpEntity<>(createUomViewRequest, getHttpHeaders());

            //Act
            ResponseEntity<ApiErrorResponse> response = restTemplate.exchange(BASE_URL, POST, request, ApiErrorResponse.class);

            //Then
            assertThat(response.getStatusCode().value()).isEqualTo(code);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getSuccess()).isFalse();
            assertThat(response.getBody().getReason()).isEqualTo(MessageUtil.getMessage(exceptionMessage, Locale.forLanguageTag(EN_LOCALE), ""));
        }

        @Test
        void should_create_uom_when_create_two_uom_with_uom_type_as_greater_for_the_same_category() throws Exception {
            //Given
            String name = "Unite";
            UUID uomCategoryId = UUID.fromString("01912c0f-2fcf-705b-ae59-d79d159f3ad0");
            CreateUomViewRequest createUomViewRequest = generateCreateUomViewRequest(
                    uomCategoryId,
                    name,
                    null,
                    CreateUomViewRequest.UomTypeEnum.REFERENCE
            );

            HttpEntity<CreateUomViewRequest> request = new HttpEntity<>(createUomViewRequest, getHttpHeaders());

            //Act
            ResponseEntity<ApiErrorResponse> response = restTemplate.exchange(BASE_URL, POST, request, ApiErrorResponse.class);

            //Then
            assertThat(response.getStatusCode().value()).isEqualTo(409);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getStatus()).isEqualTo("CONFLICT");
            assertThat(response.getBody().getCode()).isEqualTo(409);
            assertThat(response.getBody().getSuccess()).isFalse();
            assertThat(response.getBody().getReason()).isEqualTo(MessageUtil.getMessage(UOM_REFERENCE_CONFLICT_CATEGORY, Locale.forLanguageTag(EN_LOCALE), ""));
        }

        @Test
        void should_throw_exception_when_create_two_uom_with_same_name() throws Exception {
            //Given
            String name = "Unite";
            UUID uomCategoryId = UUID.fromString("01912c0f-2fcf-705b-ae59-d79d159f3ad0");
            CreateUomViewRequest createUomViewRequest = generateCreateUomViewRequest(
                    uomCategoryId,
                    name,
                    2.0,
                    CreateUomViewRequest.UomTypeEnum.GREATER
            );

            HttpEntity<CreateUomViewRequest> request = new HttpEntity<>(createUomViewRequest, getHttpHeaders());

            //Act
            ResponseEntity<ApiErrorResponse> response = restTemplate.exchange(BASE_URL, POST, request, ApiErrorResponse.class);

            //Then
            assertThat(response.getStatusCode().value()).isEqualTo(409);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getStatus()).isEqualTo("CONFLICT");
            assertThat(response.getBody().getCode()).isEqualTo(409);
            assertThat(response.getBody().getSuccess()).isFalse();
            assertThat(response.getBody().getReason()).isEqualTo(MessageUtil.getMessage(UOM_NAME_CONFLICT_EXCEPTION, Locale.forLanguageTag(EN_LOCALE), name));
        }

        @Test
        void should_throw_exception_when_a_least_of_one_require_attribute_is_not_present() throws Exception {
            //Given
            UUID uomCategoryId = UUID.fromString("01912c0f-2fcf-705b-ae59-d79d159f3ad0");
            CreateUomViewRequest createUomViewRequest = generateCreateUomViewRequest(
                    uomCategoryId,
                    null,
                    null,
                    null
            );

            HttpEntity<CreateUomViewRequest> request = new HttpEntity<>(createUomViewRequest, getHttpHeaders());

            //Act
            ResponseEntity<ApiErrorResponse> response = restTemplate.exchange(BASE_URL, POST, request, ApiErrorResponse.class);

            //Then
            assertThat(response.getStatusCode().value()).isEqualTo(400);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getSuccess()).isFalse();
            assertThat(response.getBody().getReason()).isEqualTo(MessageUtil.getMessage(VALIDATION_ERROR_OCCURRED_WHEN_PROCESSING_REQUEST, Locale.forLanguageTag(EN_LOCALE), ""));
            assertThat(response.getBody().getError()).isNotEmpty();
            assertThat(response.getBody().getError().getFirst().getField()).isNotEmpty();
            assertThat(response.getBody().getError().getFirst().getMessage()).isNotEmpty();
        }
    }

    @Nested
    class FindUomByIdRestApiIT {

        @Test
        void should_success_when_find_uom_with_existing_id() throws URISyntaxException {
            //Given
            String uomUUID = "01912c2c-b81a-7245-bab1-aee9b97b2afb";
            HttpEntity<Object> request = new HttpEntity<>(getHttpHeaders());
            FindUomByIdViewResponse findUomByIdViewResponse = new FindUomByIdViewResponse()
                    .id(UUID.fromString(uomUUID))
                    .name("Unite")
                    .uomCategoryId(UUID.fromString("01912c0f-2fcf-705b-ae59-d79d159f3ad0"))
                    .uomType(FindUomByIdViewResponse.UomTypeEnum.REFERENCE)
                    .ratio(1D)
                    .active(true);

            //Act
            ResponseEntity<FindUomByIdViewApiResponse> response = restTemplate.exchange(
                    (BASE_URL + "/" + uomUUID), GET, request, FindUomByIdViewApiResponse.class);

            //Then
            assertThat(response.getStatusCode().value()).isEqualTo(200);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getStatus()).isEqualTo("OK");
            assertThat(response.getBody().getSuccess()).isTrue();
            assertThat(response.getBody().getMessage()).isEqualTo(MessageUtil.getMessage(UOM_FIND_SUCCESSFULLY, Locale.forLanguageTag(EN_LOCALE), ""));
            assertThat(response.getBody().getData()).isNotEmpty();
            assertThat(response.getBody().getData().get(BODY)).isEqualTo(findUomByIdViewResponse);
        }

        @Test
        void should_fail_when_find_uom_category_with_non_existing_id() throws URISyntaxException {
            //Given
            String uomIdUUID = "0191b465-5f58-740d-b992-8fdfeec1d26b";
            HttpEntity<Object> request = new HttpEntity<>(getHttpHeaders());

            //Act
            ResponseEntity<ApiErrorResponse> response = restTemplate.exchange((BASE_URL + "/" + uomIdUUID), GET, request, ApiErrorResponse.class);

            //Then
            assertThat(response.getStatusCode().value()).isEqualTo(404);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getSuccess()).isFalse();
            assertThat(response.getBody().getStatus()).isEqualTo("NOT_FOUND");
            assertThat(response.getBody().getReason()).isEqualTo(MessageUtil.getMessage(UOM_NOT_FOUND_EXCEPTION, Locale.forLanguageTag(EN_LOCALE), uomIdUUID));
        }
    }

    @Nested
    class FindUomsRestApiIT {
        @Test
        void should_success_when_find_uoms() {
            //Given

            HttpHeaders httpHeaders = getHttpHeaders();

            Map<String, String> params = new LinkedHashMap<>();
            params.put("page", "0");
            params.put("size", "2");
            params.put("attribute", "name");
            params.put("direction", "DSC");

            HttpEntity<Object> request = new HttpEntity<>(httpHeaders);

            //Act
            ResponseEntity<FindUomsViewApiResponse> response = restTemplate.exchange(BASE_URL + FIND_URL_PARAM , GET, request, FindUomsViewApiResponse.class, params);

            //Then
            assertThat(response.getStatusCode().value()).isEqualTo(200);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getStatus()).isEqualTo("OK");
            assertThat(response.getBody().getSuccess()).isTrue();
            assertThat(response.getBody().getMessage()).isEqualTo(MessageUtil.getMessage(UOMS_FIND_SUCCESSFULLY, Locale.forLanguageTag(EN_LOCALE), ""));
            assertThat(response.getBody().getData().get(BODY)).isInstanceOf(FindUomsPageInfoViewResponse.class);
            assertThat(response.getBody().getData().get(BODY).getContent().size()).isGreaterThan(0);
        }
    }

    @Nested
    class SearchUomsRestApiIT {

        @Test
        void should_success_when_search_uom_categories_with_keyword() {
            //Given

            HttpHeaders httpHeaders = getHttpHeaders();

            Map<String, String> params = new LinkedHashMap<>();
            params.put("page", "0");
            params.put("size", "2");
            params.put("attribute", "name");
            params.put("direction", "DSC");
            params.put("keyword", "n");

            HttpEntity<Object> request = new HttpEntity<>(httpHeaders);

            //Act
            ResponseEntity<SearchUomsViewApiResponse> response = restTemplate.exchange(BASE_URL + "/search" + SEARCH_URL_PARAM, GET, request, SearchUomsViewApiResponse.class, params);

            //Then
            assertThat(response.getStatusCode().value()).isEqualTo(200);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getStatus()).isEqualTo("OK");
            assertThat(response.getBody().getSuccess()).isTrue();
            assertThat(response.getBody().getMessage()).isEqualTo(MessageUtil.getMessage(UOMS_FIND_SUCCESSFULLY, Locale.forLanguageTag(EN_LOCALE), ""));
            assertThat(response.getBody().getData().get(BODY)).isInstanceOf(SearchUomsPageInfoViewResponse.class);
            assertThat(response.getBody().getData().get(BODY).getContent().size()).isGreaterThan(0);
        }
    }
    
    @Nested
    class UpdateUomRestApiIT {
        @Test
        void should_success_when_update_uom() {
            //Given
            UUID uomIdUUID = UUID.fromString("0191d7ba-6781-7a63-9ec6-f0b99a908619");
            UUID uomCategoryIdUUID = UUID.fromString("01919905-c273-7aaa-8965-ef4ed404e4b9");
            String uomType = "REFERENCE";
            String name = "Nouvelle Heure";
            boolean active = true;
            double ratio = 1.0;

            UpdateUomViewRequest updateUomViewRequest = new UpdateUomViewRequest()
                    .id(uomIdUUID)
                    .name(name)
                    .uomCategoryId(uomCategoryIdUUID)
                    .uomType(UpdateUomViewRequest.UomTypeEnum.valueOf(uomType))
                    .ratio(ratio)
                    .active(active);
            HttpEntity<UpdateUomViewRequest> request = new HttpEntity<>(updateUomViewRequest, getHttpHeaders());

            //Act
            ResponseEntity<UpdateUomViewApiResponse> response = restTemplate.exchange(BASE_URL + "/" + uomIdUUID, PUT, request, UpdateUomViewApiResponse.class);

            //Then
            assertThat(response.getStatusCode().value()).isEqualTo(200);
            assertThat(response.getBody().getSuccess()).isTrue();
            assertThat(response.getBody().getStatus()).isEqualTo("OK");
            assertThat(response.getBody().getData().get(BODY)).isNotNull().isInstanceOf(UpdateUomViewResponse.class);
            assertThat(response.getBody().getData().get(BODY).getId()).isNotNull().isInstanceOf(UUID.class);
            assertThat(response.getBody().getData().get(BODY).getActive()).isNotNull().isInstanceOf(Boolean.class);
            assertThat(response.getBody().getMessage()).isEqualTo(MessageUtil.getMessage(UOM_UPDATED_SUCCESSFULLY, Locale.forLanguageTag(EN_LOCALE), ""));
        }

        @Test
        void should_fail_when_update_uom_with_duplicate_name() {
            //Given
            UUID uomIdUUID = UUID.fromString("0191d7ba-6781-7a63-9ec6-f0b99a908619");
            UUID uomCategoryIdUUID = UUID.fromString("01919905-c273-7aaa-8965-ef4ed404e4b9");
            String uomType = "REFERENCE";
            String name = "Jour";
            boolean active = true;
            double ratio = 1.0;

            UpdateUomViewRequest updateUomViewRequest = new UpdateUomViewRequest()
                    .id(uomIdUUID)
                    .name(name)
                    .uomCategoryId(uomCategoryIdUUID)
                    .uomType(UpdateUomViewRequest.UomTypeEnum.valueOf(uomType))
                    .ratio(ratio)
                    .active(active);
            HttpEntity<UpdateUomViewRequest> request = new HttpEntity<>(updateUomViewRequest, getHttpHeaders());

            //Act
            ResponseEntity<ApiErrorResponse> response = restTemplate.exchange(BASE_URL + "/" + uomIdUUID, PUT, request, ApiErrorResponse.class);

            //Then
            assertThat(response.getStatusCode().value()).isEqualTo(409);
            assertThat(response.getBody().getSuccess()).isFalse();
            assertThat(response.getBody().getStatus()).isEqualTo("CONFLICT");
            assertThat(response.getBody().getCode()).isEqualTo(409);
            assertThat(response.getBody().getReason()).isEqualTo(MessageUtil.getMessage(UOM_NAME_CONFLICT_EXCEPTION, Locale.forLanguageTag(EN_LOCALE), name));
        }

        @Test
        void should_fail_when_update_uom_with_non_existing_category() {
            //Given
            UUID uomIdUUID = UUID.fromString("019199bf-2ea3-7ac1-8ad4-6dd062d5efec");
            UUID uomCategoryIdUUID = UUID.fromString("0191d7f5-81a4-710b-b095-d04acdb50b89");
            String uomType = "GREATER";
            String name = "New Jour";
            boolean active = true;
            double ratio = 24.0;

            UpdateUomViewRequest updateUomViewRequest = new UpdateUomViewRequest()
                    .id(uomIdUUID)
                    .name(name)
                    .uomCategoryId(uomCategoryIdUUID)
                    .uomType(UpdateUomViewRequest.UomTypeEnum.valueOf(uomType))
                    .ratio(ratio)
                    .active(active);
            HttpEntity<UpdateUomViewRequest> request = new HttpEntity<>(updateUomViewRequest, getHttpHeaders());

            //Act
            ResponseEntity<ApiErrorResponse> response = restTemplate.exchange(BASE_URL + "/" + uomIdUUID, PUT, request, ApiErrorResponse.class);

            //Then
            assertThat(response.getStatusCode().value()).isEqualTo(404);
            assertThat(response.getBody().getSuccess()).isFalse();
            assertThat(response.getBody().getStatus()).isEqualTo("NOT_FOUND");
            assertThat(response.getBody().getCode()).isEqualTo(404);
            assertThat(response.getBody().getReason()).isEqualTo(MessageUtil.getMessage(UOM_CATEGORY_NOT_FOUND_EXCEPTION, Locale.forLanguageTag(EN_LOCALE), uomCategoryIdUUID.toString()));
        }

        @Test
        void should_fail_when_update_uom_with_duplicate_reference_and_category_id() {
            //Given
            UUID uomIdUUID = UUID.fromString("019199bf-2ea3-7ac1-8ad4-6dd062d5efec");
            UUID uomCategoryIdUUID = UUID.fromString("01919905-c273-7aaa-8965-ef4ed404e4b9");
            String uomType = "REFERENCE";
            String name = "Jour";
            boolean active = true;
            double ratio = 1.0;

            UpdateUomViewRequest updateUomViewRequest = new UpdateUomViewRequest()
                    .id(uomIdUUID)
                    .name(name)
                    .uomCategoryId(uomCategoryIdUUID)
                    .uomType(UpdateUomViewRequest.UomTypeEnum.valueOf(uomType))
                    .ratio(ratio)
                    .active(active);
            HttpEntity<UpdateUomViewRequest> request = new HttpEntity<>(updateUomViewRequest, getHttpHeaders());

            //Act
            ResponseEntity<ApiErrorResponse> response = restTemplate.exchange(BASE_URL + "/" + uomIdUUID, PUT, request, ApiErrorResponse.class);

            //Then
            assertThat(response.getStatusCode().value()).isEqualTo(409);
            assertThat(response.getBody().getSuccess()).isFalse();
            assertThat(response.getBody().getStatus()).isEqualTo("CONFLICT");
            assertThat(response.getBody().getCode()).isEqualTo(409);
            assertThat(response.getBody().getReason()).isEqualTo(MessageUtil.getMessage(UOM_REFERENCE_CONFLICT_CATEGORY, Locale.forLanguageTag(EN_LOCALE), ""));
        }
    }


    private static CreateUomViewRequest generateCreateUomViewRequest(UUID uomCategoryId, String name, Double ratioRequest, CreateUomViewRequest.UomTypeEnum uomTypeEnumRequest) {
        return new CreateUomViewRequest()
                .uomCategoryId(uomCategoryId)
                .name(name)
                .uomType(CreateUomViewRequest.UomTypeEnum.REFERENCE)
                .ratio(ratioRequest)
                .uomType(uomTypeEnumRequest);
    }

    private static @Nonnull HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAcceptLanguageAsLocales(List.of(Locale.forLanguageTag(EN_LOCALE)));
        headers.setAccept(List.of(APPLICATION_JSON));
        headers.setContentType(APPLICATION_JSON);
        return headers;
    }
}
