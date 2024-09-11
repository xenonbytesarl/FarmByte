package cm.xenonbyte.farmbyte.catalog.domain.test;

import cm.xenonbyte.farmbyte.catalog.adapter.data.access.inmemory.ProductCategoryInMemoryRepositoryAdapter;
import cm.xenonbyte.farmbyte.catalog.adapter.data.access.inmemory.ProductInMemoryRepositoryAdapter;
import cm.xenonbyte.farmbyte.catalog.adapter.data.access.inmemory.UomInMemoryRepositoryAdapter;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.Product;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductCategory;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductCategoryId;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductCategoryNotFoundException;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductId;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductNameConflictException;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductDomainService;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductNotFoundException;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductReferenceConflictException;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductStockAndPurchaseUomBadException;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductType;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductUomNotFoundException;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.Purchasable;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.Sellable;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ports.primary.ProductService;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ports.secondary.ProductCategoryRepository;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ports.secondary.ProductRepository;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.Uom;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategoryId;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomId;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomType;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.ports.secondary.UomRepository;
import cm.xenonbyte.farmbyte.common.domain.vo.Active;
import cm.xenonbyte.farmbyte.common.domain.vo.Direction;
import cm.xenonbyte.farmbyte.common.domain.vo.Keyword;
import cm.xenonbyte.farmbyte.common.domain.vo.Money;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.PageInfo;
import cm.xenonbyte.farmbyte.common.domain.vo.Ratio;
import cm.xenonbyte.farmbyte.common.domain.vo.Reference;
import cm.xenonbyte.farmbyte.common.domain.vo.Text;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.stream.Stream;

import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_CATEGORY_IS_REQUIRED;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_CATEGORY_NOT_FOUND_EXCEPTION;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_NAME_CONFLICT_EXCEPTION;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_NAME_IS_REQUIRED;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_NOT_FOUND_EXCEPTION;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_PURCHASE_PRICE_SHOULD_BE_GREATER_THAN_ZERO;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_PURCHASE_UOM_IS_REQUIRED_WHEN_TYPE_IS_STOCK;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_REFERENCE_CONFLICT_EXCEPTION;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_SALE_PRICE_SHOULD_BE_GREATER_THAN_ZERO;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_STOCK_AND_PURCHASE_UOM_BAD_EXCEPTION;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_STOCK_UOM_IS_REQUIRED_WHEN_TYPE_IS_STOCK;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_TYPE_IS_REQUIRED;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_UOM_NOT_FOUND_EXCEPTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author bamk
 * @version 1.0
 * @since 20/08/2024
 */
public final class ProductDomainServiceTest {

    private ProductService productService;
    private ProductRepository productRepository;
    private ProductCategoryRepository productCategoryRepository;
    private UomRepository uomRepository;

    static String categoryId = "01918e3e-18a6-70bd-92c7-9bf1927b50e7";
    static String stockUomIdUUID = "01918e3e-419f-7042-ade1-c37c03bff951";
    static String purchaseUomIdUUID = "01918e3e-5ae0-7a23-86e9-64ae98b3aabc";

    @BeforeEach
    void setUp() {
        productRepository = new ProductInMemoryRepositoryAdapter();
        uomRepository = new UomInMemoryRepositoryAdapter();
        productCategoryRepository = new ProductCategoryInMemoryRepositoryAdapter();
        productService = new ProductDomainService(
                productRepository, uomRepository, productCategoryRepository);


        saveProductCategory(
                ProductCategory.builder()
                        .id(new ProductCategoryId(UUID.fromString(categoryId)))
                        .name(Name.of(Text.of("ProductCategory.1")))
                        .build()
        );

        saveUom(
                Uom.builder()
                        .id(new UomId(UUID.fromString(stockUomIdUUID)))
                        .name(Name.of(Text.of("Unite")))
                        .uomCategoryId(new UomCategoryId(UUID.fromString("01918e47-0e3d-7d2b-9897-f556fcdca51d")))
                        .uomType(UomType.REFERENCE)
                        .build()
        );

        saveUom(
                Uom.builder()
                        .id(new UomId(UUID.fromString(purchaseUomIdUUID)))
                        .name(Name.of(Text.of("Carton 10")))
                        .uomCategoryId(new UomCategoryId(UUID.fromString("01918e47-0e3d-7d2b-9897-f556fcdca51d")))
                        .uomType(UomType.GREATER)
                        .ratio(Ratio.of(10.0))
                        .build()
        );

    }

