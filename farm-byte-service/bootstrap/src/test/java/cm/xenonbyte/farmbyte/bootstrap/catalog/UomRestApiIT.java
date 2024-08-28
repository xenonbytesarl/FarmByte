package cm.xenonbyte.farmbyte.bootstrap.catalog;

import cm.xenonbyte.farmbyte.bootstrap.DatabaseSetupExtension;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.ApiSuccessResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.CreateUomViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.CreateUomViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.ApiErrorResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.uom.UomServiceRestApiAdapter;
import cm.xenonbyte.farmbyte.common.adapter.api.messages.MessageUtil;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
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

import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Stream;

import static cm.xenonbyte.farmbyte.catalog.adapter.rest.api.uom.UomRestApi.UOM_CREATED_SUCCESSFULLY;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.UOM_NAME_CONFLICT_EXCEPTION;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.UOM_REFERENCE_CONFLICT_CATEGORY;
import static cm.xenonbyte.farmbyte.common.adapter.api.constant.CommonAdapterRestApi.BODY;
import static cm.xenonbyte.farmbyte.common.adapter.api.constant.CommonAdapterRestApi.EN_LOCALE;
import static cm.xenonbyte.farmbyte.common.adapter.api.constant.CommonAdapterRestApi.VALIDATION_ERROR_OCCURRED_WHEN_PROCESSING_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;

/**
 * @author bamk
 * @version 1.0
 * @since 28/08/2024
 */
@DirtiesContext
@ActiveProfiles("test")
@ExtendWith({DatabaseSetupExtension.class, SpringExtension.class})
@TestPropertySource(locations = {"classpath:application.yml", "classpath:application-test.yml"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public final class UomRestApiIT {

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

    static Stream<Arguments> createUomMethodSourceArgs() {
        return Stream.of(
                Arguments.of(
                        UUID.fromString("01919905-c273-7aaa-8965-ef4ed404e4b9"),
                        "Heure",
                        null,
                        CreateUomViewRequest.UomTypeEnum.REFERENCE
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
        String message = "UomApiRest.1";
        CreateUomViewRequest createUomViewRequest = generateCreateUomViewRequest(uomCategoryId, name, ratioRequest, uomTypeEnumRequest);
        HttpEntity<CreateUomViewRequest> request = new HttpEntity<>(createUomViewRequest, getHttpHeaders());

        //Act
        ResponseEntity<ApiSuccessResponse> response = restTemplate.postForEntity(new URI(BASE_URL), request, ApiSuccessResponse.class);

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
        ResponseEntity<ApiErrorResponse> response = restTemplate.postForEntity(new URI(BASE_URL), request, ApiErrorResponse.class);

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
        ResponseEntity<ApiErrorResponse> response = restTemplate.postForEntity(new URI(BASE_URL), request, ApiErrorResponse.class);

        //Then
        assertThat(response.getStatusCode().value()).isEqualTo(409);
        assertThat(response.getBody()).isNotNull();
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
        ResponseEntity<ApiErrorResponse> response = restTemplate.postForEntity(new URI(BASE_URL), request, ApiErrorResponse.class);

        //Then
        assertThat(response.getStatusCode().value()).isEqualTo(409);
        assertThat(response.getBody()).isNotNull();
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
        ResponseEntity<ApiErrorResponse> response = restTemplate.postForEntity(new URI(BASE_URL), request, ApiErrorResponse.class);

        //Then
        assertThat(response.getStatusCode().value()).isEqualTo(400);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getSuccess()).isFalse();
        assertThat(response.getBody().getReason()).isEqualTo(MessageUtil.getMessage(VALIDATION_ERROR_OCCURRED_WHEN_PROCESSING_REQUEST, Locale.forLanguageTag(EN_LOCALE), ""));
        assertThat(response.getBody().getError()).isNotEmpty();
        assertThat(response.getBody().getError().getFirst().getField()).isNotEmpty();
        assertThat(response.getBody().getError().getFirst().getMessage()).isNotEmpty();
    }

        private static CreateUomViewRequest generateCreateUomViewRequest(UUID uomCategoryId, String name, Double ratioRequest, CreateUomViewRequest.UomTypeEnum uomTypeEnumRequest) {
        return new CreateUomViewRequest()
                .uomCategoryId(uomCategoryId)
                .name(name)
                .uomType(CreateUomViewRequest.UomTypeEnum.REFERENCE)
                .ratio(ratioRequest)
                .uomType(uomTypeEnumRequest);
    }

    private static @NotNull HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAcceptLanguageAsLocales(List.of(Locale.forLanguageTag(EN_LOCALE)));
        headers.setAccept(List.of(APPLICATION_JSON));
        headers.setContentType(APPLICATION_JSON);
        return headers;
    }
}
