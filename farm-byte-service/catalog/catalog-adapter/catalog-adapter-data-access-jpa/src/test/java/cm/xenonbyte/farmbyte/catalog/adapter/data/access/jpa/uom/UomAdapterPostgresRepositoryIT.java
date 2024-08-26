package cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.uom;

import cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.AdapterPostgresRepositoryConfigTest;
import cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.DatabaseSetupExtension;
import cm.xenonbyte.farmbyte.catalog.adapter.data.access.test.UomRepositoryTest;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategoryId;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomId;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomType;
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
 * @since 07/08/2024
 */
@DataJpaTest
@ActiveProfiles("test")
@ExtendWith(DatabaseSetupExtension.class)
@Import(AdapterPostgresRepositoryConfigTest.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {UomJpaRepository.class, UomJpaMapper.class})
class UomAdapterPostgresRepositoryIT extends UomRepositoryTest {

    @Autowired
    private UomJpaRepository uomJpaRepository;

    @Autowired
    private UomJpaMapper uomJpaMapper;

    @BeforeEach
    void setUp() {

        uomCategoryId = new UomCategoryId(UUID.fromString("01912c0f-2fcf-705b-ae59-d79d159f3ad0"));
        uomType =  UomType.REFERENCE;
        name = Name.of(Text.of("Unite"));
        uomId = new UomId(UUID.fromString("01912c2c-b81a-7245-bab1-aee9b97b2afb"));

        super.uomRepository = new UomAdapterPostgresRepository(uomJpaRepository, uomJpaMapper);
    }


}