    @Nested
    class CreateProductDomainServiceTest {

        static Stream<Arguments> createProductMethodSource() {
            return Stream.of(
                    Arguments.of(
                            Name.of(Text.of("Chair")),
                            new ProductCategoryId(UUID.fromString("01918e3e-18a6-70bd-92c7-9bf1927b50e7")),
                            ProductType.STOCK,
                            new UomId(UUID.fromString("01918e3e-419f-7042-ade1-c37c03bff951")),
                            new UomId(UUID.fromString("01918e3e-5ae0-7a23-86e9-64ae98b3aabc"))
                    ),
                    Arguments.of(
                            Name.of(Text.of("Chair")),
                            new ProductCategoryId(UUID.fromString("01918e3e-18a6-70bd-92c7-9bf1927b50e7")),
                            ProductType.CONSUMABLE,
                            null,
                            null
                    ),
                    Arguments.of(
                            Name.of(Text.of("Chair")),
                            new ProductCategoryId(UUID.fromString("01918e3e-18a6-70bd-92c7-9bf1927b50e7")),
                            ProductType.SERVICE,
                            null,
                            null
                    )
            );
        }

        @ParameterizedTest
        @MethodSource("createProductMethodSource")
        void should_create_product(
                Name name,
                ProductCategoryId categoryId,
                ProductType type,
                UomId stockUomId,
                UomId purschaseUomId
        ) {
            //Given
            Product product = Product.builder()
                    .name(name)
                    .categoryId(categoryId)
                    .type(type)
                    .stockUomId(stockUomId)
                    .purchaseUomId(purschaseUomId)
                    .build();
            //Act
            Product createdProduct = productService.createProduct(product);

            //Then
            assertThat(createdProduct).isNotNull();
            assertThat(createdProduct.getId())
                    .isNotNull()
                    .satisfies(cp -> assertThat(cp.getValue()).isInstanceOf(UUID.class));
            assertThat(createdProduct.getName()).isEqualTo(product.getName());
            assertThat(createdProduct.getCategoryId()).isEqualTo(product.getCategoryId());
            assertThat(createdProduct.getStockUomId()).isEqualTo(product.getStockUomId());
            assertThat(createdProduct.getPurchaseUomId()).isEqualTo(product.getPurchaseUomId());
            assertThat(createdProduct.getType()).isEqualTo(product.getType());
            assertThat(createdProduct.getReference()).isEqualTo(product.getReference());
            assertThat(createdProduct.getImageName()).isEqualTo(product.getImageName());
            assertThat(createdProduct.getSalePrice()).isEqualTo(product.getSalePrice());
            assertThat(createdProduct.getPurchasePrice()).isEqualTo(product.getPurchasePrice());
            assertThat(createdProduct.getActive().getValue()).isTrue();
            assertThat(createdProduct.getSellable().getValue()).isFalse();
            assertThat(createdProduct.getPurchasable().getValue()).isFalse();
        }

        static Stream<Arguments> createProductThrowExceptionMethodSource() {
            return Stream.of(
                    Arguments.of(
                            null,
                            new ProductCategoryId(UUID.fromString(categoryId)),
                            ProductType.CONSUMABLE,
                            null,
                            null,
                            null,
                            null,
                            IllegalArgumentException.class,
                            PRODUCT_NAME_IS_REQUIRED
                    ),
                    Arguments.of(
                            Name.of(Text.of("Chair")),
                            null,
                            ProductType.CONSUMABLE,
                            null,
                            null,
                            null,
                            null,
                            IllegalArgumentException.class,
                            PRODUCT_CATEGORY_IS_REQUIRED
                    ),
                    Arguments.of(
                            Name.of(Text.of("Chair")),
                            new ProductCategoryId(UUID.fromString(categoryId)),
                            null,
                            null,
                            null,
                            null,
                            null,
                            IllegalArgumentException.class,
                            PRODUCT_TYPE_IS_REQUIRED
                    ),
                    Arguments.of(
                            Name.of(Text.of("Chair")),
                            new ProductCategoryId(UUID.fromString(categoryId)),
                            ProductType.STOCK,
                            null,
                            null,
                            null,
                            null,
                            IllegalArgumentException.class,
                            PRODUCT_STOCK_UOM_IS_REQUIRED_WHEN_TYPE_IS_STOCK
                    ),
                    Arguments.of(
                            Name.of(Text.of("Chair")),
                            new ProductCategoryId(UUID.fromString(categoryId)),
                            ProductType.STOCK,
                            new UomId(UUID.fromString(stockUomIdUUID)),
                            null,
                            null,
                            null,
                            IllegalArgumentException.class,
                            PRODUCT_PURCHASE_UOM_IS_REQUIRED_WHEN_TYPE_IS_STOCK
                    ),
                    Arguments.of(
                            Name.of(Text.of("Chair")),
                            new ProductCategoryId(UUID.fromString(categoryId)),
                            ProductType.SERVICE,
                            null,
                            null,
                            Money.of(new BigDecimal(-2.0)),
                            null,
                            IllegalArgumentException.class,
                            PRODUCT_PURCHASE_PRICE_SHOULD_BE_GREATER_THAN_ZERO
                    ),
                    Arguments.of(
                            Name.of(Text.of("Chair")),
                            new ProductCategoryId(UUID.fromString(categoryId)),
                            ProductType.SERVICE,
                            null,
                            null,
                            null,
                            Money.of(new BigDecimal(-1.0)),
                            IllegalArgumentException.class,
                            PRODUCT_SALE_PRICE_SHOULD_BE_GREATER_THAN_ZERO
                    )
            );
        }

