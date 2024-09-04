package cm.xenonbyte.farmbyte.catalog.adapter.rest.api.product;

import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.CreateProductViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.CreateProductViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.FindProductByIdViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.FindProductsPageInfoViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.FindProductsViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.SearchProductsPageInfoViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.SearchProductsViewResponse;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.Product;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductCategoryId;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductCategoryNotFoundException;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductId;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductNameConflictException;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductNotFoundException;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductStockAndPurchaseUomBadException;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductType;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductUomNotFoundException;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.Purchasable;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.Sellable;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ports.primary.ProductService;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomId;
import cm.xenonbyte.farmbyte.common.domain.ports.primary.StorageManager;
import cm.xenonbyte.farmbyte.common.domain.vo.Active;
import cm.xenonbyte.farmbyte.common.domain.vo.Direction;
import cm.xenonbyte.farmbyte.common.domain.vo.Filename;
import cm.xenonbyte.farmbyte.common.domain.vo.Keyword;
import cm.xenonbyte.farmbyte.common.domain.vo.Money;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.PageInfo;
import cm.xenonbyte.farmbyte.common.domain.vo.Reference;
import cm.xenonbyte.farmbyte.common.domain.vo.StorageLocation;
import cm.xenonbyte.farmbyte.common.domain.vo.Text;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_CATEGORY_IS_REQUIRED;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_CATEGORY_NOT_FOUND_EXCEPTION;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_NAME_CONFLICT_EXCEPTION;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_NAME_IS_REQUIRED;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_NOT_FOUND_EXCEPTION;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_PURCHASE_UOM_IS_REQUIRED_WHEN_TYPE_IS_STOCK;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_STOCK_AND_PURCHASE_UOM_BAD_EXCEPTION;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_STOCK_UOM_IS_REQUIRED_WHEN_TYPE_IS_STOCK;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_TYPE_IS_REQUIRED;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_UOM_NOT_FOUND_EXCEPTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
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
final class ProductDomainServiceApiAdapterTest {

    private ProductServiceRestApiAdapter productApiAdapterService;
    @Mock
    private ProductService productService;
    @Mock
    private ProductViewMapper productViewMapper;
    @Mock
    private StorageManager storageManager;

    MockMultipartFile multipartFile;
    StorageLocation location;
    Filename fileName;

    @BeforeEach
    void setUp() throws IOException {
        productApiAdapterService = new ProductDomainServiceRestApiAdapter(productService, productViewMapper, storageManager);
        multipartFile = new MockMultipartFile(
                "image",
        "image.jpg",
            "image/jpeg", "<<image data>>".getBytes(StandardCharsets.UTF_8)
        );
        location = StorageLocation.computeStoragePtah("products");
        fileName = Filename.of(Text.of(location.getPath().getValue()));
        ReflectionTestUtils.setField(productApiAdapterService, "rootPathStorageProducts", "products");
    }

    @Nested
    class CreateProductDomainServiceApiAdapterTest {

