package cm.xenonbyte.farmbyte.catalog.domain.test;

import cm.xenonbyte.farmbyte.catalog.adapter.data.access.inmemory.InMemoryProductRepository;
import cm.xenonbyte.farmbyte.catalog.adapter.data.access.inmemory.InMemoryUomRepository;
import cm.xenonbyte.farmbyte.catalog.domain.ProductNameConflictException;
import cm.xenonbyte.farmbyte.catalog.domain.core.ProductStockAndPurchaseUomBadException;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.Product;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductCategoryId;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductService;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductType;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ports.primary.IProductService;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ports.secondary.ProductRepository;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.Uom;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategoryId;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomId;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomType;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.ports.secondary.UomRepository;
import cm.xenonbyte.farmbyte.common.domain.vo.Money;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.Text;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.stream.Stream;

import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_CATEGORY_IS_REQUIRED;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_NAME_CONFLICT_EXCEPTION;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_NAME_IS_REQUIRED;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_PURCHASE_PRICE_SHOULD_BE_GREATER_THAN_ZERO;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_PURCHASE_UOM_IS_REQUIRED_WHEN_TYPE_IS_STOCK;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_SALE_PRICE_SHOULD_BE_GREATER_THAN_ZERO;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_STOCK_AND_PURCHASE_UOM_BAD_EXCEPTION;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_STOCK_UOM_IS_REQUIRED_WHEN_TYPE_IS_STOCK;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_TYPE_IS_REQUIRED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author bamk
 * @version 1.0
 * @since 20/08/2024
 */
public final class ProductTest {

    private IProductService productService;
    private ProductRepository productRepository;
    private UomRepository uomRepository;

    @BeforeEach
    void setUp() {
        productRepository = new InMemoryProductRepository();
        uomRepository = new InMemoryUomRepository();
        productService = new ProductService(productRepository, uomRepository);
    }

    static Stream<Arguments> createProductMethodSource() {
        return Stream.of(
                Arguments.of(
                    Name.of(Text.of("Chair")),
                    new ProductCategoryId(UUID.randomUUID()),
                    ProductType.STOCK,
                    new UomId(UUID.randomUUID()),
                    new UomId(UUID.randomUUID())
                ),
                Arguments.of(
                    Name.of(Text.of("Chair")),
                    new ProductCategoryId(UUID.randomUUID()),
                    ProductType.CONSUMABLE,
                    null,
                    null
                ),
                Arguments.of(
                    Name.of(Text.of("Chair")),
                    new ProductCategoryId(UUID.randomUUID()),
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
        assertThat(createdProduct.getImage()).isEqualTo(product.getImage());
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
                        new ProductCategoryId(UUID.randomUUID()),
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
                        new ProductCategoryId(UUID.randomUUID()),
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
                        new ProductCategoryId(UUID.randomUUID()),
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
                        new ProductCategoryId(UUID.randomUUID()),
                        ProductType.STOCK,
                        new UomId(UUID.randomUUID()),
                        null,
                        null,
                        null,
                        IllegalArgumentException.class,
                        PRODUCT_PURCHASE_UOM_IS_REQUIRED_WHEN_TYPE_IS_STOCK
                ),
                Arguments.of(
                        Name.of(Text.of("Chair")),
                        new ProductCategoryId(UUID.randomUUID()),
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
                        new ProductCategoryId(UUID.randomUUID()),
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
    void should_throw_exception_when_create_product_with_uom_category_is_different() {
        //Given
        UomId stockUomId = new UomId(UUID.randomUUID());
        UomId purchaseUomId = new UomId(UUID.randomUUID());

        UomCategoryId stockUomCategoryId = new UomCategoryId(UUID.randomUUID());
        UomCategoryId purschaseUomCategoryId = new UomCategoryId(UUID.randomUUID());

        saveUom(
                Uom.builder()
                    .id(stockUomId)
                    .name(Name.of(Text.of("Unite")))
                    .uomCategoryId(stockUomCategoryId)
                    .uomType(UomType.REFERENCE)
                    .build()
        );
        saveUom(
                Uom.builder()
                        .id(purchaseUomId)
                        .name(Name.of(Text.of("Temps")))
                        .uomCategoryId(purschaseUomCategoryId)
                        .uomType(UomType.REFERENCE)
                        .build()
        );

        Product product = Product.builder()
                .name(Name.of(Text.of("Chair")))
                .type(ProductType.STOCK)
                .categoryId(new ProductCategoryId(UUID.randomUUID()))
                .stockUomId(stockUomId)
                .purchaseUomId(purchaseUomId)
                .build();

        //act
        assertThatThrownBy(() -> productService.createProduct(product))
                .isInstanceOf(ProductStockAndPurchaseUomBadException.class)
                .hasMessage(PRODUCT_STOCK_AND_PURCHASE_UOM_BAD_EXCEPTION);



    }

    private void saveUom(Uom uom) {
        uomRepository.save(uom);
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
