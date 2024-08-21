package cm.xenonbyte.farmbyte.catalog.adapter.data.access.inmemory;

import cm.xenonbyte.farmbyte.catalog.adapter.data.access.test.UomCategoryRepositoryTest;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategory;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.Text;
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

        name = Name.of(Text.of("Unite"));
        UomCategory uomCategory = UomCategory.of(name);
        uomCategory.initiate();
        uomCategoryRepository.save(uomCategory);

        parentUomCategoryId = uomCategory.getId();

        UomCategory.of(Name.of(Text.of("Carton")), parentUomCategoryId);
    }
}
