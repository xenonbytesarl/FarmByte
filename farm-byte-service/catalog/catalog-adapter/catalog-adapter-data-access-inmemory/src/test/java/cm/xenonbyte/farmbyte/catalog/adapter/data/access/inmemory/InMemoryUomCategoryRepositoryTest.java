package cm.xenonbyte.farmbyte.catalog.adapter.data.access.inmemory;

import cm.xenonbyte.farmbyte.catalog.adapter.data.access.test.UomCategoryRepositoryTest;
import cm.xenonbyte.farmbyte.catalog.domain.core.uomcategory.UomCategory;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import org.junit.jupiter.api.BeforeEach;

/**
 * @author bamk
 * @version 1.0
 * @since 15/08/2024
 */
public final class InMemoryUomCategoryRepositoryTest extends UomCategoryRepositoryTest {

    @BeforeEach
    void setUp() {
        uomCategoryRepository = new InMemoryUomCategoryRepository();

        name = Name.of("Unite");
        UomCategory uomCategory = new UomCategory(name);
        uomCategory.initiate();
        uomCategoryRepository.save(uomCategory);

        parentUomCategoryId = uomCategory.getId();

        new UomCategory(Name.of("Carton"), parentUomCategoryId);
    }
}