        @ParameterizedTest
        @MethodSource("createProductThrowExceptionMethodSource")
        void should_throw_exception_when_create_product_of_a_least_of_one_missing_required_field(
                Name name,
                ProductCategoryId categoryId,
                ProductType type,
                UomId stockUomId,
                UomId purschaseUomId,
                Money purchasePrice,
                Money salePrice,
                Class<? extends IllegalArgumentException> expectedException,
                String expectedMessage
        ) {
            //Given
            Product product = Product.builder()
                    .name(name)
                    .categoryId(categoryId)
                    .type(type)
                    .stockUomId(stockUomId)
                    .purchaseUomId(purschaseUomId)
                    .purchasePrice(purchasePrice)
                    .salePrice(salePrice)
                    .build();

            //Act + Then
            assertThatThrownBy(() -> productService.createProduct(product))
                    .isInstanceOf(expectedException)
                    .hasMessage(expectedMessage);
        }

        @Test
        void should_throw_exception_when_two_product_has_same_name() {
            //Given
            Name name = Name.of(Text.of("Chair"));
            saveOneProduct(name);
            Product product = Product.builder()
                    .name(name)
                    .type(ProductType.SERVICE)
                    .categoryId(new ProductCategoryId(UUID.randomUUID()))
                    .build();

            //act
            assertThatThrownBy(() -> productService.createProduct(product))
                    .isInstanceOf(ProductNameConflictException.class)
                    .hasMessage(PRODUCT_NAME_CONFLICT_EXCEPTION);



        }

        @Test
        void should_throw_exception_when_product_category_not_found() {

            //Given
            Product product = Product.builder()
                    .categoryId(new ProductCategoryId(UUID.randomUUID()))
                    .type(ProductType.SERVICE)
                    .name(Name.of(Text.of("Product.1")))
                    .build();
            //Act + Then
            assertThatThrownBy(() -> productService.createProduct(product))
                    .isInstanceOf(ProductCategoryNotFoundException.class)
                    .hasMessage(PRODUCT_CATEGORY_NOT_FOUND_EXCEPTION);
        }

        @Test
        void should_throw_exception_when_stock_uom_not_found() {

            //Given
            Product product = Product.builder()
                    .categoryId(new ProductCategoryId(UUID.fromString(categoryId)))
                    .type(ProductType.STOCK)
                    .stockUomId(new UomId(UUID.randomUUID()))
                    .purchaseUomId(new UomId(UUID.fromString(purchaseUomIdUUID)))
                    .name(Name.of(Text.of("Product.1")))
                    .build();
            //Act + Then
            assertThatThrownBy(() -> productService.createProduct(product))
                    .isInstanceOf(ProductUomNotFoundException.class)
                    .hasMessage(PRODUCT_UOM_NOT_FOUND_EXCEPTION);
        }

        @Test
        void should_throw_exception_when_purchase_uom_not_found() {

            //Given
            Product product = Product.builder()
                    .categoryId(new ProductCategoryId(UUID.fromString(categoryId)))
                    .type(ProductType.STOCK)
                    .stockUomId(new UomId(UUID.fromString(stockUomIdUUID)))
                    .purchaseUomId(new UomId(UUID.randomUUID()))
                    .name(Name.of(Text.of("Product.1")))
                    .build();
            //Act + Then
            assertThatThrownBy(() -> productService.createProduct(product))
                    .isInstanceOf(ProductUomNotFoundException.class)
                    .hasMessage(PRODUCT_UOM_NOT_FOUND_EXCEPTION);
        }



