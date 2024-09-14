package cm.xenonbyte.farmbyte.bootstrap.catalog;

import cm.xenonbyte.farmbyte.bootstrap.DatabaseSetupExtension;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.ApiErrorResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.CreateProductViewApiResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.CreateProductViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.CreateProductViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.FindProductByIdViewApiResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.FindProductByIdViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.FindProductsPageInfoViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.FindProductsViewApiResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.SearchProductsPageInfoViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.SearchProductsViewApiResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.UpdateProductViewApiResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.UpdateProductViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.UpdateProductViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.product.ProductServiceRestApiAdapter;
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

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import static cm.xenonbyte.farmbyte.catalog.adapter.rest.api.product.ProductRestApi.PRODUCTS_FIND_SUCCESSFULLY;
import static cm.xenonbyte.farmbyte.catalog.adapter.rest.api.product.ProductRestApi.PRODUCT_CREATED_SUCCESSFULLY;
import static cm.xenonbyte.farmbyte.catalog.adapter.rest.api.product.ProductRestApi.PRODUCT_FIND_SUCCESSFULLY;
import static cm.xenonbyte.farmbyte.catalog.adapter.rest.api.product.ProductRestApi.PRODUCT_UPDATED_SUCCESSFULLY;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_CATEGORY_IS_REQUIRED;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_CATEGORY_NOT_FOUND_EXCEPTION;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_NAME_CONFLICT_EXCEPTION;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_NAME_IS_REQUIRED;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_NOT_FOUND_EXCEPTION;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_REFERENCE_CONFLICT_EXCEPTION;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_STOCK_AND_PURCHASE_UOM_BAD_EXCEPTION;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_TYPE_IS_REQUIRED;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_UOM_NOT_FOUND_EXCEPTION;
import static cm.xenonbyte.farmbyte.common.adapter.api.constant.CommonAdapterRestApi.BODY;
import static cm.xenonbyte.farmbyte.common.adapter.api.constant.CommonAdapterRestApi.EN_LOCALE;
import static cm.xenonbyte.farmbyte.common.adapter.api.constant.CommonAdapterRestApi.VALIDATION_ERROR_OCCURRED_WHEN_PROCESSING_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.PUT;
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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = {"classpath:application.yml", "classpath:application-test.yml"})
public class ProductRestApiIT {

    public static final String FIND_URL_PARAM = "?page={page}&size={size}&attribute={attribute}&direction={direction}";
    public static final String SEARCH_URL_PARAM = FIND_URL_PARAM + "&keyword={keyword}";

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

    @Nested
    class CreateProductRestApiIT {

        @Test
        void should_create_product() throws Exception {
            //Given
            String name = "Product.1";
            UUID categoryId = UUID.fromString("01912c0f-2fcf-705b-ae59-d79d159f3ad0");

            CreateProductViewRequest createProductViewRequest = getCreateProductViewRequest(name, categoryId);

            HttpEntity<MultiValueMap<String, HttpEntity<?>>> request = new HttpEntity<>(getMultiValueMap(createProductViewRequest, "createProductViewRequest"), getMultipartHttpHeaders());

            //Act
            ResponseEntity<CreateProductViewApiResponse> response = restTemplate.postForEntity(new URI(BASE_URL), request, CreateProductViewApiResponse.class);

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

            HttpEntity<MultiValueMap<String, HttpEntity<?>>> request = new HttpEntity<>(getMultiValueMap(createProductViewRequest, "createProductViewRequest"), getMultipartHttpHeaders());

            //Act
            ResponseEntity<ApiErrorResponse> response = restTemplate.postForEntity(new URI(BASE_URL), request, ApiErrorResponse.class);

            //Then
            assertThat(response.getStatusCode().value()).isEqualTo(400);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getSuccess()).isFalse();
            assertThat(response.getBody().getError()).isNotEmpty();
            assertThat(response.getBody().getStatus()).isEqualTo("BAD_REQUEST");
            assertThat(response.getBody().getError().getFirst().getField()).isEqualTo(field);
            assertThat(response.getBody().getError().getFirst().getMessage()).isEqualTo(MessageUtil.getMessage(message, Locale.forLanguageTag(EN_LOCALE), ""));
            assertThat(response.getBody().getReason()).isEqualTo(MessageUtil.getMessage(VALIDATION_ERROR_OCCURRED_WHEN_PROCESSING_REQUEST, Locale.forLanguageTag(EN_LOCALE), ""));


        }

