package cm.xenonbyte.farmbyte.catalog.adapter.rest.api.product;

import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.RestApiBeanConfigTest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.productcategory.view.CreateProductCategoryViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.productcategory.view.CreateProductCategoryViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.productcategory.view.FindProductCategoryByIdViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.productcategory.view.FindProductCategoriesPageInfoViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.productcategory.view.FindProductCategoriesViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.productcategory.view.SearchProductCategoriesPageInfoViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.productcategory.view.SearchProductCategoriesViewResponse;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductParentCategoryNotFoundException;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductCategoryNameConflictException;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductCategoryNotFoundException;
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
import org.springframework.test.web.servlet.ResultMatcher;

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
 * @since 15/08/2024
 */
@ActiveProfiles("test")
@WebMvcTest(ProductCategoryRestApi.class)
@ComponentScan(basePackages = "cm.xenonbyte.farmbyte.catalog.adapter.rest.api")
@ContextConfiguration(classes = {ProductCategoryDomainServiceRestApiAdapter.class})
public final class ProductCategoryRestApiTest extends RestApiBeanConfigTest {

    public static final String PRODUCT_CATEGORY_PATH_URI = "/api/v1/catalog/product-categories";
    @Autowired
    private MockMvc mockMvc;

    @Nested
    class CreateProductCategoryRestApiTest {


        static Stream<Arguments> createProductMethodSourceArgs() {
            return Stream.of(
                    Arguments.of(
                            "Unite",
                            null,
                            UUID.randomUUID(),
                            true
                    ),
                    Arguments.of(
                            "Unite",
                            UUID.randomUUID(),
                            UUID.randomUUID(),
                            true
                    )
            );
        }

        @ParameterizedTest
        @MethodSource("createProductMethodSourceArgs")
        void should_create_root_or_child_product_category(
                String nameValue,
                UUID parentProductCategoryUUID,
                UUID newCreateProductCategoryUUID,
                boolean activeValue
        ) throws Exception {
            CreateProductCategoryViewRequest createProductCategoryViewRequest = new CreateProductCategoryViewRequest()
                    .name(nameValue)
                    .parentProductCategoryId(parentProductCategoryUUID);
            CreateProductCategoryViewResponse createProductCategoryViewResponse = new CreateProductCategoryViewResponse()
                    .id(newCreateProductCategoryUUID)
                    .name(nameValue)
                    .parentProductCategoryId(parentProductCategoryUUID)
                    .active(activeValue);

            when(productCategoryDomainServiceRestApiAdapter.createProductCategory(createProductCategoryViewRequest))
                    .thenReturn(createProductCategoryViewResponse);

            ArgumentCaptor<CreateProductCategoryViewRequest> createProductCategoryViewRequestArgumentCaptor =
                    ArgumentCaptor.forClass(CreateProductCategoryViewRequest.class);

            mockMvc.perform(post(PRODUCT_CATEGORY_PATH_URI)
                            .accept(APPLICATION_JSON)
                            .header(ACCEPT_LANGUAGE, EN_LOCALE)
                            .contentType(APPLICATION_JSON)
                            .content(createProductCategoryViewRequestAsString(createProductCategoryViewRequest)))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$").isNotEmpty())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.code").value(201))
                    .andExpect(jsonPath("$.message").value(MessageUtil.getMessage(PRODUCT_CATEGORY_CREATED_SUCCESSFULLY, Locale.forLanguageTag(EN_LOCALE), "")))
                    .andExpect(jsonPath("$.data").isNotEmpty())
                    .andExpect(jsonPath("$.data.content.name").value(createProductCategoryViewResponse.getName()))
                    .andExpect(jsonPath("$.data.content.id").value(createProductCategoryViewResponse.getId().toString()))
                    .andExpect(jsonPath("$.data.content.active").value(createProductCategoryViewResponse.getActive()));


            verify(productCategoryDomainServiceRestApiAdapter, times(1))
                    .createProductCategory(createProductCategoryViewRequestArgumentCaptor.capture());