        static Stream<Arguments> createProductMethodSource() {
            return Stream.of(
                    Arguments.of(
                            "Product.1",
                            UUID.randomUUID(),
                            "SERVICE",
                            UUID.randomUUID(),
                            null,
                            null
                    ),
                    Arguments.of(
                            "Product.2",
                            UUID.randomUUID(),
                            "CONSUMABLE",
                            UUID.randomUUID(),
                            null,
                            null
                    ),
                    Arguments.of(
                            "Product.3",
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
                UUID categoryId,
                String type,
                UUID productId,
                UUID stockUomId,
                UUID purchaseUomId
        ) throws IOException {

            //Given
            CreateProductViewRequest createProductViewRequest = new CreateProductViewRequest()
                    .name(name)
                    .categoryId(categoryId)
                    .type(CreateProductViewRequest.TypeEnum.valueOf(type))
                    .stockUomId(stockUomId)
                    .purchaseUomId(purchaseUomId);

            Product productRequest = Product.builder()
                    .name(Name.of(Text.of(name)))
                    .type(ProductType.valueOf(type))
                    .categoryId(new ProductCategoryId(categoryId))
                    .stockUomId(stockUomId == null? null: new UomId(stockUomId))
                    .purchaseUomId(purchaseUomId == null? null: new UomId(purchaseUomId))
                    .build();

            Product productResponse= Product.builder()
                    .id(new ProductId(productId))
                    .name(Name.of(Text.of(name)))
                    .type(ProductType.valueOf(type))
                    .categoryId(new ProductCategoryId(categoryId))
                    .stockUomId(stockUomId == null? null: new UomId(stockUomId))
                    .purchaseUomId(purchaseUomId == null? null: new UomId(purchaseUomId))
                    .build();

            CreateProductViewResponse createProductViewResponse = new CreateProductViewResponse()
                    .name(name)
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

            when(storageManager.store(any(), any())).thenReturn(fileName);
            when(productViewMapper.toProduct(createProductViewRequest)).thenReturn(productRequest);
            when(productService.createProduct(productRequest)).thenReturn(productResponse);
            when(productViewMapper.toCreateProductViewResponse(productResponse)).thenReturn(createProductViewResponse);

            ArgumentCaptor<CreateProductViewRequest> createProductViewRequestArgumentCaptor =
                    ArgumentCaptor.forClass(CreateProductViewRequest.class);

            ArgumentCaptor<Product> productArgumentCaptor = ArgumentCaptor.forClass(Product.class);

            //Act
            CreateProductViewResponse result = productApiAdapterService.createProduct(createProductViewRequest, multipartFile);

            //Then
            assertThat(result).isNotNull().isEqualTo(createProductViewResponse);

            verify(storageManager).store(any(), any());
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
                            "SERVICE",
                            null,
                            null,
                            IllegalArgumentException.class,
                            PRODUCT_CATEGORY_IS_REQUIRED

                    ),
                    Arguments.of(
                            "Product.2",
                            UUID.randomUUID(),
                            null,
                            null,
                            null,
                            IllegalArgumentException.class,
                            PRODUCT_TYPE_IS_REQUIRED

                    ),
                    Arguments.of(
                            "Product.3",
                            UUID.randomUUID(),
                            "STOCK",
                            null,
                            null,
                            IllegalArgumentException.class,
                            PRODUCT_PURCHASE_UOM_IS_REQUIRED_WHEN_TYPE_IS_STOCK
                    ),
                    Arguments.of(
                            "Product.1",
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
                    .categoryId(categoryId)
                    .type(type == null? null: CreateProductViewRequest.TypeEnum.valueOf(type))
                    .stockUomId(stockUomId)
                    .purchaseUomId(purchaseUomId);

            Product productRequest = Product.builder()
                    .name(name == null || name.isEmpty()? null: Name.of(Text.of(name)))
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
            assertThatThrownBy(() -> productApiAdapterService.createProduct(createProductViewRequest, multipartFile))
                    .isInstanceOf(exceptionClass)
                    .hasMessage(exceptionMessage);

            //TODO verify(storageManager).store(any(), any());
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
            assertThatThrownBy(() -> productApiAdapterService.createProduct(createProductViewRequest, multipartFile))
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
            assertThatThrownBy(() -> productApiAdapterService.createProduct(createProductViewRequest, multipartFile))
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
            assertThatThrownBy(() -> productApiAdapterService.createProduct(createProductViewRequest, multipartFile))
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
            assertThatThrownBy(() -> productApiAdapterService.createProduct(createProductViewRequest, multipartFile))
                    .isInstanceOf(ProductStockAndPurchaseUomBadException.class)
                    .hasMessage(PRODUCT_STOCK_AND_PURCHASE_UOM_BAD_EXCEPTION);

            verify(productViewMapper, times(1)).toProduct(createProductViewRequestArgumentCaptor.capture());
            verify(productService, times(1)).createProduct(productArgumentCaptor.capture());

            assertThat(createProductViewRequestArgumentCaptor.getValue()).isEqualTo(createProductViewRequest);
            assertThat(productArgumentCaptor.getValue()).isEqualTo(productRequest);
        }
    }

    @Nested
    class FindProductDomainServiceApiAdapterTest {
        @Test
        void should_success_when_find_product_by_existing_id() {
            //Given
            UUID productIdUUID = UUID.randomUUID();
            UUID categoryIdUUID = UUID.randomUUID();
            ProductId productId = new ProductId(productIdUUID);

            Product product = Product.builder()
                    .id(productId)
                    .name(Name.of(Text.of("Mac Book Pro 2023")))
                    .categoryId(new ProductCategoryId(categoryIdUUID))
                    .type(ProductType.CONSUMABLE)
                    .reference(Reference.of(Text.of("20123546")))
                    .purchasePrice(Money.of(BigDecimal.valueOf(250.35)))
                    .salePrice(Money.of(BigDecimal.valueOf(475.41)))
                    .sellable(Sellable.with(true))
                    .purchasable(Purchasable.with(true))
                    .active(Active.with(true))
                    .build();

            FindProductByIdViewResponse findProductByIdViewResponse = new FindProductByIdViewResponse()
                    .id(productIdUUID)
                    .name("Mac Book Pro 2023")
                    .categoryId(categoryIdUUID)
                    .type(FindProductByIdViewResponse.TypeEnum.CONSUMABLE)
                    .reference("20123546")
                    .purchasePrice(BigDecimal.valueOf(250.35))
                    .salePrice(BigDecimal.valueOf(475.41))
                    .sellable(true)
                    .purchasable(true)
                    .active(true);

            when(productService.findProductById(productId)).thenReturn(product);
            when(productViewMapper.toFindProductByIdViewResponse(product)).thenReturn(findProductByIdViewResponse);

            ArgumentCaptor<ProductId> prodductIdArgumentCaptor = ArgumentCaptor.forClass(ProductId.class);
            ArgumentCaptor<Product> productArgumentCaptor = ArgumentCaptor.forClass(Product.class);

            //Act
            FindProductByIdViewResponse result = productApiAdapterService.findProductById(productIdUUID);

            //Then
            assertThat(result)
                    .isNotNull()
                    .isEqualTo(findProductByIdViewResponse);

            verify(productService, times(1)).findProductById(prodductIdArgumentCaptor.capture());
            verify(productViewMapper, times(1)).toFindProductByIdViewResponse(productArgumentCaptor.capture());

            assertThat(prodductIdArgumentCaptor.getValue()).isEqualTo(productId);
            assertThat(productArgumentCaptor.getValue()).isEqualTo(product);
        }

        @Test
        void should_fail_when_find_product_by_id_with_non_existing_id() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
            //Given
            UUID productId = UUID.randomUUID();
            when(productService.findProductById(any())).thenThrow(ProductNotFoundException.class.getConstructor(Object[].class).newInstance(new Object[]{new String[]{productId.toString()}}));
            //Act + Then
            assertThatThrownBy(() -> productApiAdapterService.findProductById(productId))
                    .isInstanceOf(ProductNotFoundException.class)
                    .hasMessage(PRODUCT_NOT_FOUND_EXCEPTION);
        }
    }