        @Test
        void should_throw_exception_when_create_product_with_existing_name() throws Exception {
            //Given
            String name = "Product.2";
            UUID categoryId = UUID.fromString("01912c0f-2fcf-705b-ae59-d79d159f3ad0");

            CreateProductViewRequest createProductViewRequest = getCreateProductViewRequest(name, categoryId);

            HttpEntity<MultiValueMap<String, HttpEntity<?>>> request = new HttpEntity<>(getMultiValueMap(createProductViewRequest, "createProductViewRequest"), getMultipartHttpHeaders());

            //Act
            ResponseEntity<ApiErrorResponse> response = restTemplate.postForEntity(new URI(BASE_URL), request, ApiErrorResponse.class);

            //Then
            assertThat(response.getStatusCode().value()).isEqualTo(409);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getSuccess()).isFalse();
            assertThat(response.getBody().getStatus()).isEqualTo("CONFLICT");
            assertThat(response.getBody().getReason()).isEqualTo(MessageUtil.getMessage(PRODUCT_NAME_CONFLICT_EXCEPTION, Locale.forLanguageTag(EN_LOCALE), name));
        }

        @Test
        void should_throw_exception_when_create_product_with_un_know_category_id() throws Exception {
            //Given
            String name = "Product.1";
            UUID categoryId = UUID.randomUUID();

            CreateProductViewRequest createProductViewRequest = getCreateProductViewRequest(name, categoryId);

            HttpEntity<MultiValueMap<String, HttpEntity<?>>> request = new HttpEntity<>(getMultiValueMap(createProductViewRequest, "createProductViewRequest"), getMultipartHttpHeaders());

            //Act
            ResponseEntity<ApiErrorResponse> response = restTemplate.postForEntity(new URI(BASE_URL), request, ApiErrorResponse.class);

            //Then
            assertThat(response.getStatusCode().value()).isEqualTo(404);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getSuccess()).isFalse();
            assertThat(response.getBody().getStatus()).isEqualTo("NOT_FOUND");
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

            HttpEntity<MultiValueMap<String, HttpEntity<?>>> request = new HttpEntity<>(getMultiValueMap(createProductViewRequest, "createProductViewRequest"), getMultipartHttpHeaders());

            //Act
            ResponseEntity<ApiErrorResponse> response = restTemplate.postForEntity(new URI(BASE_URL), request, ApiErrorResponse.class);

            //Then
            assertThat(response.getStatusCode().value()).isEqualTo(404);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getSuccess()).isFalse();
            assertThat(response.getBody().getStatus()).isEqualTo("NOT_FOUND");
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

            HttpEntity<MultiValueMap<String, HttpEntity<?>>> request = new HttpEntity<>(getMultiValueMap(createProductViewRequest, "createProductViewRequest"), getMultipartHttpHeaders());

            //Act
            ResponseEntity<ApiErrorResponse> response = restTemplate.postForEntity(new URI(BASE_URL), request, ApiErrorResponse.class);

            //Then
            assertThat(response.getStatusCode().value()).isEqualTo(400);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getSuccess()).isFalse();
            assertThat(response.getBody().getStatus()).isEqualTo("BAD_REQUEST");
            assertThat(response.getBody().getReason()).isEqualTo(MessageUtil.getMessage(PRODUCT_STOCK_AND_PURCHASE_UOM_BAD_EXCEPTION, Locale.forLanguageTag(EN_LOCALE), stockUnitName, purchaseUnitName));
        }
    }

    @Nested
    class FindProductByIdRestApiIT {
        @Test
        void should_success_when_find_product_with_existing_id() {
            //Given
            String productIdUUID = "0191bda4-65e4-73a8-8291-b2870753ad00";
            HttpEntity<Object> request = new HttpEntity<>(getJsonHttpHeaders());

            FindProductByIdViewResponse findProductByIdViewResponse = new FindProductByIdViewResponse()
                    .id(UUID.fromString(productIdUUID))
                    .name("Mac Book Pro M2 Max")
                    .categoryId(UUID.fromString("01912c0f-2fcf-705b-ae59-d79d159f3ad0"))
                    .active(true)
                    .type(FindProductByIdViewResponse.TypeEnum.STOCK)
                    .reference("3254521")
                    .sellable(true)
                    .purchasable(true)
                    .purchasePrice(new BigDecimal("36000"))
                    .salePrice(new BigDecimal("45000"))
                    .stockUomId(UUID.fromString("01912c2c-b81a-7245-bab1-aee9b97b2afb"))
                    .purchaseUomId(UUID.fromString("01912c2e-b52d-7b85-9c12-85af49fc7798"))
                    .filename("products/product.png");

            //Act
            ResponseEntity<FindProductByIdViewApiResponse> response = restTemplate.exchange(
                    BASE_URL + "/" + productIdUUID, GET, request, FindProductByIdViewApiResponse.class);

            //Then
            assertThat(response.getStatusCode().value()).isEqualTo(200);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getStatus()).isEqualTo("OK");
            assertThat(response.getBody().getSuccess()).isTrue();
            assertThat(response.getBody().getMessage()).isEqualTo(MessageUtil.getMessage(PRODUCT_FIND_SUCCESSFULLY, Locale.forLanguageTag(EN_LOCALE), ""));
            assertThat(response.getBody().getData()).isNotEmpty();
            assertThat(response.getBody().getData().get(BODY).getId().toString()).isEqualTo(findProductByIdViewResponse.getId().toString());

        }

        @Test
        void should_fail_when_find_product_with_non_existing_id() {
            //Given
            String productIdUUID = "0191bf19-fb0a-7e64-88fe-fcbb4e81e8a6";
            HttpEntity<Object> request = new HttpEntity<>(getJsonHttpHeaders());

            //Act
            ResponseEntity<ApiErrorResponse> response = restTemplate.exchange(
                    BASE_URL + "/" + productIdUUID, GET, request, ApiErrorResponse.class);

            //Then
            assertThat(response.getStatusCode().value()).isEqualTo(404);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getStatus()).isEqualTo("NOT_FOUND");
            assertThat(response.getBody().getSuccess()).isFalse();
            assertThat(response.getBody().getReason()).isEqualTo(MessageUtil.getMessage(PRODUCT_NOT_FOUND_EXCEPTION, Locale.forLanguageTag(EN_LOCALE), productIdUUID));
        }
    }

    @Nested
    class FindProductsRestApiIT{
        @Test
        void should_success_when_find_products() {
            //Given
            HttpHeaders httpHeaders = getJsonHttpHeaders();
            Map<String, String> params = new LinkedHashMap<>();
            params.put("page", "0");
            params.put("size", "2");
            params.put("attribute", "name");
            params.put("direction", "DSC");
            HttpEntity<Object> request = new HttpEntity<>(httpHeaders);

            //Act
            ResponseEntity<FindProductsViewApiResponse> response = restTemplate.exchange(BASE_URL + FIND_URL_PARAM, GET, request, FindProductsViewApiResponse.class, params);

            //Then
            assertThat(response.getStatusCode().value()).isEqualTo(200);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getStatus()).isEqualTo("OK");
            assertThat(response.getBody().getSuccess()).isTrue();
            assertThat(response.getBody().getMessage()).isEqualTo(MessageUtil.getMessage(PRODUCTS_FIND_SUCCESSFULLY, Locale.forLanguageTag(EN_LOCALE), ""));
            assertThat(response.getBody().getData().get(BODY)).isInstanceOf(FindProductsPageInfoViewResponse.class);
            assertThat(response.getBody().getData().get(BODY).getContent().size()).isGreaterThan(0);
        }
    }

    @Nested
    class SearchProductsRestApiIT{
        @Test
        void should_success_when_find_products() {
            //Given
            HttpHeaders httpHeaders = getJsonHttpHeaders();

            Map<String, String> params = new LinkedHashMap<>();
            params.put("page", "0");
            params.put("size", "2");
            params.put("attribute", "name");
            params.put("direction", "DSC");
            params.put("keyword", "p");
            HttpEntity<Object> request = new HttpEntity<>(httpHeaders);

            //Act
            ResponseEntity<SearchProductsViewApiResponse> response = restTemplate.exchange(BASE_URL + "/search" + SEARCH_URL_PARAM , GET, request, SearchProductsViewApiResponse.class, params);

            //Then
            assertThat(response.getStatusCode().value()).isEqualTo(200);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getStatus()).isEqualTo("OK");
            assertThat(response.getBody().getSuccess()).isTrue();
            assertThat(response.getBody().getMessage()).isEqualTo(MessageUtil.getMessage(PRODUCTS_FIND_SUCCESSFULLY, Locale.forLanguageTag(EN_LOCALE), ""));
            assertThat(response.getBody().getData().get(BODY)).isInstanceOf(SearchProductsPageInfoViewResponse.class);
            assertThat(response.getBody().getData().get(BODY).getContent().size()).isGreaterThan(0);
        }
    }

    @Nested
    class UpdateProductRestApiIT{

        String filenameToUpdate = System.getProperty("user.home") + "/farmbyte/products/202409112207457910200_product_image.txt";

        @Test
        void should_success_when_update_product() throws URISyntaxException {
            //Given
            String name = "IPhone 15 Pro Max";
            UUID categoryId = UUID.fromString("01912c0f-2fcf-705b-ae59-d79d159f3ad0");
            UUID productIdUUID = UUID.fromString("0191bda9-4b9f-7111-8bac-de8cc467715a");

            UpdateProductViewRequest updateProductViewRequest = new UpdateProductViewRequest()
                    .id(productIdUUID)
                    .name(name)
                    .type(UpdateProductViewRequest.TypeEnum.SERVICE)
                    .categoryId(categoryId)
                    .purchasePrice(BigDecimal.ZERO)
                    .stockUomId(null)
                    .purchaseUomId(null)
                    .salePrice(BigDecimal.ZERO)
                    .purchasable(false)
                    .sellable(false)
                    .filename(filenameToUpdate)
                    .active(true);

            HttpEntity<MultiValueMap<String, HttpEntity<?>>> request = new HttpEntity<>(getMultiValueMap(updateProductViewRequest, "updateProductViewRequest"), getMultipartHttpHeaders());

            //Act
            ResponseEntity<UpdateProductViewApiResponse> response = restTemplate.exchange(BASE_URL + "/" + productIdUUID, PUT, request, UpdateProductViewApiResponse.class);

            //Then
            assertThat(response.getStatusCode().value()).isEqualTo(200);
            assertThat(response.getBody().getSuccess()).isTrue();
            assertThat(response.getBody().getData().get(BODY)).isNotNull().isInstanceOf(UpdateProductViewResponse.class);
            assertThat(response.getBody().getData().get(BODY).getId()).isNotNull().isInstanceOf(UUID.class);
            assertThat(response.getBody().getData().get(BODY).getActive()).isNotNull().isInstanceOf(Boolean.class);
            assertThat(response.getBody().getMessage()).isEqualTo(MessageUtil.getMessage(PRODUCT_UPDATED_SUCCESSFULLY, Locale.forLanguageTag(EN_LOCALE), ""));
        }

        @Test
        void should_fail_when_update_product_with_not_found_product_id() throws URISyntaxException {
            //Given
            String name = "IPhone 15 Pro Max";
            UUID categoryId = UUID.fromString("01912c0f-2fcf-705b-ae59-d79d159f3ad0");
            UUID productIdUUID = UUID.fromString("0191e2c3-62e9-75eb-a3fd-b3f44cd436ba");

            UpdateProductViewRequest updateProductViewRequest = new UpdateProductViewRequest()
                    .id(productIdUUID)
                    .name(name)
                    .type(UpdateProductViewRequest.TypeEnum.SERVICE)
                    .categoryId(categoryId)
                    .purchasePrice(BigDecimal.ZERO)
                    .stockUomId(null)
                    .purchaseUomId(null)
                    .salePrice(BigDecimal.ZERO)
                    .purchasable(false)
                    .sellable(false)
                    .filename(filenameToUpdate)
                    .active(true);

            HttpEntity<MultiValueMap<String, HttpEntity<?>>> request = new HttpEntity<>(getMultiValueMap(updateProductViewRequest, "updateProductViewRequest"), getMultipartHttpHeaders());

            //Act
            ResponseEntity<ApiErrorResponse> response = restTemplate.exchange(BASE_URL + "/" + productIdUUID, PUT, request, ApiErrorResponse.class);

            //Then
            assertThat(response.getStatusCode().value()).isEqualTo(404);
            assertThat(response.getBody().getSuccess()).isFalse();
            assertThat(response.getBody().getStatus()).isEqualTo("NOT_FOUND");
            assertThat(response.getBody().getReason()).isEqualTo(MessageUtil.getMessage(PRODUCT_NOT_FOUND_EXCEPTION, Locale.forLanguageTag(EN_LOCALE), productIdUUID.toString()));
        }

        @Test
        void should_fail_when_update_product_with_not_found_product_category_id() throws URISyntaxException {
            //Given
            String name = "IPhone 15 Pro Max";
            UUID categoryId = UUID.fromString("0191e2ca-b831-7a1c-b147-f4138aed0772");
            UUID productIdUUID = UUID.fromString("0191bda9-4b9f-7111-8bac-de8cc467715a");

            UpdateProductViewRequest updateProductViewRequest = new UpdateProductViewRequest()
                    .id(productIdUUID)
                    .name(name)
                    .type(UpdateProductViewRequest.TypeEnum.SERVICE)
                    .categoryId(categoryId)
                    .purchasePrice(BigDecimal.ZERO)
                    .stockUomId(null)
                    .purchaseUomId(null)
                    .salePrice(BigDecimal.ZERO)
                    .purchasable(false)
                    .sellable(false)
                    .filename(filenameToUpdate)
                    .active(true);

            HttpEntity<MultiValueMap<String, HttpEntity<?>>> request = new HttpEntity<>(getMultiValueMap(updateProductViewRequest, "updateProductViewRequest"), getMultipartHttpHeaders());

            //Act
            ResponseEntity<ApiErrorResponse> response = restTemplate.exchange(BASE_URL + "/" + productIdUUID, PUT, request, ApiErrorResponse.class);

            //Then
            assertThat(response.getStatusCode().value()).isEqualTo(404);
            assertThat(response.getBody().getSuccess()).isFalse();
            assertThat(response.getBody().getStatus()).isEqualTo("NOT_FOUND");
            assertThat(response.getBody().getReason()).isEqualTo(MessageUtil.getMessage(PRODUCT_CATEGORY_NOT_FOUND_EXCEPTION, Locale.forLanguageTag(EN_LOCALE), categoryId.toString()));
        }

        @Test
        void should_fail_when_update_product_with_duplicate_name() throws URISyntaxException {
            //Given
            String name = "Product.2";
            UUID categoryId = UUID.fromString("01912c0f-2fcf-705b-ae59-d79d159f3ad0");
            UUID productIdUUID = UUID.fromString("0191bda9-4b9f-7111-8bac-de8cc467715a");

            UpdateProductViewRequest updateProductViewRequest = new UpdateProductViewRequest()
                    .id(productIdUUID)
                    .name(name)
                    .type(UpdateProductViewRequest.TypeEnum.SERVICE)
                    .categoryId(categoryId)
                    .purchasePrice(BigDecimal.ZERO)
                    .stockUomId(null)
                    .purchaseUomId(null)
                    .salePrice(BigDecimal.ZERO)
                    .purchasable(false)
                    .sellable(false)
                    .filename(filenameToUpdate)
                    .active(true);

            HttpEntity<MultiValueMap<String, HttpEntity<?>>> request = new HttpEntity<>(getMultiValueMap(updateProductViewRequest, "updateProductViewRequest"), getMultipartHttpHeaders());

            //Act
            ResponseEntity<ApiErrorResponse> response = restTemplate.exchange(BASE_URL + "/" + productIdUUID, PUT, request, ApiErrorResponse.class);

            //Then
            assertThat(response.getStatusCode().value()).isEqualTo(409);
            assertThat(response.getBody().getSuccess()).isFalse();
            assertThat(response.getBody().getStatus()).isEqualTo("CONFLICT");
            assertThat(response.getBody().getReason()).isEqualTo(MessageUtil.getMessage(PRODUCT_NAME_CONFLICT_EXCEPTION, Locale.forLanguageTag(EN_LOCALE), name));
        }

        @Test
        void should_fail_when_update_product_with_duplicate_reference() throws URISyntaxException {
            //Given
            String name = "New HP Prolian Pro";
            UUID categoryId = UUID.fromString("01912c0f-2fcf-705b-ae59-d79d159f3ad0");
            UUID productIdUUID = UUID.fromString("0191bda9-4b9f-7111-8bac-de8cc467715a");
            String reference = "3254521";

            UpdateProductViewRequest updateProductViewRequest = new UpdateProductViewRequest()
                    .id(productIdUUID)
                    .name(name)
                    .type(UpdateProductViewRequest.TypeEnum.SERVICE)
                    .categoryId(categoryId)
                    .purchasePrice(BigDecimal.ZERO)
                    .reference(reference)
                    .stockUomId(null)
                    .purchaseUomId(null)
                    .salePrice(BigDecimal.ZERO)
                    .purchasable(false)
                    .sellable(false)
                    .filename(filenameToUpdate)
                    .active(true);

            HttpEntity<MultiValueMap<String, HttpEntity<?>>> request = new HttpEntity<>(getMultiValueMap(updateProductViewRequest, "updateProductViewRequest"), getMultipartHttpHeaders());

            //Act
            ResponseEntity<ApiErrorResponse> response = restTemplate.exchange(BASE_URL + "/" + productIdUUID, PUT, request, ApiErrorResponse.class);

            //Then
            assertThat(response.getStatusCode().value()).isEqualTo(409);
            assertThat(response.getBody().getSuccess()).isFalse();
            assertThat(response.getBody().getStatus()).isEqualTo("CONFLICT");
            assertThat(response.getBody().getReason()).isEqualTo(MessageUtil.getMessage(PRODUCT_REFERENCE_CONFLICT_EXCEPTION, Locale.forLanguageTag(EN_LOCALE), reference));
        }

        @Test
        void should_fail_when_update_product_with_not_found_uom_id() throws URISyntaxException {
            //Given
            String name = "New HP Prolian Pro";
            UUID categoryId = UUID.fromString("01912c0f-2fcf-705b-ae59-d79d159f3ad0");
            UUID productIdUUID = UUID.fromString("0191bda9-4b9f-7111-8bac-de8cc467715a");
            String reference = "9282829598";

            UUID uomIdUUID = UUID.fromString("0191e2d0-1411-7f88-b925-cf1883c4a54d");

            UpdateProductViewRequest updateProductViewRequest = new UpdateProductViewRequest()
                    .id(productIdUUID)
                    .name(name)
                    .type(UpdateProductViewRequest.TypeEnum.STOCK)
                    .categoryId(categoryId)
                    .purchasePrice(BigDecimal.ZERO)
                    .reference(reference)
                    .stockUomId(uomIdUUID)
                    .purchaseUomId(uomIdUUID)
                    .salePrice(BigDecimal.ZERO)
                    .purchasable(false)
                    .sellable(false)
                    .filename(filenameToUpdate)
                    .active(true);

            HttpEntity<MultiValueMap<String, HttpEntity<?>>> request = new HttpEntity<>(getMultiValueMap(updateProductViewRequest, "updateProductViewRequest"), getMultipartHttpHeaders());

            //Act
            ResponseEntity<ApiErrorResponse> response = restTemplate.exchange(BASE_URL + "/" + productIdUUID, PUT, request, ApiErrorResponse.class);

            //Then
            assertThat(response.getStatusCode().value()).isEqualTo(404);
            assertThat(response.getBody().getSuccess()).isFalse();
            assertThat(response.getBody().getStatus()).isEqualTo("NOT_FOUND");
            assertThat(response.getBody().getReason()).isEqualTo(MessageUtil.getMessage(PRODUCT_UOM_NOT_FOUND_EXCEPTION, Locale.forLanguageTag(EN_LOCALE), uomIdUUID.toString()));
        }

        @Test
        void should_fail_when_update_product_with_invalid_stock_uom_and_purchase_uom_id() throws URISyntaxException {
            //Given
            String name = "New HP Prolian Pro";
            UUID categoryId = UUID.fromString("01912c0f-2fcf-705b-ae59-d79d159f3ad0");
            UUID productIdUUID = UUID.fromString("0191bda9-4b9f-7111-8bac-de8cc467715a");
            String reference = "9282829598";

            UUID stockUomId = UUID.fromString("019199bf-2ea3-7ac1-8ad4-6dd062d5efec");
            UUID purchaseUomId = UUID.fromString("01912c2c-b81a-7245-bab1-aee9b97b2afb");

            UpdateProductViewRequest updateProductViewRequest = new UpdateProductViewRequest()
                    .id(productIdUUID)
                    .name(name)
                    .type(UpdateProductViewRequest.TypeEnum.STOCK)
                    .categoryId(categoryId)
                    .purchasePrice(BigDecimal.ZERO)
                    .reference(reference)
                    .stockUomId(stockUomId)
                    .purchaseUomId(purchaseUomId)
                    .salePrice(BigDecimal.ZERO)
                    .purchasable(false)
                    .sellable(false)
                    .filename(filenameToUpdate)
                    .active(true);

            HttpEntity<MultiValueMap<String, HttpEntity<?>>> request = new HttpEntity<>(getMultiValueMap(updateProductViewRequest, "updateProductViewRequest"), getMultipartHttpHeaders());

            //Act
            ResponseEntity<ApiErrorResponse> response = restTemplate.exchange(BASE_URL + "/" + productIdUUID, PUT, request, ApiErrorResponse.class);

            //Then
            assertThat(response.getStatusCode().value()).isEqualTo(400);
            assertThat(response.getBody().getSuccess()).isFalse();
            assertThat(response.getBody().getStatus()).isEqualTo("BAD_REQUEST");
            assertThat(response.getBody().getReason()).isEqualTo(MessageUtil.getMessage(PRODUCT_STOCK_AND_PURCHASE_UOM_BAD_EXCEPTION, Locale.forLanguageTag(EN_LOCALE), "Jour", "Unite"));
        }
    }

    private static CreateProductViewRequest getCreateProductViewRequest(String name, UUID categoryId) {
        return new CreateProductViewRequest()
                .name(name)
                .type(CreateProductViewRequest.TypeEnum.SERVICE)
                .categoryId(categoryId);
    }

    private @Nonnull MultiValueMap getMultiValueMap(Object object, String attribute) throws URISyntaxException {
        Resource image = getImageResource("product_image.txt");

        MultiValueMap body = new LinkedMultiValueMap();

        body.add("image", image);
        body.add(attribute, object);
        return body;
    }

    private Resource getImageResource(String image) throws URISyntaxException {
        return new ClassPathResource(image);
    }

    private static @Nonnull HttpHeaders getMultipartHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAcceptLanguageAsLocales(List.of(Locale.forLanguageTag(EN_LOCALE)));
        headers.setAccept(List.of(APPLICATION_JSON));
        headers.setContentType(MULTIPART_FORM_DATA);
        return headers;
    }

    private static @Nonnull HttpHeaders getJsonHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAcceptLanguageAsLocales(List.of(Locale.forLanguageTag(EN_LOCALE)));
        headers.setAccept(List.of(APPLICATION_JSON));
        headers.setContentType(APPLICATION_JSON);
        return headers;
    }
}
