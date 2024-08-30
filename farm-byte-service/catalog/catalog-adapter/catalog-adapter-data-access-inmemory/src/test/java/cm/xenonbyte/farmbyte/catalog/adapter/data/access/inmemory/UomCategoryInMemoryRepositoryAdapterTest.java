package cm.xenonbyte.farmbyte.catalog.adapter.data.access.inmemory;

import cm.xenonbyte.farmbyte.catalog.adapter.data.access.test.UomCategoryRepositoryTest;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategory;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategoryId;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.Text;
import org.junit.jupiter.api.BeforeEach;

import java.util.UUID;

/**
 * @author bamk
 * @version 1.0
 * @since 15/08/2024
 */
public final class UomCategoryInMemoryRepositoryAdapterTest extends UomCategoryRepositoryTest {

   @BeforeEach
   void setUp() {
       uomCategoryRepository = new UomCategoryInMemoryRepositoryAdapter();

       name = Name.of(Text.of("Unite"));
       UomCategory uomCategory = UomCategory.of(name);
       uomCategory.initiate();
       uomCategoryRepository.save(uomCategory);
       parentUomCategoryId = uomCategory.getId();

       UomCategory.of(Name.of(Text.of("Carton")), parentUomCategoryId);

       uomCategoryRepository.save(
               UomCategory.builder()
                       .id(new UomCategoryId(UUID.fromString("0191a55b-a411-784c-bcd1-c3fc4b9213e5")))
                       .name(Name.of(Text.of("Distance")))
                       .build()
       );
       uomCategoryRepository.save(
               UomCategory.builder()
                       .id(new UomCategoryId(UUID.fromString("0191a55b-c234-75e5-9132-357b7347bfaf")))
                       .name(Name.of(Text.of("Volume")))
                       .build()
       );
       uomCategoryRepository.save(
               UomCategory.builder()
                       .id(new UomCategoryId(UUID.fromString("0191a55b-d972-7f67-a2dd-e88fc0afc484")))
                       .name(Name.of(Text.of("Temps")))
                       .build()
       );
       uomCategoryRepository.save(
               UomCategory.builder()
                       .id(new UomCategoryId(UUID.fromString("0191a55b-f51f-7e83-bc63-55f933ff00ca")))
                       .name(Name.of(Text.of("Poids")))
                       .build()
       );
   }

}
