package cm.xenonbyte.farmbyte.catalog.adapter.rest.api.product;

import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.CreateProductViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.CreateProductViewResponse;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.Product;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductCategoryId;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductCategoryNotFoundException;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductId;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductNameConflictException;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductStockAndPurchaseUomBadException;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductType;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductUomNotFoundException;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ports.primary.IProductService;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomId;
import cm.xenonbyte.farmbyte.common.domain.vo.Image;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.Text;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.stream.Stream;

import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_CATEGORY_IS_REQUIRED;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_CATEGORY_NOT_FOUND_EXCEPTION;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_NAME_CONFLICT_EXCEPTION;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_NAME_IS_REQUIRED;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_PURCHASE_UOM_IS_REQUIRED_WHEN_TYPE_IS_STOCK;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_STOCK_AND_PURCHASE_UOM_BAD_EXCEPTION;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_STOCK_UOM_IS_REQUIRED_WHEN_TYPE_IS_STOCK;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_TYPE_IS_REQUIRED;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_UOM_NOT_FOUND_EXCEPTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author bamk
 * @version 1.0
 * @since 23/08/2024
 */
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
final class ProductApiAdapterServiceTest {

    private IProductApiAdapterService productApiAdapterService;
    @Mock
    private IProductService productService;
    @Mock
    private ProductViewMapper productViewMapper;

    @BeforeEach
    void setUp() {
        productApiAdapterService = new ProductApiAdapterService(productService, productViewMapper);
    }

    static Stream<Arguments> createProductMethodSource() {
        return Stream.of(
                Arguments.of(
                        "Product.1",
                        null,
                        UUID.randomUUID(),
                        "SERVICE",
                        UUID.randomUUID(),
                        null,
                        null
                ),
                Arguments.of(
                        "Product.2",
                        null,
                        UUID.randomUUID(),
                        "CONSUMABLE",
                        UUID.randomUUID(),
                        null,
                        null
                ),
                Arguments.of(
                        "Product.3",
                        null,
                        UUID.randomUUID(),
                        "STOCK",
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        UUID.randomUUID()
                )
        );
    }

    @ParameterizedTest
    @MethodSource("createProductMethodSource")
    void should_create_product(
            String name,
            String image,
            UUID categoryId,
            String type,
            UUID productId,
            UUID stockUomId,
            UUID purchaseUomId
    ) {

        //Given
        CreateProductViewRequest createProductViewRequest = new CreateProductViewRequest()
                .name(name)
                .image(image)
                .categoryId(categoryId)
                .type(CreateProductViewRequest.TypeEnum.valueOf(type))
                .stockUomId(stockUomId)
                .purchaseUomId(purchaseUomId);

        Product productRequest = Product.builder()
                .name(Name.of(Text.of(name)))
                .image(image == null || image.isEmpty()? null: Image.with(Text.of(image)))
                .type(ProductType.valueOf(type))
                .categoryId(new ProductCategoryId(categoryId))
                .stockUomId(stockUomId == null? null: new UomId(stockUomId))
                .purchaseUomId(purchaseUomId == null? null: new UomId(purchaseUomId))
                .build();

        Product productResponse= Product.builder()
                .id(new ProductId(productId))
                .name(Name.of(Text.of(name)))
                .image(image == null || image.isEmpty()? null: Image.with(Text.of(image)))
                .type(ProductType.valueOf(type))
                .categoryId(new ProductCategoryId(categoryId))
                .stockUomId(stockUomId == null? null: new UomId(stockUomId))
                .purchaseUomId(purchaseUomId == null? null: new UomId(purchaseUomId))
                .build();

        CreateProductViewResponse createProductViewResponse = new CreateProductViewResponse()
                .name(name)
                .image(image)
                .categoryId(categoryId)
                .type(CreateProductViewResponse.TypeEnum.valueOf(type))
                .purchasable(false)
                .sellable(false)
                .salePrice(BigDecimal.ZERO)
                .purchasePrice(BigDecimal.ZERO)
                .active(true)
                .stockUomId(null)
                .purchasable(null)
                .id(productId);

        when(productViewMapper.toProduct(createProductViewRequest)).thenReturn(productRequest);
        when(productService.createProduct(productRequest)).thenReturn(productResponse);
        when(productViewMapper.toCreateProductViewResponse(productResponse)).thenReturn(createProductViewResponse);

        ArgumentCaptor<CreateProductViewRequest> createProductViewRequestArgumentCaptor =
                ArgumentCaptor.forClass(CreateProductViewRequest.class);

        ArgumentCaptor<Product> productArgumentCaptor = ArgumentCaptor.forClass(Product.class);

        //Act
        CreateProductViewResponse result = productApiAdapterService.createProduct(createProductViewRequest);

        //Then
        assertThat(result).isNotNull().isEqualTo(createProductViewResponse);

        verify(productViewMapper, times(1)).toProduct(createProductViewRequestArgumentCaptor.capture());
        verify(productService, times(1)).createProduct(productArgumentCaptor.capture());
        verify(productViewMapper, times(1)).toCreateProductViewResponse(productArgumentCaptor.capture());

        assertThat(createProductViewRequestArgumentCaptor.getValue()).isEqualTo(createProductViewRequest);
        assertThat(productArgumentCaptor.getAllValues().get(0)).isEqualTo(productRequest);
        assertThat(productArgumentCaptor.getAllValues().get(1)).isEqualTo(productResponse);

    }

