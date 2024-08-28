package cm.xenonbyte.farmbyte.bootstrap.catalog;

import cm.xenonbyte.farmbyte.bootstrap.DatabaseSetupExtension;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.ApiErrorResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.ApiSuccessResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.CreateProductViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.CreateProductViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.product.ProductServiceRestApiAdapter;
import cm.xenonbyte.farmbyte.common.adapter.api.messages.MessageUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nonnull;
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
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Stream;

import static cm.xenonbyte.farmbyte.catalog.adapter.rest.api.product.ProductRestApi.PRODUCT_CREATED_SUCCESSFULLY;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_CATEGORY_IS_REQUIRED;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_CATEGORY_NOT_FOUND_EXCEPTION;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_NAME_CONFLICT_EXCEPTION;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_NAME_IS_REQUIRED;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_STOCK_AND_PURCHASE_UOM_BAD_EXCEPTION;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_TYPE_IS_REQUIRED;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_UOM_NOT_FOUND_EXCEPTION;
import static cm.xenonbyte.farmbyte.common.adapter.api.constant.CommonAdapterRestApi.BODY;
import static cm.xenonbyte.farmbyte.common.adapter.api.constant.CommonAdapterRestApi.EN_LOCALE;
import static cm.xenonbyte.farmbyte.common.adapter.api.constant.CommonAdapterRestApi.VALIDATION_ERROR_OCCURRED_WHEN_PROCESSING_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;

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
public final class ProductRestApiIT {

    @LocalServerPort
    private int port;

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    ProductServiceRestApiAdapter productServiceRestApiAdapter;

    String BASE_URL;

    @BeforeEach
    void setUp() {
        BASE_URL = "http://localhost:" + port + "/api/v1/catalog/products";
    }

    @Test
    void should_create_product() throws Exception {
        //Given
        String name = "Product.1";
        UUID categoryId = UUID.fromString("01912c0f-2fcf-705b-ae59-d79d159f3ad0");

        CreateProductViewRequest createProductViewRequest = new CreateProductViewRequest()
                .name(name)
                .type(CreateProductViewRequest.TypeEnum.SERVICE)
                .categoryId(categoryId);

        Resource image = getImageResource("product_image.txt");

        MultiValueMap body = new LinkedMultiValueMap();

        body.add("image", image);
        body.add("createProductViewRequest", createProductViewRequest);

        HttpEntity<MultiValueMap<String, HttpEntity<?>>> request = new HttpEntity<>(body, getHttpHeaders());

        //Act
        ResponseEntity<ApiSuccessResponse> response = restTemplate.postForEntity(new URI(BASE_URL), request, ApiSuccessResponse.class);

        //Then
        assertThat(response.getStatusCode().value()).isEqualTo(201);
        assertThat(response.getBody().getSuccess()).isTrue();
        assertThat(response.getBody().getData().get(BODY)).isNotNull().isInstanceOf(CreateProductViewResponse.class);
        assertThat(response.getBody().getData().get(BODY).getId()).isNotNull().isInstanceOf(UUID.class);
        assertThat(response.getBody().getData().get(BODY).getActive()).isNotNull().isInstanceOf(Boolean.class);
        assertThat(response.getBody().getMessage()).isEqualTo(MessageUtil.getMessage(PRODUCT_CREATED_SUCCESSFULLY, Locale.forLanguageTag(EN_LOCALE), ""));
    }

    static Stream<Arguments> createProductThrowExceptionMethodSource() {
        return Stream.of(
                Arguments.of(
                        null,
                        UUID.fromString("01912c0f-2fcf-705b-ae59-d79d159f3ad0"),
                        CreateProductViewRequest.TypeEnum.SERVICE,
                        "name",
                        PRODUCT_NAME_IS_REQUIRED
                ),
                Arguments.of(
                        "Product.1",
                        null,
                        CreateProductViewRequest.TypeEnum.SERVICE,
                        "categoryId",
                        PRODUCT_CATEGORY_IS_REQUIRED
                ),
                Arguments.of(
                        "Product.1",
                        UUID.fromString("01912c0f-2fcf-705b-ae59-d79d159f3ad0"),
                        null,
                        "type",
                        PRODUCT_TYPE_IS_REQUIRED
                )
        );
    }