        @Test
        void should_throw_exception_when_create_product_with_uom_category_is_different() {
            //Given

            UomId uomId = new UomId(UUID.randomUUID());
            saveUom(
                    Uom.builder()
                            .id(uomId)
                            .name(Name.of(Text.of("Other Uom")))
                            .uomType(UomType.REFERENCE)
                            .uomCategoryId(new UomCategoryId(UUID.randomUUID()))
                            .build()
            );

            Product product = Product.builder()
                    .name(Name.of(Text.of("Chair")))
                    .type(ProductType.STOCK)
                    .categoryId(new ProductCategoryId(UUID.fromString(categoryId)))
                    .stockUomId(new UomId(UUID.fromString(stockUomIdUUID)))
                    .purchaseUomId(uomId)
                    .build();

            //act
            assertThatThrownBy(() -> productService.createProduct(product))
                    .isInstanceOf(ProductStockAndPurchaseUomBadException.class)
                    .hasMessage(PRODUCT_STOCK_AND_PURCHASE_UOM_BAD_EXCEPTION);

        }
    }

    @Nested
    class FindProductByIdDomainServiceTest {

        private Product product;
        private ProductId productId;

        @BeforeEach
        void setUp() {
            productId = new ProductId(UUID.randomUUID());

            product = Product.builder()
                    .id(productId)
                    .name(Name.of(Text.of("Mac Book Pro 2023")))
                    .categoryId(new ProductCategoryId(UUID.randomUUID()))
                    .type(ProductType.CONSUMABLE)
                    .reference(Reference.of(Text.of("20123546")))
                    .purchasePrice(Money.of(BigDecimal.valueOf(250.35)))
                    .salePrice(Money.of(BigDecimal.valueOf(475.41)))
                    .sellable(Sellable.with(true))
                    .purchasable(Purchasable.with(true))
                    .active(Active.with(true))
                    .build();

            productRepository.save(product);
        }

        @Test
        void should_success_when_find_product_with_existing_id() {

            //Act
            Product result = productService.findProductById(productId);

            //Then
            assertThat(result).isNotNull();
            assertThat(result).isEqualTo(product);
        }

        @Test
        void should_fail_when_find_product_by_non_existing_id() {
            assertThatThrownBy(() -> productService.findProductById(new ProductId(UUID.randomUUID())))
                    .isInstanceOf(ProductNotFoundException.class);
        }
    }

    @Nested
    class FindProductsDomainServiceTest {

        @BeforeEach
        void setUp() {
            productRepository.save(
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
                    .build()
            );
            productRepository.save(
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
                            .build()
            );
            productRepository.save(
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
            );
        }

        @Test
        void should_success_when_find_products() {
            //Given
            int page = 0;
            int size = 2;
            String attribute = "name";
            Direction direction = Direction.ASC;

            //Act
            PageInfo<Product> result = productService.findProducts(page, size, attribute, direction);

            //Then
            assertThat(result.getContent().size()).isGreaterThan(0);
            assertThat(result.getFirst()).isTrue();
            assertThat(result.getLast()).isFalse();
            assertThat(result.getPageSize()).isEqualTo(size);
            assertThat(result.getTotalElements()).isGreaterThan(0);
            assertThat(result.getTotalPages()).isGreaterThan(0);
        }
    }

    @Nested
    class SearchProductDomainServiceTest {

        @BeforeEach
        void setUp() {
            productRepository.save(
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
                            .build()
            );
            productRepository.save(
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
                            .build()
            );
            productRepository.save(
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
            );
        }

        @Test
        void should_success_when_search_product_with_existing_keyword() {
            //Given
            int page = 0;
            int size = 2;
            String attribute = "name";
            Direction direction = Direction.ASC;
            String keyword = "p";

            //Act
            PageInfo<Product> result = productService.searchProducts(page, size, attribute, direction, Keyword.of(Text.of(keyword)));

            //Then
            assertThat(result.getContent().size()).isGreaterThan(0);
            assertThat(result.getFirst()).isTrue();
            assertThat(result.getLast()).isFalse();
            assertThat(result.getPageSize()).isEqualTo(size);
            assertThat(result.getTotalElements()).isGreaterThan(0);
            assertThat(result.getTotalPages()).isGreaterThan(0);
        }
    }

