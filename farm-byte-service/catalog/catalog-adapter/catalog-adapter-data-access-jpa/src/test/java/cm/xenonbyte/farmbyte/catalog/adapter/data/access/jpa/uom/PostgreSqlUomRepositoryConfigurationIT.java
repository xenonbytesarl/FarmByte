package cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.uom;

import cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.PostgreSqlRepositoryConfigurationIT;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.vo.Name;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.vo.UomCategoryId;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.vo.UomType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.UUID;

/**
 * @author bamk
 * @version 1.0
 * @since 07/08/2024
 */
@DataJpaTest
@EnableAutoConfiguration
@ContextConfiguration(classes = {JpaUomRepository.class, UomMapper.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PostgreSqlUomRepositoryConfigurationIT extends PostgreSqlRepositoryConfigurationIT {

    @Autowired
    private JpaUomRepository jpaUomRepository;

    @Autowired
    private UomMapper uomMapper;

    @BeforeEach
    void setUp() {

        uomCategoryId = UomCategoryId.of(UUID.fromString("01912c0f-2fcf-705b-ae59-d79d159f3ad0"));
        uomType =  UomType.REFERENCE;
        name = Name.of("Unite");

        super.uomRepository = new PostgreSqlUomRepository(jpaUomRepository, uomMapper);

    }


}
