package cm.xenonbyte.farmbyte.catalog.adapter.data.access.inmemory;

import cm.xenonbyte.farmbyte.catalog.adapter.data.access.test.ProductRepositoryTest;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductId;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.Purchasable;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.Sellable;
import cm.xenonbyte.farmbyte.common.domain.vo.Active;
import cm.xenonbyte.farmbyte.common.domain.vo.Filename;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.Product;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductCategoryId;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductType;
import cm.xenonbyte.farmbyte.common.domain.vo.Money;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.Reference;
import cm.xenonbyte.farmbyte.common.domain.vo.Text;
import org.junit.jupiter.api.BeforeEach;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author bamk
 * @version 1.0
 * @since 23/08/2024
 */
public final class ProductInMemoryRepositoryAdapterTest extends ProductRepositoryTest {

    @BeforeEach
    void setUp() {
        productRepository = new ProductInMemoryRepositoryAdapter();
        name = Name.of(Text.of("Product.2"));
        categoryId = new ProductCategoryId(UUID.randomUUID());
        imageName = Filename.of(Text.of("product.png"));

        productId = new ProductId(UUID.fromString("0191bd85-b58a-7411-95e5-9d99b7747726"));

        productRepository.save(Product.builder()
                .id(productId)
                .name(name)
                .type(ProductType.SERVICE)
                .categoryId(new ProductCategoryId(UUID.randomUUID()))
                .build());

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
}
