package cm.xenonbyte.farmbyte.bootstrap.catalog;

import cm.xenonbyte.farmbyte.bootstrap.DatabaseSetupExtension;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.productcategory.view.ApiErrorResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.productcategory.view.CreateProductCategoryViewApiResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.productcategory.view.CreateProductCategoryViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.productcategory.view.CreateProductCategoryViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.productcategory.view.FindProductCategoriesPageInfoViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.productcategory.view.FindProductCategoriesViewApiResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.productcategory.view.FindProductCategoryByIdViewApiResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.productcategory.view.FindProductCategoryByIdViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.productcategory.view.SearchProductCategoriesPageInfoViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.productcategory.view.SearchProductCategoriesViewApiResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.product.ProductCategoryServiceRestApiAdapter;
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

import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Stream;

import static cm.xenonbyte.farmbyte.catalog.adapter.rest.api.product.ProductCategoryRestApi.PRODUCT_CATEGORIES_FIND_SUCCESSFULLY;
import static cm.xenonbyte.farmbyte.catalog.adapter.rest.api.product.ProductCategoryRestApi.PRODUCT_CATEGORY_CREATED_SUCCESSFULLY;
import static cm.xenonbyte.farmbyte.catalog.adapter.rest.api.product.ProductCategoryRestApi.PRODUCT_CATEGORY_FIND_SUCCESSFULLY;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_PARENT_CATEGORY_WITH_ID_NOT_FOUND_EXCEPTION;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_CATEGORY_NAME_CONFLICT_EXCEPTION;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_CATEGORY_NOT_FOUND_EXCEPTION;
import static cm.xenonbyte.farmbyte.common.adapter.api.constant.CommonAdapterRestApi.BODY;
import static cm.xenonbyte.farmbyte.common.adapter.api.constant.CommonAdapterRestApi.EN_LOCALE;
import static cm.xenonbyte.farmbyte.common.adapter.api.constant.CommonAdapterRestApi.VALIDATION_ERROR_OCCURRED_WHEN_PROCESSING_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
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
public class ProductCategoryRestApiIT {

    @LocalServerPort
    private int port;

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    ProductCategoryServiceRestApiAdapter productCategoryServiceRestApiAdapter;

    String BASE_URL;

    @BeforeEach
    void setUp() {
        BASE_URL = "http://localhost:" + port + "/api/v1/catalog/product-categories";
    }


    @Nested
    class CreateProductCategoryRestApiIT {

        static Stream<Arguments> createProductMethodSourceArgs() {
            return Stream.of(
                    Arguments.of(
                            "Unite",
                            null
                    ),
                    Arguments.of(
                            "Law Raw Material",
                            UUID.fromString("01912c0f-2fcf-705b-ae59-d79d159f3ad0")
                    )
            );
        }

        @ParameterizedTest
        @MethodSource("createProductMethodSourceArgs")
        void should_create_root_or_child_product_category(
                String nameValue,
                UUID parentProductCategoryUUID
        ) throws Exception {
            //Given
            CreateProductCategoryViewRequest createProductCategoryViewRequest = getCreateProductCategoryViewRequest(nameValue, parentProductCategoryUUID);
            HttpEntity<CreateProductCategoryViewRequest> request = new HttpEntity<>(createProductCategoryViewRequest, getHttpHeaders());

            //Act
            ResponseEntity<CreateProductCategoryViewApiResponse> response = restTemplate.exchange(BASE_URL , POST, request, CreateProductCategoryViewApiResponse.class);

            //Then
            assertThat(response.getStatusCode().value()).isEqualTo(201);
            assertThat(response.getBody().getSuccess()).isTrue();
            assertThat(response.getBody().getData().get(BODY)).isNotNull().isInstanceOf(CreateProductCategoryViewResponse.class);
            assertThat(response.getBody().getData().get(BODY).getId()).isNotNull().isInstanceOf(UUID.class);
            assertThat(response.getBody().getData().get(BODY).getActive()).isNotNull().isInstanceOf(Boolean.class);
            assertThat(response.getBody().getMessage()).isEqualTo(MessageUtil.getMessage(PRODUCT_CATEGORY_CREATED_SUCCESSFULLY, Locale.forLanguageTag(EN_LOCALE), ""));

        }