    @Nested
    class UpdateProductDomainServiceTest {
        Product product;
        ProductId productId;
        ProductCategoryId productCategoryId;

        @BeforeEach
        void setUp() {
            productCategoryId = new ProductCategoryId(UUID.fromString("0191e105-e116-7136-b7b8-2e295fc5696a"));

            productId = new ProductId(UUID.fromString("0191e105-fda9-705c-b015-da67f74b981e"));

            productCategoryRepository.save(
                    ProductCategory.builder()
                            .id(productCategoryId)
                            .name(Name.of(Text.of("Computer")))
                            .active(Active.with(true))
                            .build()
            );

            product = productRepository.save(
                    Product.builder()
                        .id(productId)
                        .name(Name.of(Text.of("Mac Book Pro 2023")))
                        .categoryId(productCategoryId)
                        .type(ProductType.CONSUMABLE)
                        .reference(Reference.of(Text.of("20123546")))
                        .purchasePrice(Money.of(BigDecimal.valueOf(250.35)))
                        .salePrice(Money.of(BigDecimal.valueOf(475.41)))
                        .sellable(Sellable.with(true))
                        .purchasable(Purchasable.with(true))
                        .active(Active.with(true))
                        .build()
            );

            productRepository.save(
                    Product.builder()
                            .id(new ProductId(UUID.fromString("0191e126-da9e-724f-af08-c94dd2a0bda8")))
                            .name(Name.of(Text.of("HP Proliant 2023")))
                            .categoryId(productCategoryId)
                            .type(ProductType.STOCK)
                            .reference(Reference.of(Text.of("2120354")))
                            .purchasePrice(Money.of(BigDecimal.valueOf(145.87)))
                            .salePrice(Money.of(BigDecimal.valueOf(378.98)))
                            .sellable(Sellable.with(true))
                            .purchasable(Purchasable.with(true))
                            .stockUomId(new UomId(UUID.fromString(stockUomIdUUID)))
                            .purchaseUomId(new UomId(UUID.fromString(purchaseUomIdUUID)))
                            .active(Active.with(true))
                            .build()
            );
        }

        @Test
        void should_success_when_update_product() {
            //Given
            Product productToUpdate = Product.builder()
                    .id(productId)
                    .name(Name.of(Text.of("New Mac Book Pro 2023")))
                    .categoryId(productCategoryId)
                    .type(ProductType.CONSUMABLE)
                    .reference(Reference.of(Text.of("20123546")))
                    .purchasePrice(Money.of(BigDecimal.valueOf(275.35)))
                    .salePrice(Money.of(BigDecimal.valueOf(490.41)))
                    .sellable(Sellable.with(true))
                    .purchasable(Purchasable.with(true))
                    .active(Active.with(true))
                    .build();
            //Act
            Product result = productService.updateProduct(productId, productToUpdate);

            //Then
            assertThat(result).isNotNull().isEqualTo(productToUpdate);
        }

        @Test
        void should_fail_when_update_product_with_non_existing_id() {
            //Given
            UUID nonExistingProductIdUUID = UUID.fromString("0191e13a-3fe9-757b-a670-937e2190ff5a");

            Product productToUpdate = Product.builder()
                    .id(new ProductId(nonExistingProductIdUUID))
                    .name(Name.of(Text.of("New Mac Book Pro 2023")))
                    .categoryId(productCategoryId)
                    .type(ProductType.CONSUMABLE)
                    .reference(Reference.of(Text.of("20123546")))
                    .purchasePrice(Money.of(BigDecimal.valueOf(275.35)))
                    .salePrice(Money.of(BigDecimal.valueOf(490.41)))
                    .sellable(Sellable.with(true))
                    .purchasable(Purchasable.with(true))
                    .active(Active.with(true))
                    .build();
            //Act + Then
            assertThatThrownBy(() -> productService.updateProduct(new ProductId(nonExistingProductIdUUID), productToUpdate))
                    .isInstanceOf(ProductNotFoundException.class)
                    .hasMessage(PRODUCT_NOT_FOUND_EXCEPTION);
        }