    @ParameterizedTest
    @MethodSource("createProductThrowExceptionMethodSource")
    void should_throw_exception_when_a_least_of_one_attribute_are_missing(
            String name,
            UUID categoryId,
            CreateProductViewRequest.TypeEnum typeRequest,
            String field,
            String message
    ) throws Exception {
        //Given
        CreateProductViewRequest createProductViewRequest = new CreateProductViewRequest()
                .name(name)
                .categoryId(categoryId)
                .type(typeRequest);

        Resource image = getImageResource("product_image.txt");

        MultiValueMap body = new LinkedMultiValueMap();

        body.add("image", image);
        body.add("createProductViewRequest", createProductViewRequest);

        HttpEntity<MultiValueMap<String, HttpEntity<?>>> request = new HttpEntity<>(body, getHttpHeaders());

        //Act
        ResponseEntity<ApiErrorResponse> response = restTemplate.postForEntity(new URI(BASE_URL), request, ApiErrorResponse.class);

        //Then
        assertThat(response.getStatusCode().value()).isEqualTo(400);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getSuccess()).isFalse();
        assertThat(response.getBody().getReason()).isEqualTo(MessageUtil.getMessage(VALIDATION_ERROR_OCCURRED_WHEN_PROCESSING_REQUEST, Locale.forLanguageTag(EN_LOCALE), ""));
        assertThat(response.getBody().getError()).isNotEmpty();
        assertThat(response.getBody().getError().getFirst().getField()).isEqualTo(field);
        assertThat(response.getBody().getError().getFirst().getMessage()).isEqualTo(MessageUtil.getMessage(message, Locale.forLanguageTag(EN_LOCALE), ""));


    }

    @Test
    void should_throw_exception_when_create_product_with_existing_name() throws Exception {
        //Given
        String name = "Product.2";
        UUID categoryId = UUID.fromString("01912c0f-2fcf-705b-ae59-d79d159f3ad0");

        CreateProductViewRequest createProductViewRequest = new CreateProductViewRequest()
                .name(name)
                .type(CreateProductViewRequest.TypeEnum.SERVICE)
                .categoryId(categoryId);

        Resource image = getImageResource("product_image.txt");

        MultiValueMap body = new LinkedMultiValueMap();

        body.add("image", image);
        body.add("createProductViewRequest", createProductViewRequest);

        HttpEntity<MultiValueMap<String, HttpEntity<?>>> request = new HttpEntity<>(body, getHttpHeaders());

        //Act
        ResponseEntity<ApiErrorResponse> response = restTemplate.postForEntity(new URI(BASE_URL), request, ApiErrorResponse.class);

        //Then
        assertThat(response.getStatusCode().value()).isEqualTo(409);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getSuccess()).isFalse();
        assertThat(response.getBody().getReason()).isEqualTo(MessageUtil.getMessage(PRODUCT_NAME_CONFLICT_EXCEPTION, Locale.forLanguageTag(EN_LOCALE), name));
    }

    @Test
    void should_throw_exception_when_create_product_with_un_know_category_id() throws Exception {
        //Given
        String name = "Product.1";
        UUID categoryId = UUID.randomUUID();

        CreateProductViewRequest createProductViewRequest = new CreateProductViewRequest()
                .name(name)
                .type(CreateProductViewRequest.TypeEnum.SERVICE)
                .categoryId(categoryId);

        Resource image = getImageResource("product_image.txt");

        MultiValueMap body = new LinkedMultiValueMap();

        body.add("image", image);
        body.add("createProductViewRequest", createProductViewRequest);

        HttpEntity<MultiValueMap<String, HttpEntity<?>>> request = new HttpEntity<>(body, getHttpHeaders());

        //Act
        ResponseEntity<ApiErrorResponse> response = restTemplate.postForEntity(new URI(BASE_URL), request, ApiErrorResponse.class);

        //Then
        assertThat(response.getStatusCode().value()).isEqualTo(404);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getSuccess()).isFalse();
        assertThat(response.getBody().getReason()).isEqualTo(MessageUtil.getMessage(PRODUCT_CATEGORY_NOT_FOUND_EXCEPTION, Locale.forLanguageTag(EN_LOCALE), categoryId.toString()));

    }

    @Test
    void should_throw_exception_when_create_product_with_un_know_purchase_or_stock_uom_id() throws Exception {
        //Given
        String name = "Product.1";
        UUID categoryId = UUID.fromString("01912c0f-2fcf-705b-ae59-d79d159f3ad0");

        UUID stockUomId = UUID.randomUUID();
        UUID purchaseUomId = UUID.randomUUID();
        CreateProductViewRequest createProductViewRequest = new CreateProductViewRequest()
                .name(name)
                .type(CreateProductViewRequest.TypeEnum.STOCK)
                .stockUomId(stockUomId)
                .purchaseUomId(purchaseUomId)
                .categoryId(categoryId);

        Resource image = getImageResource("product_image.txt");

        MultiValueMap body = new LinkedMultiValueMap();

        body.add("image", image);
        body.add("createProductViewRequest", createProductViewRequest);

        HttpEntity<MultiValueMap<String, HttpEntity<?>>> request = new HttpEntity<>(body, getHttpHeaders());

        //Act
        ResponseEntity<ApiErrorResponse> response = restTemplate.postForEntity(new URI(BASE_URL), request, ApiErrorResponse.class);

        //Then
        assertThat(response.getStatusCode().value()).isEqualTo(404);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getSuccess()).isFalse();
        assertThat(response.getBody().getReason()).isEqualTo(MessageUtil.getMessage(PRODUCT_UOM_NOT_FOUND_EXCEPTION, Locale.forLanguageTag(EN_LOCALE), stockUomId.toString()));
    }

    @Test
    void should_throw_exception_when_create_product_with_purchase_and_stock_uom_id_in_different_uom_category() throws Exception {
        //Given
        String name = "Product.6";
        UUID categoryId = UUID.fromString("01912c0f-2fcf-705b-ae59-d79d159f3ad0");
        String stockUnitName = "Unite";
        String purchaseUnitName = "Jour";

        UUID stockUomId = UUID.fromString("01912c2c-b81a-7245-bab1-aee9b97b2afb");
        UUID purchaseUomId = UUID.fromString("019199bf-2ea3-7ac1-8ad4-6dd062d5efec");
        CreateProductViewRequest createProductViewRequest = new CreateProductViewRequest()
                .name(name)
                .type(CreateProductViewRequest.TypeEnum.STOCK)
                .stockUomId(stockUomId)
                .purchaseUomId(purchaseUomId)
                .categoryId(categoryId);

        Resource image = getImageResource("product_image.txt");

        MultiValueMap body = new LinkedMultiValueMap();

        body.add("image", image);
        body.add("createProductViewRequest", createProductViewRequest);

        HttpEntity<MultiValueMap<String, HttpEntity<?>>> request = new HttpEntity<>(body, getHttpHeaders());

        //Act
        ResponseEntity<ApiErrorResponse> response = restTemplate.postForEntity(new URI(BASE_URL), request, ApiErrorResponse.class);

        //Then
        assertThat(response.getStatusCode().value()).isEqualTo(400);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getSuccess()).isFalse();
        assertThat(response.getBody().getReason()).isEqualTo(MessageUtil.getMessage(PRODUCT_STOCK_AND_PURCHASE_UOM_BAD_EXCEPTION, Locale.forLanguageTag(EN_LOCALE), stockUnitName, purchaseUnitName));
    }

    private String createProductViewAsString(@Nonnull CreateProductViewRequest createProductViewRequest) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(createProductViewRequest);
    }

    private Resource getImageResource(String image) throws URISyntaxException {
        return new ClassPathResource(image);
    }

    private static @NotNull HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAcceptLanguageAsLocales(List.of(Locale.forLanguageTag(EN_LOCALE)));
        headers.setAccept(List.of(APPLICATION_JSON));
        headers.setContentType(MULTIPART_FORM_DATA);
        return headers;
    }
}
