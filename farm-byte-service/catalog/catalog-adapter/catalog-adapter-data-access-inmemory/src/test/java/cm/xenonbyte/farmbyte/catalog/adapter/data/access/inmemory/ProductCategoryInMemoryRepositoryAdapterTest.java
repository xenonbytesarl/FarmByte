package cm.xenonbyte.farmbyte.catalog.adapter.data.access.inmemory;

import cm.xenonbyte.farmbyte.catalog.adapter.data.access.test.ProductCategoryRepositoryTest;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductCategory;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.Text;
import org.junit.jupiter.api.BeforeEach;

/**
 * @author bamk
 * @version 1.0
 * @since 15/08/2024
 */
public final class ProductCategoryInMemoryRepositoryAdapterTest extends ProductCategoryRepositoryTest {

    @BeforeEach
    void setUp() {
        productCategoryRepository = new ProductCategoryInMemoryRepositoryAdapter();

        name = Name.of(Text.of("Raw Material"));
        ProductCategory productCategory = ProductCategory.of(name);
        productCategory.initiate();
        productCategoryRepository.save(productCategory);

        parentProductCategoryId = productCategory.getId();

        ProductCategory.of(Name.of(Text.of("Manufactured")), parentProductCategoryId);
    }
}