        @Test
        void should_fail_when_update_product_with_non_product_category_id() {
            //Given
            Product productToUpdate = Product.builder()
                    .id(productId)
                    .name(Name.of(Text.of("New Mac Book Pro 2023")))
                    .categoryId(new ProductCategoryId(UUID.fromString("0191e14b-9f0d-7812-a411-7061fa7b415f")))
                    .type(ProductType.CONSUMABLE)
                    .reference(Reference.of(Text.of("20123546")))
                    .purchasePrice(Money.of(BigDecimal.valueOf(275.35)))
                    .salePrice(Money.of(BigDecimal.valueOf(490.41)))
                    .sellable(Sellable.with(true))
                    .purchasable(Purchasable.with(true))
                    .active(Active.with(true))
                    .build();
            //Act + Then
            assertThatThrownBy(() -> productService.updateProduct(productId, productToUpdate))
                    .isInstanceOf(ProductCategoryNotFoundException.class)
                    .hasMessage(PRODUCT_CATEGORY_NOT_FOUND_EXCEPTION);
        }

        @Test
        void should_fail_when_update_product_with_duplicate_name() {
            //Given
            Product productToUpdate = Product.builder()
                    .id(productId)
                    .name(Name.of(Text.of("HP Proliant 2023")))
                    .categoryId(productCategoryId)
                    .type(ProductType.CONSUMABLE)
                    .reference(Reference.of(Text.of("20123546")))
                    .purchasePrice(Money.of(BigDecimal.valueOf(275.35)))
                    .salePrice(Money.of(BigDecimal.valueOf(490.41)))
                    .sellable(Sellable.with(true))
                    .purchasable(Purchasable.with(true))
                    .active(Active.with(true))
                    .build();
            //Act + Then
            assertThatThrownBy(() -> productService.updateProduct(productId, productToUpdate))
                    .isInstanceOf(ProductNameConflictException.class)
                    .hasMessage(PRODUCT_NAME_CONFLICT_EXCEPTION);
        }

        @Test
        void should_fail_when_update_product_with_duplicate_reference() {
            //Given
            Product productToUpdate = Product.builder()
                    .id(productId)
                    .name(Name.of(Text.of("New Mac Book Pro 2023")))
                    .categoryId(productCategoryId)
                    .type(ProductType.CONSUMABLE)
                    .reference(Reference.of(Text.of("2120354")))
                    .purchasePrice(Money.of(BigDecimal.valueOf(275.35)))
                    .salePrice(Money.of(BigDecimal.valueOf(490.41)))
                    .sellable(Sellable.with(true))
                    .purchasable(Purchasable.with(true))
                    .active(Active.with(true))
                    .build();
            //Act + Then
            assertThatThrownBy(() -> productService.updateProduct(productId, productToUpdate))
                    .isInstanceOf(ProductReferenceConflictException.class)
                    .hasMessage(PRODUCT_REFERENCE_CONFLICT_EXCEPTION);
        }

        @Test
        void should_fail_when_update_product_with_non_existing_stock_uom_id() {
            //Given
            Product productToUpdate = Product.builder()
                    .id(productId)
                    .name(Name.of(Text.of("New Mac Book Pro 2023")))
                    .categoryId(productCategoryId)
                    .type(ProductType.STOCK)
                    .reference(Reference.of(Text.of("4569875")))
                    .purchasePrice(Money.of(BigDecimal.valueOf(275.35)))
                    .salePrice(Money.of(BigDecimal.valueOf(490.41)))
                    .sellable(Sellable.with(true))
                    .purchasable(Purchasable.with(true))
                    .stockUomId(new UomId(UUID.fromString("0191e166-9f7e-7ede-b014-2ff7b97b2e47")))
                    .purchaseUomId(new UomId(UUID.fromString("0191e167-b5bb-7e93-b6be-008c07c9fda8")))
                    .active(Active.with(true))
                    .build();
            //Act + Then
            assertThatThrownBy(() -> productService.updateProduct(productId, productToUpdate))
                    .isInstanceOf(ProductUomNotFoundException.class)
                    .hasMessage(PRODUCT_UOM_NOT_FOUND_EXCEPTION);
        }
    }

    private void saveUom(Uom uom) {
        uomRepository.save(uom);
    }

    private void saveProductCategory(ProductCategory productCategory) {
        productCategoryRepository.save(productCategory);
    }

    private void saveOneProduct(Name name) {
        Product product = Product.builder()
                .name(name)
                .type(ProductType.CONSUMABLE)
                .categoryId(new ProductCategoryId(UUID.randomUUID()))
                .build();
        product.validate();
        product.initiate();
        productRepository.save(product);
    }
}