        static Stream<Arguments> createProductMethodThrowExceptionSourceArgs() {
            return Stream.of(
                    Arguments.of(
                            "Raw of material",
                            UUID.fromString("01912c0f-2fcf-705b-ae59-d79d159f3ad0"),
                            PRODUCT_CATEGORY_NAME_CONFLICT_EXCEPTION,
                            "Raw of material",
                            409

                    ),
                    Arguments.of(
                            "Raw of material",
                            UUID.fromString("019156f3-0db6-794e-bfe0-f371636cd410"),
                            PRODUCT_PARENT_CATEGORY_WITH_ID_NOT_FOUND_EXCEPTION,
                            "019156f3-0db6-794e-bfe0-f371636cd410",
                            404
                    )
            );
        }

        @ParameterizedTest
        @MethodSource("createProductMethodThrowExceptionSourceArgs")
        void should_throw_exception_when_create_product_category_fail(
                String nameValue,
                UUID parentProductCategoryUUID,
                String exceptionMessage,
                String args,
                int code

        ) throws Exception {
            CreateProductCategoryViewRequest createProductCategoryViewRequest = getCreateProductCategoryViewRequest(nameValue, parentProductCategoryUUID);
            HttpEntity<CreateProductCategoryViewRequest> request = new HttpEntity<>(createProductCategoryViewRequest, getHttpHeaders());

            //Act
            ResponseEntity<ApiErrorResponse> response = restTemplate.exchange(BASE_URL , POST, request, ApiErrorResponse.class);

            //Then
            assertThat(response.getStatusCode().value()).isEqualTo(code);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getSuccess()).isFalse();
            assertThat(response.getBody().getReason()).isEqualTo(MessageUtil.getMessage(exceptionMessage, Locale.forLanguageTag(EN_LOCALE), args));
        }

