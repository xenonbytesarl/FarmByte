package cm.xenonbyte.farmbyte.bootstrap.catalog;

import cm.xenonbyte.farmbyte.bootstrap.DatabaseSetupExtension;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.ApiErrorResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.ApiSuccessResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.CreateUomCategoryViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.CreateUomCategoryViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.uom.UomCategoryServiceRestApiAdapter;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Stream;

import static cm.xenonbyte.farmbyte.catalog.adapter.rest.api.uom.UomCategoryRestApi.UOM_CATEGORY_CREATED_SUCCESSFULLY;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.UOM_CATEGORY_NAME_CONFLICT_EXCEPTION;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.UOM_PARENT_CATEGORY_NOT_FOUND_EXCEPTION;
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
@ActiveProfiles("test")
@TestPropertySource(locations = {"classpath:application.yml", "classpath:application-test.yml"})
@ExtendWith({DatabaseSetupExtension.class, SpringExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public final class UomCategoryRestApiIT {

    @LocalServerPort
    private int port;

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    UomCategoryServiceRestApiAdapter uomCategoryServiceRestApiAdapter;

    String BASE_URL;

    @BeforeEach
    void setUp() {
        BASE_URL = "http://localhost:" + port + "/api/v1/catalog/uom-categories";
    }

    static Stream<Arguments> createUomMethodSourceArgs() {
        return Stream.of(
                Arguments.of(
                        "Unite.1",
                        null
                ),
                Arguments.of(
                        "Unite.2",
                        UUID.fromString("01912c2e-b52d-7b85-9c12-85af49fc7798")
                )
        );
    }

    @ParameterizedTest
    @MethodSource("createUomMethodSourceArgs")
    void should_create_uom_category(
        String nameValue,
        UUID parentUomCategoryUUID
    ) throws URISyntaxException {
        //Given
        CreateUomCategoryViewRequest createUomCategoryViewRequest = getCreateUomCategoryViewRequest(nameValue, parentUomCategoryUUID);
        HttpEntity<CreateUomCategoryViewRequest> request = new HttpEntity<>(createUomCategoryViewRequest, getHttpHeaders());

        //Act
        ResponseEntity<ApiSuccessResponse> response = restTemplate.postForEntity(new URI(BASE_URL), request, ApiSuccessResponse.class);

        //Then
        assertThat(response.getStatusCode().value()).isEqualTo(201);
        assertThat(response.getBody().getSuccess()).isTrue();
        assertThat(response.getBody().getData().get(BODY)).isNotNull().isInstanceOf(CreateUomCategoryViewResponse.class);
        assertThat(response.getBody().getData().get(BODY).getId()).isNotNull().isInstanceOf(UUID.class);
        assertThat(response.getBody().getData().get(BODY).getActive()).isNotNull().isInstanceOf(Boolean.class);
        assertThat(response.getBody().getMessage()).isEqualTo(MessageUtil.getMessage(UOM_CATEGORY_CREATED_SUCCESSFULLY, Locale.forLanguageTag(EN_LOCALE), ""));

    }

    static Stream<Arguments> createUomMethodThrowExceptionSourceArgs() {
        return Stream.of(
                Arguments.of(
                        "Unite",
                        UUID.fromString("01912c2e-b52d-7b85-9c12-85af49fc7798"),
                        UOM_CATEGORY_NAME_CONFLICT_EXCEPTION,
                        409,
                        "Unite"

                ),
                Arguments.of(
                        "Unite.1",
                        UUID.fromString("019156f3-0db6-794e-bfe0-f371636cd410"),
                        UOM_PARENT_CATEGORY_NOT_FOUND_EXCEPTION,
                        404,
                        "019156f3-0db6-794e-bfe0-f371636cd410"
                )
        );
    }

    @ParameterizedTest
    @MethodSource("createUomMethodThrowExceptionSourceArgs")
    void should_throw_exception_when_create_uom_category_fail(
            String nameValue,
            UUID parentUomCategoryUUID,
            String exceptionMessage,
            int code,
            String args

    ) throws Exception {
        CreateUomCategoryViewRequest createUomCategoryViewRequest = getCreateUomCategoryViewRequest(nameValue, parentUomCategoryUUID);
        HttpEntity<CreateUomCategoryViewRequest> request = new HttpEntity<>(createUomCategoryViewRequest, getHttpHeaders());

        //Act
        ResponseEntity<ApiErrorResponse> response = restTemplate.postForEntity(new URI(BASE_URL), request, ApiErrorResponse.class);

        //Then
        assertThat(response.getStatusCode().value()).isEqualTo(code);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getSuccess()).isFalse();
        assertThat(response.getBody().getReason()).isEqualTo(MessageUtil.getMessage(exceptionMessage, Locale.forLanguageTag(EN_LOCALE), args));
    }

    @Test
    void should_throw_exception_when_create_uom_category_without_required_field() throws Exception {
        CreateUomCategoryViewRequest createUomCategoryViewRequest = getCreateUomCategoryViewRequest(null, null);

        HttpEntity<CreateUomCategoryViewRequest> request = new HttpEntity<>(createUomCategoryViewRequest, getHttpHeaders());

        //Act
        ResponseEntity<ApiErrorResponse> response = restTemplate.postForEntity(new URI(BASE_URL), request, ApiErrorResponse.class);

        //Then
        assertThat(response.getStatusCode().value()).isEqualTo(400);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getSuccess()).isFalse();
        assertThat(response.getBody().getReason()).isEqualTo(MessageUtil.getMessage(VALIDATION_ERROR_OCCURRED_WHEN_PROCESSING_REQUEST, Locale.forLanguageTag(EN_LOCALE), "args"));
        assertThat(response.getBody().getError()).isNotEmpty();
        assertThat(response.getBody().getError().getFirst().getField()).isNotEmpty();
        assertThat(response.getBody().getError().getFirst().getMessage()).isNotEmpty();
    }

    private static CreateUomCategoryViewRequest getCreateUomCategoryViewRequest(String nameValue, UUID parentUomCategoryUUID) {
        return new CreateUomCategoryViewRequest()
                .name(nameValue)
                .parentUomCategoryId(parentUomCategoryUUID);
    }

    private static @NotNull HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAcceptLanguageAsLocales(List.of(Locale.forLanguageTag(EN_LOCALE)));
        headers.setAccept(List.of(APPLICATION_JSON));
        headers.setContentType(APPLICATION_JSON);
        return headers;
    }
}
