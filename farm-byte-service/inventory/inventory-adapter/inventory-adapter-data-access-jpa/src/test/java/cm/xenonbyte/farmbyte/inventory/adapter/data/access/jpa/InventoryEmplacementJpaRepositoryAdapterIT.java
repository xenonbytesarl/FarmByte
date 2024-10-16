package cm.xenonbyte.farmbyte.inventory.adapter.data.access.jpa;

import cm.xenonbyte.farmbyte.common.domain.vo.Active;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.Text;
import cm.xenonbyte.farmbyte.inventory.adapter.access.test.InventoryEmplacementRepositoryTest;
import cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacement;
import cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacementId;
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
 * @since 16/10/2024
 */
@DataJpaTest
@ActiveProfiles("test")
@ExtendWith(DatabaseSetupExtension.class)
@Import(JpaRepositoryAdapterTest.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {InventoryEmplacementRepositoryJpa.class, InventoryEmplacementJpaMapper.class})
public class InventoryEmplacementJpaRepositoryAdapterIT extends InventoryEmplacementRepositoryTest {

    @Autowired
    private InventoryEmplacementJpaMapper inventoryEmplacementJpaMapper;

    @Autowired
    private InventoryEmplacementRepositoryJpa inventoryEmplacementRepositoryJpa;

    @BeforeEach
    void setUp() {
        inventoryEmplacementRepository = new InventoryEmplacementJpaRepositoryAdapter(
                inventoryEmplacementRepositoryJpa, inventoryEmplacementJpaMapper);

        name = Name.of(Text.of("Internal Emplacement 1"));

        parentId = new InventoryEmplacementId(UUID.fromString("019296f7-1e4b-7739-8f09-cf650590f0d7"));

        inventoryEmplacement = InventoryEmplacement.builder()
                .id(new InventoryEmplacementId(UUID.fromString("01929708-609d-7d64-ae29-1940b7e5f6f1")))
                .name(Name.of(Text.of("Internal Emplacement 2")))
                .parentId(parentId)
                .active(Active.with(true))
                .build();
    }
}
