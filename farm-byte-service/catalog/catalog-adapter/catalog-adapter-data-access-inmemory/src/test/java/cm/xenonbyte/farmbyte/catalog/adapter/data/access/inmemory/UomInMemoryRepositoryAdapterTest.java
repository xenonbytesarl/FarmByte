package cm.xenonbyte.farmbyte.catalog.adapter.data.access.inmemory;

import cm.xenonbyte.farmbyte.catalog.adapter.data.access.test.UomRepositoryTest;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.Uom;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategoryId;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomType;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.Ratio;
import cm.xenonbyte.farmbyte.common.domain.vo.Text;
import org.junit.jupiter.api.BeforeEach;

import java.util.UUID;

/**
 * @author bamk
 * @version 1.0
 * @since 06/08/2024
 */
public final class UomInMemoryRepositoryAdapterTest extends UomRepositoryTest {



    @BeforeEach
    void setUp() {
        uomCategoryId = new UomCategoryId(UUID.randomUUID());
        uomType =  UomType.REFERENCE;
        name = Name.of(Text.of("Unite"));

        uomRepository = new UomInMemoryRepositoryAdapter();

        //We save some uom in storage for some test case
        Uom uom1 = createSomeUom(
                name,
                uomCategoryId,
                uomType,
                null);
        Uom uom2 = createSomeUom(Name.of(Text.of("Carton de 5")),
                uomCategoryId,
                uomType,
                Ratio.of(5.0));
        uomRepository.save(uom1);
        uomRepository.save(uom2);

        uomId = uom1.getId();
    }

}