    @Nested
    class FindProductsDomainServiceApiAdapterTest {

        @Test
        void should_success_when_find_products() {
            //Given
            int page = 0;
            int size = 2;
            String attribute = "name";
            Direction direction = Direction.ASC;

            PageInfo<Product> productPageInfo = new PageInfo<Product>().with(
                    page, size, List.of(
                        Product.builder()
                                .id(new ProductId(UUID.randomUUID()))
                                .name(Name.of(Text.of("Mac Book Pro 2023")))
                                .categoryId(new ProductCategoryId(UUID.randomUUID()))
                                .type(ProductType.CONSUMABLE)
                                .reference(Reference.of(Text.of("20123546")))
                                .purchasePrice(Money.of(BigDecimal.valueOf(250.35)))
                                .salePrice(Money.of(BigDecimal.valueOf(475.41)))
                                .sellable(Sellable.with(true))
                                .purchasable(Purchasable.with(true))
                                .active(Active.with(true))
                                .build(),
                        Product.builder()
                                .id(new ProductId(UUID.randomUUID()))
                                .name(Name.of(Text.of("HP Pro")))
                                .categoryId(new ProductCategoryId(UUID.randomUUID()))
                                .type(ProductType.CONSUMABLE)
                                .reference(Reference.of(Text.of("65489456")))
                                .purchasePrice(Money.of(BigDecimal.valueOf(175.47)))
                                .salePrice(Money.of(BigDecimal.valueOf(350.12)))
                                .sellable(Sellable.with(true))
                                .purchasable(Purchasable.with(true))
                                .active(Active.with(true))
                                .build(),
                        Product.builder()
                                .id(new ProductId(UUID.randomUUID()))
                                .name(Name.of(Text.of("DELL Pro")))
                                .categoryId(new ProductCategoryId(UUID.randomUUID()))
                                .type(ProductType.CONSUMABLE)
                                .reference(Reference.of(Text.of("159989561")))
                                .purchasePrice(Money.of(BigDecimal.valueOf(223.95)))
                                .salePrice(Money.of(BigDecimal.valueOf(410.12)))
                                .sellable(Sellable.with(true))
                                .purchasable(Purchasable.with(true))
                                .active(Active.with(true))
                                .build()
                    )
            );

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

            when(productService.findProducts(page, size, attribute, direction)).thenReturn(productPageInfo);
            when(productViewMapper.toFindProductsPageInfoViewResponse(productPageInfo)).thenReturn(findProductsPageInfoViewResponse);

            ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
            ArgumentCaptor<Direction> directionArgumentCaptor = ArgumentCaptor.forClass(Direction.class);
            ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
            ArgumentCaptor<PageInfo> pageInfoArgumentCaptor = ArgumentCaptor.forClass(PageInfo.class);

            //Act
            FindProductsPageInfoViewResponse result = productApiAdapterService.findProducts(page, size, attribute, direction);

            //Then
            assertThat(result)
                    .isNotNull()
                    .isEqualTo(findProductsPageInfoViewResponse);

            verify(productService, times(1))
                    .findProducts(integerArgumentCaptor.capture(), integerArgumentCaptor.capture(),
                            stringArgumentCaptor.capture(), directionArgumentCaptor.capture());

            verify(productViewMapper, times(1)).toFindProductsPageInfoViewResponse(pageInfoArgumentCaptor.capture());

            assertThat(integerArgumentCaptor.getAllValues().get(0)).isEqualTo(page);
            assertThat(integerArgumentCaptor.getAllValues().get(1)).isEqualTo(size);
            assertThat(stringArgumentCaptor.getAllValues().get(0)).isEqualTo(attribute);
            assertThat(directionArgumentCaptor.getAllValues().get(0)).isEqualTo(Direction.ASC);
            assertThat(pageInfoArgumentCaptor.getValue()).isEqualTo(productPageInfo);
        }
    }

