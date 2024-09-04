package cm.xenonbyte.farmbyte.catalog.adapter.rest.api.product;

import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.RestApiBeanConfigTest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.CreateProductViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.CreateProductViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.FindProductByIdViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.FindProductsPageInfoViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.FindProductsViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.SearchProductsPageInfoViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.SearchProductsViewResponse;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductCategoryNotFoundException;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductId;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductNameConflictException;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductNotFoundException;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductStockAndPurchaseUomBadException;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductUomNotFoundException;
import cm.xenonbyte.farmbyte.common.adapter.api.messages.MessageUtil;
import cm.xenonbyte.farmbyte.common.domain.vo.Filename;
import cm.xenonbyte.farmbyte.common.domain.vo.Image;
import cm.xenonbyte.farmbyte.common.domain.vo.StorageLocation;
import cm.xenonbyte.farmbyte.common.domain.vo.Text;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nonnull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Stream;

import static cm.xenonbyte.farmbyte.catalog.adapter.rest.api.product.ProductRestApi.PRODUCTS_FIND_SUCCESSFULLY;
import static cm.xenonbyte.farmbyte.catalog.adapter.rest.api.product.ProductRestApi.PRODUCT_CREATED_SUCCESSFULLY;
import static cm.xenonbyte.farmbyte.catalog.adapter.rest.api.product.ProductRestApi.PRODUCT_FIND_SUCCESSFULLY;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_CATEGORY_IS_REQUIRED;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_CATEGORY_NOT_FOUND_EXCEPTION;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_NAME_CONFLICT_EXCEPTION;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_NAME_IS_REQUIRED;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_NOT_FOUND_EXCEPTION;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_STOCK_AND_PURCHASE_UOM_BAD_EXCEPTION;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_TYPE_IS_REQUIRED;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_UOM_NOT_FOUND_EXCEPTION;
import static cm.xenonbyte.farmbyte.common.adapter.api.constant.CommonAdapterRestApi.ACCEPT_LANGUAGE;
import static cm.xenonbyte.farmbyte.common.adapter.api.constant.CommonAdapterRestApi.EN_LOCALE;
import static cm.xenonbyte.farmbyte.common.adapter.api.constant.CommonAdapterRestApi.VALIDATION_ERROR_OCCURRED_WHEN_PROCESSING_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author bamk
 * @version 1.0
 * @since 26/08/2024
 */
@ActiveProfiles("test")
@WebMvcTest(ProductRestApi.class)
@ComponentScan(basePackages = "cm.xenonbyte.farmbyte.catalog.adapter.rest.api")
@ContextConfiguration(classes = {ProductDomainServiceRestApiAdapter.class})
public final class ProductRestApiTest extends RestApiBeanConfigTest {

    public static final String PRODUCT_PATH_URI = "/api/v1/catalog/products";

    @Autowired
    private MockMvc mockMvc;

    MockMultipartFile imageMultipartFile;
    StorageLocation location;
    Filename fileName;

    @BeforeEach
    void setUp() {
        imageMultipartFile = new MockMultipartFile(
                "image",
                "image.jpg",
                "image/jpeg",
                "<<image data>>".getBytes(StandardCharsets.UTF_8)
        );
        location = StorageLocation.computeStoragePtah("products");
        fileName = Filename.of(Text.of(location.getPath().getValue()));
        ReflectionTestUtils.setField(productDomainServiceRestApiAdapter, "rootPathStorageProducts", "products");
    }

