package cm.xenonbyte.farmbyte.catalog.adapter.data.access.inmemory;

import cm.xenonbyte.farmbyte.catalog.adapter.data.access.test.ProductRepositoryTest;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.Product;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductCategoryId;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductType;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.Text;
import org.junit.jupiter.api.BeforeEach;

import java.util.UUID;

/**
 * @author bamk
 * @version 1.0
 * @since 23/08/2024
 */
public final class InMemoryProductRepositoryTest extends ProductRepositoryTest {

    @BeforeEach
    void setUp() {
        productRepository = new InMemoryProductRepository();
        name = Name.of(Text.of("Product.2"));

        productRepository.save(Product.builder()
                .name(name)
                .type(ProductType.SERVICE)
                .categoryId(new ProductCategoryId(UUID.randomUUID()))
                .build());
    }
}