    static Stream<Arguments> createProductThrowExceptionMethodSource() {
        return Stream.of(
                Arguments.of(
                        null,
                        null,
                        UUID.randomUUID(),
                        "SERVICE",
                        null,
                        null,
                        IllegalArgumentException.class,
                        PRODUCT_NAME_IS_REQUIRED

                ),
                Arguments.of(
                        "Product.1",
                        null,
                        null,
                        "SERVICE",
                        null,
                        null,
                        IllegalArgumentException.class,
                        PRODUCT_CATEGORY_IS_REQUIRED

                ),
                Arguments.of(
                        "Product.2",
                        null,
                        UUID.randomUUID(),
                        null,
                        null,
                        null,
                        IllegalArgumentException.class,
                        PRODUCT_TYPE_IS_REQUIRED

                ),
                Arguments.of(
                        "Product.3",
                        null,
                        UUID.randomUUID(),
                        "STOCK",
                        null,
                        null,
                        IllegalArgumentException.class,
                        PRODUCT_PURCHASE_UOM_IS_REQUIRED_WHEN_TYPE_IS_STOCK
                ),
                Arguments.of(
                        "Product.1",
                        null,
                        UUID.randomUUID(),
                        "STOCK",
                        UUID.randomUUID(),
                        null,
                        IllegalArgumentException.class,
                        PRODUCT_STOCK_UOM_IS_REQUIRED_WHEN_TYPE_IS_STOCK
                )
        );
    }

    @ParameterizedTest
    @MethodSource("createProductThrowExceptionMethodSource")
    void should_throw_exception_when_create_product_with_missing_required_attributes(
        String name,
        String image,
        UUID categoryId,
        String type,
        UUID stockUomId,
        UUID purchaseUomId,
        Class<? extends IllegalArgumentException> exceptionClass,
        String exceptionMessage
    ) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        //Given
        CreateProductViewRequest createProductViewRequest = new CreateProductViewRequest()
                .name(name)
                .image(image)
                .categoryId(categoryId)
                .type(type == null? null: CreateProductViewRequest.TypeEnum.valueOf(type))
                .stockUomId(stockUomId)
                .purchaseUomId(purchaseUomId);

        Product productRequest = Product.builder()
                .name(name == null || name.isEmpty()? null: Name.of(Text.of(name)))
                .image(image == null || image.isEmpty()? null: Image.with(Text.of(image)))
                .type(type == null? null: ProductType.valueOf(type))
                .categoryId(new ProductCategoryId(categoryId))
                .stockUomId(stockUomId == null? null: new UomId(stockUomId))
                .purchaseUomId(purchaseUomId == null? null: new UomId(purchaseUomId))
                .build();

        when(productViewMapper.toProduct(createProductViewRequest)).thenReturn(productRequest);
        when(productService.createProduct(productRequest))
                .thenThrow(exceptionClass.getConstructor(String.class).newInstance(exceptionMessage));

        ArgumentCaptor<CreateProductViewRequest> createProductViewRequestArgumentCaptor =
                ArgumentCaptor.forClass(CreateProductViewRequest.class);
        ArgumentCaptor<Product> productArgumentCaptor = ArgumentCaptor.forClass(Product.class);