        @Test
        void should_throw_exception_when_create_product_category_without_required_field() throws Exception {
            //Given
            CreateProductCategoryViewRequest createProductCategoryViewRequest = getCreateProductCategoryViewRequest(null, null);

            HttpEntity<CreateProductCategoryViewRequest> request = new HttpEntity<>(createProductCategoryViewRequest, getHttpHeaders());

            //Act
            ResponseEntity<ApiErrorResponse> response = restTemplate.exchange(BASE_URL , POST, request, ApiErrorResponse.class);

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
    class FindProductCategoryByIdRestApiIT {

        @Test
        void should_success_when_find_product_category_with_existing_id() {
            //Given
            String productCategoryUUID = "01912c0f-2fcf-705b-ae59-d79d159f3ad0";
            HttpEntity<Object> request = new HttpEntity<>(getHttpHeaders());
            FindProductCategoryByIdViewResponse findProductCategoryByIdViewResponse = new FindProductCategoryByIdViewResponse()
                    .id(UUID.fromString(productCategoryUUID))
                    .name("Raw of material")
                    .active(true);

            //Act
            ResponseEntity<FindProductCategoryByIdViewApiResponse> response = restTemplate.exchange(
                    BASE_URL + "/" + productCategoryUUID, GET, request, FindProductCategoryByIdViewApiResponse.class);

            //Then
            assertThat(response.getStatusCode().value()).isEqualTo(200);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getStatus()).isEqualTo("OK");
            assertThat(response.getBody().getSuccess()).isTrue();
            assertThat(response.getBody().getMessage()).isEqualTo(MessageUtil.getMessage(PRODUCT_CATEGORY_FIND_SUCCESSFULLY, Locale.forLanguageTag(EN_LOCALE), ""));
            assertThat(response.getBody().getData()).isNotEmpty();
            assertThat(response.getBody().getData().get(BODY)).isEqualTo(findProductCategoryByIdViewResponse);
        }

        @Test
        void should_fail_when_find_product_category_with_non_existing_id() {
            //Given
            String productCategoryUUID = "0191b8db-b2bc-7d2c-8ac4-73d3b7d50337";
            HttpEntity<Object> request = new HttpEntity<>(getHttpHeaders());

            //Act
            ResponseEntity<ApiErrorResponse> response = restTemplate.exchange(
                    BASE_URL + "/" + productCategoryUUID, GET, request, ApiErrorResponse.class);

            //Then
            assertThat(response.getStatusCode().value()).isEqualTo(404);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getStatus()).isEqualTo("NOT_FOUND");
            assertThat(response.getBody().getSuccess()).isFalse();
            assertThat(response.getBody().getReason()).isEqualTo(MessageUtil.getMessage(PRODUCT_CATEGORY_NOT_FOUND_EXCEPTION, Locale.forLanguageTag(EN_LOCALE), productCategoryUUID));
        }
    }

    @Nested
    class FindProductCategoriesRestApiIT {

        @Test
        void should_success_when_find_product_categories() {
            //Given

            HttpHeaders httpHeaders = getHttpHeaders();
            httpHeaders.set("page", "0");
            httpHeaders.set("size", "2");
            httpHeaders.set("attribute", "name");
            httpHeaders.set("direction", "DSC");
            HttpEntity<Object> request = new HttpEntity<>(httpHeaders);

            //Act
            ResponseEntity<FindProductCategoriesViewApiResponse> response = restTemplate.exchange(BASE_URL , GET, request, FindProductCategoriesViewApiResponse.class);

            //Then
            assertThat(response.getStatusCode().value()).isEqualTo(200);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getStatus()).isEqualTo("OK");
            assertThat(response.getBody().getSuccess()).isTrue();
            assertThat(response.getBody().getMessage()).isEqualTo(MessageUtil.getMessage(PRODUCT_CATEGORIES_FIND_SUCCESSFULLY, Locale.forLanguageTag(EN_LOCALE), ""));
            assertThat(response.getBody().getData().get(BODY)).isInstanceOf(FindProductCategoriesPageInfoViewResponse.class);
            assertThat(response.getBody().getData().get(BODY).getContent().size()).isGreaterThan(0);
        }
    }

    @Nested
    class SearchProductCategoriesRestApiIT {

        @Test
        void should_success_when_search_uom_categories_with_keyword() {
            //Given

            HttpHeaders httpHeaders = getHttpHeaders();
            httpHeaders.set("page", "0");
            httpHeaders.set("size", "2");
            httpHeaders.set("attribute", "name");
            httpHeaders.set("direction", "DSC");
            httpHeaders.set("keyword", "n");
            HttpEntity<Object> request = new HttpEntity<>(httpHeaders);

            //Act
            ResponseEntity<SearchProductCategoriesViewApiResponse> response = restTemplate.exchange(BASE_URL + "/search" , GET, request, SearchProductCategoriesViewApiResponse.class);

            //Then
            assertThat(response.getStatusCode().value()).isEqualTo(200);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getStatus()).isEqualTo("OK");
            assertThat(response.getBody().getSuccess()).isTrue();
            assertThat(response.getBody().getMessage()).isEqualTo(MessageUtil.getMessage(PRODUCT_CATEGORIES_FIND_SUCCESSFULLY, Locale.forLanguageTag(EN_LOCALE), ""));
            assertThat(response.getBody().getData().get(BODY)).isInstanceOf(SearchProductCategoriesPageInfoViewResponse.class);
            assertThat(response.getBody().getData().get(BODY).getContent().size()).isGreaterThan(0);
        }
    }

    private static CreateProductCategoryViewRequest getCreateProductCategoryViewRequest(String nameValue, UUID parentProductCategoryUUID) {
        return new CreateProductCategoryViewRequest()
                .name(nameValue)
                .parentProductCategoryId(parentProductCategoryUUID);
    }

    private static @Nonnull HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAcceptLanguageAsLocales(List.of(Locale.forLanguageTag(EN_LOCALE)));
        headers.setAccept(List.of(APPLICATION_JSON));
        headers.setContentType(APPLICATION_JSON);
        return headers;
    }
}
