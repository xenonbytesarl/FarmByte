package cm.xenonbyte.farmbyte.inventory.data.access.inmemory;

import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.Text;
import cm.xenonbyte.farmbyte.inventory.adapter.access.test.InventoryEmplacementRepositoryTest;
import cm.xenonbyte.farmbyte.inventory.adapter.data.access.inmemory.InMemoryInventoryEmplacementRepository;
import cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacement;
import cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacementId;
import org.junit.jupiter.api.BeforeEach;

import java.util.UUID;

/**
 * @author bamk
 * @version 1.0
 * @since 16/10/2024
 */
public final class InventoryEmplacementInMemoryRepositoryAdapterTest extends InventoryEmplacementRepositoryTest {

    @BeforeEach
    void setUp() {
        inventoryEmplacementRepository = new InMemoryInventoryEmplacementRepository();

        name = Name.of(Text.of("Root View Emplacement"));

        parentId = new InventoryEmplacementId(UUID.fromString("019296b0-857b-71bd-ab43-1f855b4ccef5"));

        inventoryEmplacement = inventoryEmplacementRepository.save(
                InventoryEmplacement.builder()
                        .id(parentId)
                        .name(name)
                        .build());

        inventoryEmplacementRepository.save(
                InventoryEmplacement.builder()
                    .id(new InventoryEmplacementId(UUID.fromString("019296b2-1138-7fcd-b732-aa676eb469fd")))
                    .name(Name.of(Text.of("Child internal Emplacement")))
                    .parentId(parentId)
                    .build());


    }
}