        //Act
        assertThatThrownBy(() -> productApiAdapterService.createProduct(createProductViewRequest))
                .isInstanceOf(exceptionClass)
                .hasMessage(exceptionMessage);

        verify(productViewMapper, times(1)).toProduct(createProductViewRequestArgumentCaptor.capture());
        verify(productService, times(1)).createProduct(productArgumentCaptor.capture());

        assertThat(createProductViewRequestArgumentCaptor.getValue()).isEqualTo(createProductViewRequest);
        assertThat(productArgumentCaptor.getValue()).isEqualTo(productRequest);
    }

    @Test
    void should_throw_exception_when_create_product_with_duplicate_name() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        //Given
        String name = "Product.1";
        UUID categoryId = UUID.randomUUID();
        CreateProductViewRequest createProductViewRequest = new CreateProductViewRequest()
                .name(name)
                .categoryId(categoryId)
                .type(CreateProductViewRequest.TypeEnum.SERVICE);

        Product productRequest = Product.builder()
                .name(Name.of(Text.of(name)))
                .type(ProductType.SERVICE)
                .categoryId(new ProductCategoryId(categoryId))
                .build();

        when(productViewMapper.toProduct(createProductViewRequest)).thenReturn(productRequest);
        when(productService.createProduct(productRequest))
                .thenThrow(ProductNameConflictException.class.getConstructor(Object[].class).newInstance(new Object[] {new String[]{name}}));

        ArgumentCaptor<CreateProductViewRequest> createProductViewRequestArgumentCaptor =
                ArgumentCaptor.forClass(CreateProductViewRequest.class);
        ArgumentCaptor<Product> productArgumentCaptor = ArgumentCaptor.forClass(Product.class);

        //Act
        assertThatThrownBy(() -> productApiAdapterService.createProduct(createProductViewRequest))
                .isInstanceOf(ProductNameConflictException.class)
                .hasMessage(PRODUCT_NAME_CONFLICT_EXCEPTION);

        verify(productViewMapper, times(1)).toProduct(createProductViewRequestArgumentCaptor.capture());
        verify(productService, times(1)).createProduct(productArgumentCaptor.capture());

        assertThat(createProductViewRequestArgumentCaptor.getValue()).isEqualTo(createProductViewRequest);
        assertThat(productArgumentCaptor.getValue()).isEqualTo(productRequest);
    }


    @Test
    void should_throw_exception_when_create_product_with_not_found_category() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        //Given
        String name = "Product.1";
        UUID categoryId = UUID.randomUUID();
        CreateProductViewRequest createProductViewRequest = new CreateProductViewRequest()
                .name(name)
                .categoryId(categoryId)
                .type(CreateProductViewRequest.TypeEnum.SERVICE);

        Product productRequest = Product.builder()
                .name(Name.of(Text.of(name)))
                .type(ProductType.SERVICE)
                .categoryId(new ProductCategoryId(categoryId))
                .build();

        when(productViewMapper.toProduct(createProductViewRequest)).thenReturn(productRequest);
        when(productService.createProduct(productRequest))
                .thenThrow(ProductCategoryNotFoundException.class.getConstructor(Object[].class)
                        .newInstance(new Object[] {new String[]{productRequest.getCategoryId().getValue().toString()}}));

        ArgumentCaptor<CreateProductViewRequest> createProductViewRequestArgumentCaptor =
                ArgumentCaptor.forClass(CreateProductViewRequest.class);
        ArgumentCaptor<Product> productArgumentCaptor = ArgumentCaptor.forClass(Product.class);

        //Act
        assertThatThrownBy(() -> productApiAdapterService.createProduct(createProductViewRequest))
                .isInstanceOf(ProductCategoryNotFoundException.class)
                .hasMessage(PRODUCT_CATEGORY_NOT_FOUND_EXCEPTION);

        verify(productViewMapper, times(1)).toProduct(createProductViewRequestArgumentCaptor.capture());
        verify(productService, times(1)).createProduct(productArgumentCaptor.capture());

        assertThat(createProductViewRequestArgumentCaptor.getValue()).isEqualTo(createProductViewRequest);
        assertThat(productArgumentCaptor.getValue()).isEqualTo(productRequest);
    }

    @Test
    void should_throw_exception_when_create_product_with_not_found_stock_uom_or_purchase_uom() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        //Given
        String name = "Product.1";
        UUID categoryId = UUID.randomUUID();
        UUID stockUomId = UUID.randomUUID();
        UUID purchaseUomId = UUID.randomUUID();

        CreateProductViewRequest createProductViewRequest = new CreateProductViewRequest()
                .name(name)
                .categoryId(categoryId)
                .type(CreateProductViewRequest.TypeEnum.STOCK)
                .purchaseUomId(purchaseUomId)
                .stockUomId(stockUomId);

        Product productRequest = Product.builder()
                .name(Name.of(Text.of(name)))
                .type(ProductType.STOCK)
                .stockUomId(new UomId(stockUomId))
                .purchaseUomId(new UomId(purchaseUomId))
                .categoryId(new ProductCategoryId(categoryId))
                .build();

        when(productViewMapper.toProduct(createProductViewRequest)).thenReturn(productRequest);
        when(productService.createProduct(productRequest))
                .thenThrow(ProductUomNotFoundException.class.getConstructor(Object[].class)
                        .newInstance(new Object[] {new String[]{productRequest.getPurchaseUomId().getValue().toString()}}));

        ArgumentCaptor<CreateProductViewRequest> createProductViewRequestArgumentCaptor =
                ArgumentCaptor.forClass(CreateProductViewRequest.class);
        ArgumentCaptor<Product> productArgumentCaptor = ArgumentCaptor.forClass(Product.class);

        //Act
        assertThatThrownBy(() -> productApiAdapterService.createProduct(createProductViewRequest))
                .isInstanceOf(ProductUomNotFoundException.class)
                .hasMessage(PRODUCT_UOM_NOT_FOUND_EXCEPTION);

        verify(productViewMapper, times(1)).toProduct(createProductViewRequestArgumentCaptor.capture());
        verify(productService, times(1)).createProduct(productArgumentCaptor.capture());

        assertThat(createProductViewRequestArgumentCaptor.getValue()).isEqualTo(createProductViewRequest);
        assertThat(productArgumentCaptor.getValue()).isEqualTo(productRequest);
    }

    @Test
    void should_throw_exception_when_create_product_with_uom_where_uom_category_is_different() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        //Given
        String name = "Product.1";
        UUID categoryId = UUID.randomUUID();
        UUID stockUomId = UUID.randomUUID();
        UUID purchaseUomId = UUID.randomUUID();

        CreateProductViewRequest createProductViewRequest = new CreateProductViewRequest()
                .name(name)
                .categoryId(categoryId)
                .type(CreateProductViewRequest.TypeEnum.STOCK)
                .purchaseUomId(purchaseUomId)
                .stockUomId(stockUomId);

        Product productRequest = Product.builder()
                .name(Name.of(Text.of(name)))
                .type(ProductType.STOCK)
                .stockUomId(new UomId(stockUomId))
                .purchaseUomId(new UomId(purchaseUomId))
                .categoryId(new ProductCategoryId(categoryId))
                .build();

        when(productViewMapper.toProduct(createProductViewRequest)).thenReturn(productRequest);
        when(productService.createProduct(productRequest))
                .thenThrow(ProductStockAndPurchaseUomBadException.class.getConstructor(Object[].class)
                        .newInstance(new Object[] {new String[]{
                                productRequest.getStockUomId().getValue().toString(),
                                productRequest.getPurchaseUomId().getValue().toString()}}));

        ArgumentCaptor<CreateProductViewRequest> createProductViewRequestArgumentCaptor =
                ArgumentCaptor.forClass(CreateProductViewRequest.class);
        ArgumentCaptor<Product> productArgumentCaptor = ArgumentCaptor.forClass(Product.class);

        //Act
        assertThatThrownBy(() -> productApiAdapterService.createProduct(createProductViewRequest))
                .isInstanceOf(ProductStockAndPurchaseUomBadException.class)
                .hasMessage(PRODUCT_STOCK_AND_PURCHASE_UOM_BAD_EXCEPTION);

        verify(productViewMapper, times(1)).toProduct(createProductViewRequestArgumentCaptor.capture());
        verify(productService, times(1)).createProduct(productArgumentCaptor.capture());

        assertThat(createProductViewRequestArgumentCaptor.getValue()).isEqualTo(createProductViewRequest);
        assertThat(productArgumentCaptor.getValue()).isEqualTo(productRequest);
    }
}