    @Nested
    class CreateProductRestApiTest {
        @Test
        void should_create_product() throws Exception {
            //Given
            String name = "Product.1";
            UUID categoryId = UUID.randomUUID();
            ProductId productId = new ProductId(UUID.randomUUID());

            CreateProductViewRequest createProductViewRequest = new CreateProductViewRequest()
                    .name(name)
                    .type(CreateProductViewRequest.TypeEnum.SERVICE)
                    .categoryId(categoryId);

            MockMultipartFile createProductViewMultipartFile = new MockMultipartFile(
                    "createProductViewRequest",
                    "createProductViewRequest",
                    APPLICATION_JSON_VALUE,
                    createProductViewAsString(createProductViewRequest).getBytes(StandardCharsets.UTF_8)
            );

            CreateProductViewResponse createProductViewResponse = new CreateProductViewResponse()
                    .id(productId.getValue())
                    .name(name)
                    .type(CreateProductViewResponse.TypeEnum.SERVICE)
                    .categoryId(categoryId)
                    .purchasePrice(BigDecimal.ZERO)
                    .salePrice(BigDecimal.ZERO)
                    .purchasable(false)
                    .sellable(false)
                    .filename(Image.DEFAULT_PRODUCT_IMAGE_URL)
                    .active(true);

            when(productDomainServiceRestApiAdapter.createProduct(createProductViewRequest, imageMultipartFile)).thenReturn(createProductViewResponse);
            ArgumentCaptor<CreateProductViewRequest> createProductViewRequestArgumentCaptor =
                    ArgumentCaptor.forClass(CreateProductViewRequest.class);
            ArgumentCaptor<MockMultipartFile> mockMultipartFileArgumentCaptor = ArgumentCaptor.forClass(MockMultipartFile.class);

            //Act + Then
            mockMvc.perform(multipart(PRODUCT_PATH_URI)
                            .file(imageMultipartFile)
                            .file(createProductViewMultipartFile)
                            .accept(APPLICATION_JSON)
                            .header(ACCEPT_LANGUAGE, EN_LOCALE)
                            .contentType(MULTIPART_FORM_DATA))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$").isNotEmpty())
                    .andExpect(jsonPath("$.code").value(201))
                    .andExpect(jsonPath("$.status").value("CREATED"))
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value(MessageUtil.getMessage(PRODUCT_CREATED_SUCCESSFULLY, Locale.forLanguageTag(EN_LOCALE), "")))
                    .andExpect(jsonPath("$.data").isNotEmpty())
                    .andExpect(jsonPath("$.data.content.id").value(productId.getValue().toString()))
                    .andExpect(jsonPath("$.data.content.name").value(name))
                    .andExpect(jsonPath("$.data.content.categoryId").value(categoryId.toString()));

            verify(productDomainServiceRestApiAdapter, times(1)).createProduct(createProductViewRequestArgumentCaptor.capture(), mockMultipartFileArgumentCaptor.capture());
            assertThat(createProductViewRequestArgumentCaptor.getValue()).isEqualTo(createProductViewRequest);
            assertThat(mockMultipartFileArgumentCaptor.getValue()).isEqualTo(imageMultipartFile);
        }

