package cm.xenonbyte.farmbyte.catalog.adapter.rest.api.product;

import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.ApiRestBeanConfig;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.CreateProductViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.CreateProductViewResponse;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductCategoryNotFoundException;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductId;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductNameConflictException;
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

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Stream;

import static cm.xenonbyte.farmbyte.catalog.adapter.rest.api.product.ProductApiRest.PRODUCT_CREATED_SUCCESSFULLY;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_CATEGORY_IS_REQUIRED;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_CATEGORY_NOT_FOUND_EXCEPTION;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_NAME_CONFLICT_EXCEPTION;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_NAME_IS_REQUIRED;
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
@WebMvcTest(ProductApiRest.class)
@ComponentScan(basePackages = "cm.xenonbyte.farmbyte.catalog.adapter.rest.api")
@ContextConfiguration(classes = {ProductApiAdapterService.class})
public final class ProductApiRestTest extends ApiRestBeanConfig {

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
        ReflectionTestUtils.setField(productApiAdapterService, "rootPathStorageProducts", "products");
    }

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

        when(productApiAdapterService.createProduct(createProductViewRequest, imageMultipartFile)).thenReturn(createProductViewResponse);
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

        verify(productApiAdapterService, times(1)).createProduct(createProductViewRequestArgumentCaptor.capture(), mockMultipartFileArgumentCaptor.capture());
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

        when(productApiAdapterService.createProduct(createProductViewRequest, imageMultipartFile))
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

        verify(productApiAdapterService, times(1)).createProduct(createProductViewRequestArgumentCaptor.capture(), mockMultipartFileArgumentCaptor.capture());
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

        when(productApiAdapterService.createProduct(createProductViewRequest, imageMultipartFile))
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

        verify(productApiAdapterService, times(1)).createProduct(createProductViewRequestArgumentCaptor.capture(), mockMultipartFileArgumentCaptor.capture());
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

        when(productApiAdapterService.createProduct(createProductViewRequest, imageMultipartFile))
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

        verify(productApiAdapterService, times(1)).createProduct(createProductViewRequestArgumentCaptor.capture(), mockMultipartFileArgumentCaptor.capture());
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

        when(productApiAdapterService.createProduct(createProductViewRequest, imageMultipartFile))
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

        verify(productApiAdapterService, times(1)).createProduct(createProductViewRequestArgumentCaptor.capture(), mockMultipartFileArgumentCaptor.capture());
        assertThat(createProductViewRequestArgumentCaptor.getValue()).isEqualTo(createProductViewRequest);
        assertThat(mockMultipartFileArgumentCaptor.getValue()).isEqualTo(imageMultipartFile);
    }

    private String createProductViewAsString(@Nonnull CreateProductViewRequest createProductViewRequest) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(createProductViewRequest);
    }

}
