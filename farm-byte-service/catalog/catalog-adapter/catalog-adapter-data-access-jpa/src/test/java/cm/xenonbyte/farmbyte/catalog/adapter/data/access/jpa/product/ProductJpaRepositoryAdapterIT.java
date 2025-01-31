package cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.product;

import cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.DatabaseSetupExtension;
import cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.JpaRepositoryAdapterTest;
import cm.xenonbyte.farmbyte.catalog.adapter.data.access.test.ProductRepositoryTest;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.Product;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductCategoryId;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductId;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductType;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.Purchasable;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.Sellable;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomId;
import cm.xenonbyte.farmbyte.common.domain.vo.Active;
import cm.xenonbyte.farmbyte.common.domain.vo.Filename;
import cm.xenonbyte.farmbyte.common.domain.vo.Money;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.Reference;
import cm.xenonbyte.farmbyte.common.domain.vo.Text;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author bamk
 * @version 1.0
 * @since 23/08/2024
 */
@DataJpaTest
@ActiveProfiles("test")
@ExtendWith(DatabaseSetupExtension.class)
@Import(JpaRepositoryAdapterTest.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {ProductJpaRepository.class, ProductJpaMapper.class})
class ProductJpaRepositoryAdapterIT extends ProductRepositoryTest {

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Autowired
    private ProductJpaMapper productJpaMapper;

    @BeforeEach
    void setUp() {
        productRepository = new ProductJpaRepositoryAdapter(productJpaRepository, productJpaMapper);
        categoryId = new ProductCategoryId(UUID.fromString("01912c0f-2fcf-705b-ae59-d79d159f3ad0"));
        imageName = Filename.of(Text.of("product.png"));
        name = Name.of(Text.of("Product.2"));
        productId = new ProductId(UUID.fromString("0191bda4-65e4-73a8-8291-b2870753ad00"));
        oldProductId = new ProductId(UUID.fromString("0191bda4-65e4-73a8-8291-b2870753ad00"));
        reference = Reference.of(Text.of("63321457"));
        oldProduct =  Product.builder()
                        .id(oldProductId)
                        .name(Name.of(Text.of("New Mac Book Pro M2 Max")))
                        .categoryId(categoryId)
                        .type(ProductType.STOCK)
                        .reference(Reference.of(Text.of("3254521")))
                        .purchasePrice(Money.of(BigDecimal.valueOf(36000)))
                        .salePrice(Money.of(BigDecimal.valueOf(45000)))
                        .sellable(Sellable.with(true))
                        .purchasable(Purchasable.with(true))
                        .imageName(Filename.of(Text.of("products/product.png")))
                        .stockUomId(new UomId(UUID.fromString("01912c2c-b81a-7245-bab1-aee9b97b2afb")))
                        .purchaseUomId(new UomId(UUID.fromString("01912c2e-b52d-7b85-9c12-85af49fc7798")))
                        .active(Active.with(true))
                        .build();
    }
}