        static Stream<Arguments> createProductThrowExceptionMethodSource() {
            return Stream.of(
                    Arguments.of(
                            null,
                            UUID.randomUUID(),
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
                            UUID.randomUUID(),
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
            MockMultipartFile createProductViewMultipartFile = new MockMultipartFile(
                    "createProductViewRequest",
                    "createProductViewRequest",
                    APPLICATION_JSON_VALUE,
                    createProductViewAsString(createProductViewRequest).getBytes(StandardCharsets.UTF_8)
            );

            //Act + Then
            mockMvc.perform(multipart(PRODUCT_PATH_URI)
                            .file(createProductViewMultipartFile)
                            .file(imageMultipartFile)
                            .accept(APPLICATION_JSON)
                            .header(ACCEPT_LANGUAGE, EN_LOCALE)
                            .contentType(MULTIPART_FORM_DATA))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(400))
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.reason").value(MessageUtil.getMessage(
                            VALIDATION_ERROR_OCCURRED_WHEN_PROCESSING_REQUEST, Locale.forLanguageTag(EN_LOCALE), ""
                    )))
                    .andExpect(jsonPath("$.error.[0].field").value(field))
                    .andExpect(jsonPath("$.error.[0].message").value(MessageUtil.getMessage(
                            message, Locale.forLanguageTag(EN_LOCALE), ""
                    )));
        }

        @Test
        void should_throw_exception_when_create_product_with_existing_name() throws Exception {
            //Given
            String name = "Product.1";
            UUID categoryId = UUID.randomUUID();

            CreateProductViewRequest createProductViewRequest = new CreateProductViewRequest()
                    .name(name)
                    .type(CreateProductViewRequest.TypeEnum.SERVICE)
                    .categoryId(categoryId);

            MockMultipartFile createProductViewMultipartFile = new MockMultipartFile(
                    "createProductViewRequest",
                    "createProductViewRequest",
                    APPLICATION_JSON_VALUE,
                    createProductViewAsString(createProductViewRequest).getBytes(StandardCharsets.UTF_8)
            );

            when(productDomainServiceRestApiAdapter.createProduct(createProductViewRequest, imageMultipartFile))
                    .thenThrow(ProductNameConflictException.class.getConstructor(Object[].class).newInstance(new Object[]{new String[]{name}}));
            ArgumentCaptor<CreateProductViewRequest> createProductViewRequestArgumentCaptor =
                    ArgumentCaptor.forClass(CreateProductViewRequest.class);
            ArgumentCaptor<MockMultipartFile> mockMultipartFileArgumentCaptor = ArgumentCaptor.forClass(MockMultipartFile.class);

            //Act + Then
            mockMvc.perform(multipart(PRODUCT_PATH_URI)
                            .file(createProductViewMultipartFile)
                            .file(imageMultipartFile)
                            .accept(APPLICATION_JSON)
                            .header(ACCEPT_LANGUAGE, EN_LOCALE)
                            .contentType(MULTIPART_FORM_DATA))
                    .andDo(print())
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$").isNotEmpty())
                    .andExpect(jsonPath("$.code").value(409))
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.status").value("CONFLICT"))
                    .andExpect(jsonPath("$.reason").value(MessageUtil.getMessage(PRODUCT_NAME_CONFLICT_EXCEPTION, Locale.forLanguageTag(EN_LOCALE), name)));

            verify(productDomainServiceRestApiAdapter, times(1)).createProduct(createProductViewRequestArgumentCaptor.capture(), mockMultipartFileArgumentCaptor.capture());
            assertThat(createProductViewRequestArgumentCaptor.getValue()).isEqualTo(createProductViewRequest);
            assertThat(mockMultipartFileArgumentCaptor.getValue()).isEqualTo(imageMultipartFile);
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

            MockMultipartFile createProductViewMultipartFile = new MockMultipartFile(
                    "createProductViewRequest",
                    "createProductViewRequest",
                    APPLICATION_JSON_VALUE,
                    createProductViewAsString(createProductViewRequest).getBytes(StandardCharsets.UTF_8)
            );

            when(productDomainServiceRestApiAdapter.createProduct(createProductViewRequest, imageMultipartFile))
                    .thenThrow(ProductCategoryNotFoundException.class.getConstructor(Object[].class).newInstance(new Object[]{new String[]{categoryId.toString()}}));
            ArgumentCaptor<CreateProductViewRequest> createProductViewRequestArgumentCaptor =
                    ArgumentCaptor.forClass(CreateProductViewRequest.class);
            ArgumentCaptor<MockMultipartFile> mockMultipartFileArgumentCaptor = ArgumentCaptor.forClass(MockMultipartFile.class);

            //Act + Then
            mockMvc.perform(multipart(PRODUCT_PATH_URI)
                            .file(createProductViewMultipartFile)
                            .file(imageMultipartFile)
                            .accept(APPLICATION_JSON)
                            .header(ACCEPT_LANGUAGE, EN_LOCALE)
                            .contentType(MULTIPART_FORM_DATA))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$").isNotEmpty())
                    .andExpect(jsonPath("$.code").value(404))
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                    .andExpect(jsonPath("$.reason").value(MessageUtil.getMessage(PRODUCT_CATEGORY_NOT_FOUND_EXCEPTION, Locale.forLanguageTag(EN_LOCALE), categoryId.toString())));

            verify(productDomainServiceRestApiAdapter, times(1)).createProduct(createProductViewRequestArgumentCaptor.capture(), mockMultipartFileArgumentCaptor.capture());
            assertThat(createProductViewRequestArgumentCaptor.getValue()).isEqualTo(createProductViewRequest);
            assertThat(mockMultipartFileArgumentCaptor.getValue()).isEqualTo(imageMultipartFile);
        }

        @Test
        void should_throw_exception_when_create_product_with_un_know_purchase_or_stock_uom_id() throws Exception {
            //Given
            String name = "Product.1";
            UUID categoryId = UUID.randomUUID();

            UUID stockUomId = UUID.randomUUID();
            UUID purchaseUomId = UUID.randomUUID();
            CreateProductViewRequest createProductViewRequest = new CreateProductViewRequest()
                    .name(name)
                    .type(CreateProductViewRequest.TypeEnum.STOCK)
                    .stockUomId(stockUomId)
                    .purchaseUomId(purchaseUomId)
                    .categoryId(categoryId);

            MockMultipartFile createProductViewMultipartFile = new MockMultipartFile(
                    "createProductViewRequest",
                    "createProductViewRequest",
                    APPLICATION_JSON_VALUE,
                    createProductViewAsString(createProductViewRequest).getBytes(StandardCharsets.UTF_8)
            );

            when(productDomainServiceRestApiAdapter.createProduct(createProductViewRequest, imageMultipartFile))
                    .thenThrow(ProductUomNotFoundException.class.getConstructor(Object[].class).newInstance(new Object[]{new String[]{stockUomId.toString()}}));
            ArgumentCaptor<CreateProductViewRequest> createProductViewRequestArgumentCaptor =
                    ArgumentCaptor.forClass(CreateProductViewRequest.class);
            ArgumentCaptor<MockMultipartFile> mockMultipartFileArgumentCaptor = ArgumentCaptor.forClass(MockMultipartFile.class);

            //Act + Then
            mockMvc.perform(multipart(PRODUCT_PATH_URI)
                            .file(createProductViewMultipartFile)
                            .file(imageMultipartFile)
                            .accept(APPLICATION_JSON)
                            .header(ACCEPT_LANGUAGE, EN_LOCALE)
                            .contentType(MULTIPART_FORM_DATA))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$").isNotEmpty())
                    .andExpect(jsonPath("$.code").value(404))
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                    .andExpect(jsonPath("$.reason").value(MessageUtil.getMessage(PRODUCT_UOM_NOT_FOUND_EXCEPTION, Locale.forLanguageTag(EN_LOCALE), stockUomId.toString())));

            verify(productDomainServiceRestApiAdapter, times(1)).createProduct(createProductViewRequestArgumentCaptor.capture(), mockMultipartFileArgumentCaptor.capture());
            assertThat(createProductViewRequestArgumentCaptor.getValue()).isEqualTo(createProductViewRequest);
            assertThat(mockMultipartFileArgumentCaptor.getValue()).isEqualTo(imageMultipartFile);
        }

        @Test
        void should_throw_exception_when_create_product_with_purchase_and_stock_uom_id_in_different_uom_category() throws Exception {
            //Given
            String name = "Product.1";
            UUID categoryId = UUID.randomUUID();

            UUID stockUomId = UUID.randomUUID();
            UUID purchaseUomId = UUID.randomUUID();
            CreateProductViewRequest createProductViewRequest = new CreateProductViewRequest()
                    .name(name)
                    .type(CreateProductViewRequest.TypeEnum.STOCK)
                    .stockUomId(stockUomId)
                    .purchaseUomId(purchaseUomId)
                    .categoryId(categoryId);

            MockMultipartFile createProductViewMultipartFile = new MockMultipartFile(
                    "createProductViewRequest",
                    "createProductViewRequest",
                    APPLICATION_JSON_VALUE,
                    createProductViewAsString(createProductViewRequest).getBytes(StandardCharsets.UTF_8)
            );

            when(productDomainServiceRestApiAdapter.createProduct(createProductViewRequest, imageMultipartFile))
                    .thenThrow(ProductStockAndPurchaseUomBadException.class.getConstructor(Object[].class).newInstance(new Object[]{new String[]{stockUomId.toString(), purchaseUomId.toString()}}));
            ArgumentCaptor<CreateProductViewRequest> createProductViewRequestArgumentCaptor =
                    ArgumentCaptor.forClass(CreateProductViewRequest.class);
            ArgumentCaptor<MockMultipartFile> mockMultipartFileArgumentCaptor = ArgumentCaptor.forClass(MockMultipartFile.class);

            //Act + Then
            mockMvc.perform(multipart(PRODUCT_PATH_URI)
                            .file(createProductViewMultipartFile)
                            .file(imageMultipartFile)
                            .accept(APPLICATION_JSON)
                            .header(ACCEPT_LANGUAGE, EN_LOCALE)
                            .contentType(MULTIPART_FORM_DATA))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$").isNotEmpty())
                    .andExpect(jsonPath("$.code").value(400))
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                    .andExpect(jsonPath("$.reason").value(MessageUtil.getMessage(PRODUCT_STOCK_AND_PURCHASE_UOM_BAD_EXCEPTION, Locale.forLanguageTag(EN_LOCALE), stockUomId.toString(), purchaseUomId.toString())));

            verify(productDomainServiceRestApiAdapter, times(1)).createProduct(createProductViewRequestArgumentCaptor.capture(), mockMultipartFileArgumentCaptor.capture());
            assertThat(createProductViewRequestArgumentCaptor.getValue()).isEqualTo(createProductViewRequest);
            assertThat(mockMultipartFileArgumentCaptor.getValue()).isEqualTo(imageMultipartFile);
        }
    }

    @Nested
    class FindProductByIdRestApiTest {

        @Test
        void should_success_when_find_product_with_existing_id() throws Exception {

            //Given
            UUID productIdUUID = UUID.randomUUID();
            FindProductByIdViewResponse findProductByIdViewResponse = new FindProductByIdViewResponse()
                    .id(productIdUUID)
                    .name("HP Pro")
                    .categoryId(UUID.randomUUID())
                    .type(FindProductByIdViewResponse.TypeEnum.CONSUMABLE)
                    .reference("65489456")
                    .purchasePrice(BigDecimal.valueOf(175.47))
                    .salePrice(BigDecimal.valueOf(350.12))
                    .sellable(true)
                    .purchasable(true)
                    .active(true);

            when(productDomainServiceRestApiAdapter.findProductById(productIdUUID)).thenReturn(findProductByIdViewResponse);
            ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);

            //Act
            mockMvc.perform(get(String.format(PRODUCT_PATH_URI + "/%s", productIdUUID))
                            .accept(APPLICATION_JSON)
                            .header(ACCEPT_LANGUAGE, EN_LOCALE)
                            .contentType(APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isNotEmpty())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.status").value("OK"))
                    .andExpect(jsonPath("$.message").value(MessageUtil.getMessage(PRODUCT_FIND_SUCCESSFULLY, Locale.forLanguageTag(EN_LOCALE), "")))
                    .andExpect(jsonPath("$.data").isMap())
                    .andExpect(jsonPath("$.data.content").isNotEmpty())
                    .andExpect(jsonPath("$.data.content.id").value(findProductByIdViewResponse.getId().toString()))
                    .andExpect(jsonPath("$.data.content.name").value(findProductByIdViewResponse.getName()))
                    .andExpect(jsonPath("$.data.content.categoryId").value(findProductByIdViewResponse.getCategoryId().toString()))
                    .andExpect(jsonPath("$.data.content.sellable").value(findProductByIdViewResponse.getSellable()))
                    .andExpect(jsonPath("$.data.content.purchasable").value(findProductByIdViewResponse.getPurchasable()))
                    .andExpect(jsonPath("$.data.content.active").value(findProductByIdViewResponse.getActive()));


            //Then
            verify(productDomainServiceRestApiAdapter, times(1)).findProductById(uuidArgumentCaptor.capture());
            assertThat(uuidArgumentCaptor.getValue()).isEqualTo(productIdUUID);

        }

        @Test
        void should_fail_when_find_product_by_non_existing_id() throws Exception {
            //Given
            UUID productIdUUID = UUID.randomUUID();

            when(productDomainServiceRestApiAdapter.findProductById(productIdUUID))
                    .thenThrow(ProductNotFoundException.class.getConstructor(Object[].class)
                            .newInstance(new Object[]{new String[]{productIdUUID.toString()}}));

            ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);

            //Act
            mockMvc.perform(get(String.format(PRODUCT_PATH_URI + "/%s", productIdUUID))
                            .accept(APPLICATION_JSON)
                            .header(ACCEPT_LANGUAGE, EN_LOCALE)
                            .contentType(APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$").isNotEmpty())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.code").value(404))
                    .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                    .andExpect(jsonPath("$.reason").value(MessageUtil.getMessage(PRODUCT_NOT_FOUND_EXCEPTION, Locale.forLanguageTag(EN_LOCALE), productIdUUID.toString())));
        }
    }

    @Nested
    class FindProductsRestApiTest {

        @Test
        void should_success_when_find_products() throws Exception {

            //Given
            int page = 0;
            int size = 2;
            String attribute = "name";
            String direction = "ASC";

            FindProductsPageInfoViewResponse findProductsPageInfoViewResponse = new FindProductsPageInfoViewResponse()
                    .first(true)
                    .last(false)
                    .pageSize(size)
                    .totalElements(3L)
                    .totalPages(3)
                    .content(
                            List.of(
                                new FindProductsViewResponse()
                                    .id(UUID.randomUUID())
                                    .name("Mac Book Pro 2023")
                                    .categoryId(UUID.randomUUID())
                                    .type(FindProductsViewResponse.TypeEnum.CONSUMABLE)
                                    .reference("20123546")
                                    .purchasePrice(BigDecimal.valueOf(250.35))
                                    .salePrice(BigDecimal.valueOf(475.41))
                                    .sellable(true)
                                    .purchasable(true)
                                    .active(true),

                                new FindProductsViewResponse()
                                    .id(UUID.randomUUID())
                                    .name("HP Pro")
                                    .categoryId(UUID.randomUUID())
                                    .type(FindProductsViewResponse.TypeEnum.CONSUMABLE)
                                    .reference("65489456")
                                    .purchasePrice(BigDecimal.valueOf(175.47))
                                    .salePrice(BigDecimal.valueOf(350.12))
                                    .sellable(true)
                                    .purchasable(true)
                                    .active(true),

                                new FindProductsViewResponse()
                                    .id(UUID.randomUUID())
                                    .name("DELL Pro")
                                    .categoryId(UUID.randomUUID())
                                    .type(FindProductsViewResponse.TypeEnum.CONSUMABLE)
                                    .reference("159989561")
                                    .purchasePrice(BigDecimal.valueOf(223.95))
                                    .salePrice(BigDecimal.valueOf(410.12))
                                    .sellable(true)
                                    .purchasable(true)
                                    .active(true)
                            )
                    );

            when(productDomainServiceRestApiAdapter.findProducts(page, size, attribute, direction))
                    .thenReturn(findProductsPageInfoViewResponse);
            ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
            ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

            //Act
            mockMvc.perform(get(String.format(PRODUCT_PATH_URI))
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
                    .andExpect(jsonPath("$.message").value(MessageUtil.getMessage(PRODUCTS_FIND_SUCCESSFULLY, Locale.forLanguageTag(EN_LOCALE), "")))
                    .andExpect(jsonPath("$.data").isNotEmpty())
                    .andExpect(jsonPath("$.data.content.content").isArray());

            verify(productDomainServiceRestApiAdapter, times(1)).findProducts(integerArgumentCaptor.capture(),
                    integerArgumentCaptor.capture(), stringArgumentCaptor.capture(), stringArgumentCaptor.capture());

            assertThat(integerArgumentCaptor.getAllValues().get(0)).isEqualTo(page);
            assertThat(integerArgumentCaptor.getAllValues().get(1)).isEqualTo(size);
            assertThat(stringArgumentCaptor.getAllValues().get(0)).isEqualTo(attribute);
            assertThat(stringArgumentCaptor.getAllValues().get(1)).isEqualTo(direction);


        }
    }

    @Nested
    class SearchProductsRestApiTest {

        @Test
        void should_success_when_find_products() throws Exception {

            //Given
            int page = 0;
            int size = 2;
            String attribute = "name";
            String direction = "ASC";
            String keyword = "p";

            SearchProductsPageInfoViewResponse searchProductsPageInfoViewResponse = new SearchProductsPageInfoViewResponse()
                    .first(true)
                    .last(false)
                    .pageSize(size)
                    .totalElements(3L)
                    .totalPages(3)
                    .content(
                            List.of(
                                    new SearchProductsViewResponse()
                                            .id(UUID.randomUUID())
                                            .name("Mac Book Pro 2023")
                                            .categoryId(UUID.randomUUID())
                                            .type(SearchProductsViewResponse.TypeEnum.CONSUMABLE)
                                            .reference("20123546")
                                            .purchasePrice(BigDecimal.valueOf(250.35))
                                            .salePrice(BigDecimal.valueOf(475.41))
                                            .sellable(true)
                                            .purchasable(true)
                                            .active(true),

                                    new SearchProductsViewResponse()
                                            .id(UUID.randomUUID())
                                            .name("HP Pro")
                                            .categoryId(UUID.randomUUID())
                                            .type(SearchProductsViewResponse.TypeEnum.CONSUMABLE)
                                            .reference("65489456")
                                            .purchasePrice(BigDecimal.valueOf(175.47))
                                            .salePrice(BigDecimal.valueOf(350.12))
                                            .sellable(true)
                                            .purchasable(true)
                                            .active(true),

                                    new SearchProductsViewResponse()
                                            .id(UUID.randomUUID())
                                            .name("DELL Pro")
                                            .categoryId(UUID.randomUUID())
                                            .type(SearchProductsViewResponse.TypeEnum.CONSUMABLE)
                                            .reference("159989561")
                                            .purchasePrice(BigDecimal.valueOf(223.95))
                                            .salePrice(BigDecimal.valueOf(410.12))
                                            .sellable(true)
                                            .purchasable(true)
                                            .active(true)
                            )
                    );

            when(productDomainServiceRestApiAdapter.searchProducts(page, size, attribute, direction, keyword))
                    .thenReturn(searchProductsPageInfoViewResponse);
            ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
            ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

            //Act
            mockMvc.perform(get(String.format(PRODUCT_PATH_URI + "/search"))
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
                    .andExpect(jsonPath("$.message").value(MessageUtil.getMessage(PRODUCTS_FIND_SUCCESSFULLY, Locale.forLanguageTag(EN_LOCALE), "")))
                    .andExpect(jsonPath("$.data").isNotEmpty())
                    .andExpect(jsonPath("$.data.content.content").isArray());

            verify(productDomainServiceRestApiAdapter, times(1)).searchProducts(integerArgumentCaptor.capture(),
                    integerArgumentCaptor.capture(), stringArgumentCaptor.capture(), stringArgumentCaptor.capture(), stringArgumentCaptor.capture());

            assertThat(integerArgumentCaptor.getAllValues().get(0)).isEqualTo(page);
            assertThat(integerArgumentCaptor.getAllValues().get(1)).isEqualTo(size);
            assertThat(stringArgumentCaptor.getAllValues().get(0)).isEqualTo(attribute);
            assertThat(stringArgumentCaptor.getAllValues().get(1)).isEqualTo(direction);


        }
    }

    private String createProductViewAsString(@Nonnull CreateProductViewRequest createProductViewRequest) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(createProductViewRequest);
    }

}
