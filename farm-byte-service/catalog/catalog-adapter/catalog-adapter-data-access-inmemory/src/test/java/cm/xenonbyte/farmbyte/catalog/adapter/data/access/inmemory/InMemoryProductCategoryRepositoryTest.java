package cm.xenonbyte.farmbyte.catalog.adapter.data.access.inmemory;

import cm.xenonbyte.farmbyte.catalog.adapter.data.access.test.ProductCategoryRepositoryTest;
import cm.xenonbyte.farmbyte.catalog.domain.core.productcategory.ProductCategory;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import org.junit.jupiter.api.BeforeEach;

/**
 * @author bamk
 * @version 1.0
 * @since 15/08/2024
 */
public final class InMemoryProductCategoryRepositoryTest extends ProductCategoryRepositoryTest {

    @BeforeEach
    void setUp() {
        productCategoryRepository = new InMemoryProductCategoryRepository();

        name = Name.of("Raw Material");
        ProductCategory productCategory = ProductCategory.of(name);
        productCategory.initiate();
        productCategoryRepository.save(productCategory);

        parentProductCategoryId = productCategory.getId();

        ProductCategory.of(Name.of("Manufactured"), parentProductCategoryId);
    }
}