    @Nested
    class SearchProductsDomainServiceApiAdapterTest {

        @Test
        void should_success_when_search_products_with_key_word() {
            //Given
            int page = 0;
            int size = 2;
            String attribute = "name";
            Direction direction = Direction.ASC;
            String keyword = "p";

            PageInfo<Product> productPageInfo = new PageInfo<Product>().with(
                    page, size, List.of(
                            Product.builder()
                                    .id(new ProductId(UUID.randomUUID()))
                                    .name(Name.of(Text.of("Mac Book Pro 2023")))
                                    .categoryId(new ProductCategoryId(UUID.randomUUID()))
                                    .type(ProductType.CONSUMABLE)
                                    .reference(Reference.of(Text.of("20123546")))
                                    .purchasePrice(Money.of(BigDecimal.valueOf(250.35)))
                                    .salePrice(Money.of(BigDecimal.valueOf(475.41)))
                                    .sellable(Sellable.with(true))
                                    .purchasable(Purchasable.with(true))
                                    .active(Active.with(true))
                                    .build(),
                            Product.builder()
                                    .id(new ProductId(UUID.randomUUID()))
                                    .name(Name.of(Text.of("HP Pro")))
                                    .categoryId(new ProductCategoryId(UUID.randomUUID()))
                                    .type(ProductType.CONSUMABLE)
                                    .reference(Reference.of(Text.of("65489456")))
                                    .purchasePrice(Money.of(BigDecimal.valueOf(175.47)))
                                    .salePrice(Money.of(BigDecimal.valueOf(350.12)))
                                    .sellable(Sellable.with(true))
                                    .purchasable(Purchasable.with(true))
                                    .active(Active.with(true))
                                    .build(),
                            Product.builder()
                                    .id(new ProductId(UUID.randomUUID()))
                                    .name(Name.of(Text.of("DELL Pro")))
                                    .categoryId(new ProductCategoryId(UUID.randomUUID()))
                                    .type(ProductType.CONSUMABLE)
                                    .reference(Reference.of(Text.of("159989561")))
                                    .purchasePrice(Money.of(BigDecimal.valueOf(223.95)))
                                    .salePrice(Money.of(BigDecimal.valueOf(410.12)))
                                    .sellable(Sellable.with(true))
                                    .purchasable(Purchasable.with(true))
                                    .active(Active.with(true))
                                    .build()
                    )
            );

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

            when(productService.searchProducts(page, size, attribute, direction, Keyword.of(Text.of(keyword)))).thenReturn(productPageInfo);
            when(productViewMapper.toSearchProductsPageInfoViewResponse(productPageInfo)).thenReturn(searchProductsPageInfoViewResponse);

            ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
            ArgumentCaptor<Direction> directionArgumentCaptor = ArgumentCaptor.forClass(Direction.class);
            ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
            ArgumentCaptor<PageInfo> pageInfoArgumentCaptor = ArgumentCaptor.forClass(PageInfo.class);
            ArgumentCaptor<Keyword> keywordArgumentCaptor = ArgumentCaptor.forClass(Keyword.class);

            //Act
            SearchProductsPageInfoViewResponse result = productApiAdapterService.searchProducts(page, size, attribute, direction, keyword);

            //Then
            assertThat(result)
                    .isNotNull()
                    .isEqualTo(searchProductsPageInfoViewResponse);

            verify(productService, times(1))
                    .searchProducts(integerArgumentCaptor.capture(), integerArgumentCaptor.capture(),
                            stringArgumentCaptor.capture(), directionArgumentCaptor.capture(), keywordArgumentCaptor.capture());

            verify(productViewMapper, times(1)).toSearchProductsPageInfoViewResponse(pageInfoArgumentCaptor.capture());

            assertThat(integerArgumentCaptor.getAllValues().get(0)).isEqualTo(page);
            assertThat(integerArgumentCaptor.getAllValues().get(1)).isEqualTo(size);
            assertThat(stringArgumentCaptor.getAllValues().get(0)).isEqualTo(attribute);
            assertThat(directionArgumentCaptor.getAllValues().get(0)).isEqualTo(Direction.ASC);
            assertThat(pageInfoArgumentCaptor.getValue()).isEqualTo(productPageInfo);
        }
    }
}