            assertThat(createProductCategoryViewRequestArgumentCaptor.getValue()).isEqualTo(createProductCategoryViewRequest);


        }

        static Stream<Arguments> createProductMethodThrowExceptionSourceArgs() {
            return Stream.of(
                    Arguments.of(
                            "Unite",
                            UUID.randomUUID(),
                            new ProductCategoryNameConflictException(new String[] {"Unite"}),
                            PRODUCT_CATEGORY_NAME_CONFLICT_EXCEPTION,
                            "Unite",
                            409,
                            "CONFLICT",
                            status().isConflict()

                    ),
                    Arguments.of(
                            "Unite",
                            UUID.fromString("019156f3-0db6-794e-bfe0-f371636cd410"),
                            new ProductParentCategoryNotFoundException(new String[] {"019156f3-0db6-794e-bfe0-f371636cd410"}),
                            PRODUCT_PARENT_CATEGORY_WITH_ID_NOT_FOUND_EXCEPTION,
                            "019156f3-0db6-794e-bfe0-f371636cd410",
                            404,
                            "NOT_FOUND",
                            status().isNotFound()
                    )
            );
        }

        @ParameterizedTest
        @MethodSource("createProductMethodThrowExceptionSourceArgs")
        void should_throw_exception_when_create_product_category_fail(
                String nameValue,
                UUID parentProductCategoryUUID,
                RuntimeException exception,
                String exceptionMessage,
                String messageArgs,
                int code,
                String status,
                ResultMatcher isStatus

        ) throws Exception {
            CreateProductCategoryViewRequest createProductCategoryViewRequest = new CreateProductCategoryViewRequest()
                    .name(nameValue)
                    .parentProductCategoryId(parentProductCategoryUUID);

            when(productCategoryDomainServiceRestApiAdapter.createProductCategory(createProductCategoryViewRequest))
                    .thenThrow(exception);

            ArgumentCaptor<CreateProductCategoryViewRequest> createProductCategoryViewRequestArgumentCaptor =
                    ArgumentCaptor.forClass(CreateProductCategoryViewRequest.class);

            mockMvc.perform(post(PRODUCT_CATEGORY_PATH_URI)
                            .accept(APPLICATION_JSON)
                            .header(ACCEPT_LANGUAGE, EN_LOCALE)
                            .contentType(APPLICATION_JSON)
                            .content(createProductCategoryViewRequestAsString(createProductCategoryViewRequest)))
                    .andDo(print())
                    .andExpect(isStatus)
                    .andExpect(jsonPath("$").isNotEmpty())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.code").value(code))
                    .andExpect(jsonPath("$.status").value(status))
                    .andExpect(jsonPath("$.reason").value(MessageUtil.getMessage(
                            exceptionMessage, Locale.forLanguageTag(EN_LOCALE), messageArgs)));

            verify(productCategoryDomainServiceRestApiAdapter, times(1)).createProductCategory(createProductCategoryViewRequestArgumentCaptor.capture());
            assertThat(createProductCategoryViewRequestArgumentCaptor.getValue()).isEqualTo(createProductCategoryViewRequest);
        }

        @Test
        void should_throw_exception_when_create_product_category_without_required_field() throws Exception {
            CreateProductCategoryViewRequest createProductCategoryViewRequest = new CreateProductCategoryViewRequest()
                    .name(null)
                    .parentProductCategoryId(null);

            mockMvc.perform(post(PRODUCT_CATEGORY_PATH_URI)
                            .accept(APPLICATION_JSON)
                            .header(ACCEPT_LANGUAGE, EN_LOCALE)
                            .contentType(APPLICATION_JSON)
                            .content(createProductCategoryViewRequestAsString(createProductCategoryViewRequest)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$").isNotEmpty())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.code").value(400))
                    .andExpect(jsonPath("$.reason").value(MessageUtil.getMessage(
                            VALIDATION_ERROR_OCCURRED_WHEN_PROCESSING_REQUEST, Locale.forLanguageTag(EN_LOCALE), "")))
                    .andExpect(jsonPath("$.error.[0].field").isNotEmpty())
                    .andExpect(jsonPath("$.error.[0].message").isNotEmpty());
        }


    }

    @Nested
    class FindProductCategoryByIdRestApiTest {

        @Test
        void should_success_when_find_product_category_with_existing_id() throws Exception {
            //Given
            UUID productcategoryUUID = UUID.fromString("0191ac31-e0e9-76c2-bb68-bc155b49c3cf");
            FindProductCategoryByIdViewResponse findProductCategoryByIdViewResponse = new FindProductCategoryByIdViewResponse().id(productcategoryUUID).name("Unite");

            when(productCategoryDomainServiceRestApiAdapter.findProductCategoryById(productcategoryUUID)).thenReturn(findProductCategoryByIdViewResponse);
            ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);

            //Act
            mockMvc.perform(get(String.format(PRODUCT_CATEGORY_PATH_URI + "/%s", productcategoryUUID))
                            .accept(APPLICATION_JSON)
                            .header(ACCEPT_LANGUAGE, EN_LOCALE)
                            .contentType(APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isNotEmpty())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.status").value("OK"))
                    .andExpect(jsonPath("$.message").value(MessageUtil.getMessage(PRODUCT_CATEGORY_FIND_SUCCESSFULLY, Locale.forLanguageTag(EN_LOCALE), "")))
                    .andExpect(jsonPath("$.data").isMap())
                    .andExpect(jsonPath("$.data.content").isNotEmpty())
                    .andExpect(jsonPath("$.data.content.id").value(findProductCategoryByIdViewResponse.getId().toString()))
                    .andExpect(jsonPath("$.data.content.name").value(findProductCategoryByIdViewResponse.getName()))
                    .andExpect(jsonPath("$.data.content.active").value(findProductCategoryByIdViewResponse.getActive()));

            //Then
            verify(productCategoryDomainServiceRestApiAdapter, times(1)).findProductCategoryById(uuidArgumentCaptor.capture());
            assertThat(uuidArgumentCaptor.getValue()).isEqualTo(productcategoryUUID);
        }

        @Test
        void should_fail_when_find_product_category_with_non_existing_id() throws Exception {
            //Given
            UUID productcategoryUUID = UUID.fromString("0191ac31-e0e9-76c2-bb68-bc155b49c3cf");

            when(productCategoryDomainServiceRestApiAdapter.findProductCategoryById(productcategoryUUID)).thenThrow(ProductCategoryNotFoundException.class.getConstructor(Object[].class).newInstance(new Object[] {new String[]{productcategoryUUID.toString()}}));
            ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);

            //Act
            mockMvc.perform(get(String.format(PRODUCT_CATEGORY_PATH_URI + "/%s", productcategoryUUID))
                            .accept(APPLICATION_JSON)
                            .header(ACCEPT_LANGUAGE, EN_LOCALE)
                            .contentType(APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$").isNotEmpty())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.code").value(404))
                    .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                    .andExpect(jsonPath("$.reason").value(MessageUtil.getMessage(PRODUCT_CATEGORY_NOT_FOUND_EXCEPTION, Locale.forLanguageTag(EN_LOCALE), productcategoryUUID.toString())));

            //Then
            verify(productCategoryDomainServiceRestApiAdapter, times(1)).findProductCategoryById(uuidArgumentCaptor.capture());
            assertThat(uuidArgumentCaptor.getValue()).isEqualTo(productcategoryUUID);
        }
    }

    @Nested
    class FindProductCategoriesRestApiTest {

        @Test
        void should_success_when_find_product_categories() throws Exception {
            int page = 0;
            int size = 2;
            String attribute = "name";
            String direction = "ASC";

            FindProductCategoriesPageInfoViewResponse findProductCategoriesPageInfoViewResponse = new FindProductCategoriesPageInfoViewResponse()
                    .pageSize(size)
                    .last(false)
                    .first(true)
                    .totalElements(5L)
                    .totalPages(3)
                    .content(List.of(
                            new FindProductCategoriesViewResponse()
                                    .name("Alcohol")
                                    .id(UUID.randomUUID())
                                    .active(true),
                            new FindProductCategoriesViewResponse()
                                    .name("Juice")
                                    .id(UUID.randomUUID())
                                    .active(true),
                            new FindProductCategoriesViewResponse()
                                    .name("Water")
                                    .id(UUID.randomUUID())
                                    .active(true),
                            new FindProductCategoriesViewResponse()
                                    .name("Fresh")
                                    .id(UUID.randomUUID())
                                    .active(true),
                            new FindProductCategoriesViewResponse()
                                    .name("Meet")
                                    .id(UUID.randomUUID())
                                    .active(true)
                    ));
            when(productCategoryDomainServiceRestApiAdapter.findProductCategories(page, size, attribute, direction)).thenReturn(findProductCategoriesPageInfoViewResponse);
            ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
            ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
            //Act
            mockMvc.perform(get(PRODUCT_CATEGORY_PATH_URI )
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
                    .andExpect(jsonPath("$.message").value(MessageUtil.getMessage(PRODUCT_CATEGORIES_FIND_SUCCESSFULLY, Locale.forLanguageTag(EN_LOCALE), "")))
                    .andExpect(jsonPath("$.data").isNotEmpty())
                    .andExpect(jsonPath("$.data.content.content").isArray());

            //Then
            verify(productCategoryDomainServiceRestApiAdapter, times(1)).findProductCategories(integerArgumentCaptor.capture(), integerArgumentCaptor.capture(), stringArgumentCaptor.capture(), stringArgumentCaptor.capture());
            assertThat(integerArgumentCaptor.getAllValues().get(0)).isEqualTo(page);
            assertThat(integerArgumentCaptor.getAllValues().get(1)).isEqualTo(size);
            assertThat(stringArgumentCaptor.getAllValues().get(0)).isEqualTo(attribute);
            assertThat(stringArgumentCaptor.getAllValues().get(1)).isEqualTo(direction);

        }
    }

    @Nested
    class SearchProductCategoriesRestApiTest {
        @Test
        void should_success_when_search_product_categories_with_keyword() throws Exception {
            int page = 0;
            int size = 2;
            String attribute = "name";
            String direction = "ASC";
            String keyword = "e";

            SearchProductCategoriesPageInfoViewResponse searchProductCategoriesPageInfoViewResponse = new SearchProductCategoriesPageInfoViewResponse()
                    .pageSize(size)
                    .last(false)
                    .first(true)
                    .totalElements(5L)
                    .totalPages(3)
                    .content(List.of(
                            new SearchProductCategoriesViewResponse()
                                    .id(UUID.randomUUID())
                                    .name("Alcohol")
                                    .active(true),
                            new SearchProductCategoriesViewResponse()
                                    .id(UUID.randomUUID())
                                    .name("Juice")
                                    .active(true),
                            new SearchProductCategoriesViewResponse()
                                    .id(UUID.randomUUID())
                                    .name("Water")
                                    .active(true),
                            new SearchProductCategoriesViewResponse()
                                    .id(UUID.randomUUID())
                                    .name("Fresh")
                                    .active(true),
                            new SearchProductCategoriesViewResponse()
                                    .name("Meet")
                                    .id(UUID.randomUUID())
                                    .active(true)
                    ));
            when(productCategoryDomainServiceRestApiAdapter.searchProductCategories(page, size, attribute, direction, keyword)).thenReturn(searchProductCategoriesPageInfoViewResponse);
            ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
            ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

            //Act
            mockMvc.perform(get(PRODUCT_CATEGORY_PATH_URI + "/search")
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
                    .andExpect(jsonPath("$.message").value(MessageUtil.getMessage(PRODUCT_CATEGORIES_FIND_SUCCESSFULLY, Locale.forLanguageTag(EN_LOCALE), "")))
                    .andExpect(jsonPath("$.data").isNotEmpty())
                    .andExpect(jsonPath("$.data.content.content").isArray());

            //Then
            verify(productCategoryDomainServiceRestApiAdapter, times(1)).searchProductCategories(integerArgumentCaptor.capture(), integerArgumentCaptor.capture(),
                    stringArgumentCaptor.capture(), stringArgumentCaptor.capture(), stringArgumentCaptor.capture());
            assertThat(integerArgumentCaptor.getAllValues().get(0)).isEqualTo(page);
            assertThat(integerArgumentCaptor.getAllValues().get(1)).isEqualTo(size);
            assertThat(stringArgumentCaptor.getAllValues().get(0)).isEqualTo(attribute);
            assertThat(stringArgumentCaptor.getAllValues().get(1)).isEqualTo(direction);
            assertThat(stringArgumentCaptor.getAllValues().get(2)).isEqualTo(keyword);

        }
    }

    private String createProductCategoryViewRequestAsString(CreateProductCategoryViewRequest createProductCategoryViewRequest) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(createProductCategoryViewRequest);
    }
}
