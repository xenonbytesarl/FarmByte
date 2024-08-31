package cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.uom;

import cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.DatabaseSetupExtension;
import cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.JpaRepositoryAdapterTest;
import cm.xenonbyte.farmbyte.catalog.adapter.data.access.test.UomCategoryRepositoryTest;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategoryId;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.Text;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.UUID;

/**
 * @author bamk
 * @version 1.0
 * @since 15/08/2024
 */
@DataJpaTest
@ActiveProfiles("test")
@ExtendWith(DatabaseSetupExtension.class)
@Import(JpaRepositoryAdapterTest.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {UomCategoryJpaRepository.class, UomCategoryJpaMapper.class})
class UomCategoryJpaRepositoryAdapterIT extends UomCategoryRepositoryTest {

    @Autowired
    private UomCategoryJpaRepository uomCategoryJpaRepository;

    @Autowired
    private UomCategoryJpaMapper uomCategoryJpaMapper;

    @BeforeEach
    void setUp() {

        super.uomCategoryRepository = new UomCategoryJpaRepositoryAdapter(uomCategoryJpaRepository, uomCategoryJpaMapper);

        name = Name.of(Text.of("Unite"));

        parentUomCategoryId = new UomCategoryId(UUID.fromString("01912c0f-2fcf-705b-ae59-d79d159f3ad0"));

    }
}
