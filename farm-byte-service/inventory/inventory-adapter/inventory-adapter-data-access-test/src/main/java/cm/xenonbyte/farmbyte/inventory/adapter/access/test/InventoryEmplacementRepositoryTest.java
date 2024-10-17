package cm.xenonbyte.farmbyte.inventory.adapter.access.test;

import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.Text;
import cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacement;
import cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacementId;
import cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacementRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author bamk
 * @version 1.0
 * @since 16/10/2024
 */
public abstract class InventoryEmplacementRepositoryTest {

    protected InventoryEmplacementRepository inventoryEmplacementRepository;
    protected Name name;
    protected InventoryEmplacementId parentId;
    protected InventoryEmplacement inventoryEmplacement;

    @Nested
    class CreateInventoryEmplacementRepositoryTest {
        @Test
        void should_return_false_when_find_inventory_emplacement_with_non_existing_name() {

            //Given + Act
            Boolean result = inventoryEmplacementRepository.existsByNameIgnoreCase(Name.of(Text.of("Fake Emplacement Name 1")));
            //Then
            assertThat(result).isFalse();
        }

        @Test
        void should_return_true_when_find_inventory_emplacement_with_existing_emplacement() {
            //Given + Act
            Boolean result = inventoryEmplacementRepository.existsByNameIgnoreCase(name);
            //Then
            assertThat(result).isTrue();
        }

        @Test
        void should_return_false_when_find_inventory_emplacement_with_non_parent_id() {
            //Given
            InventoryEmplacementId fakeInventoryEmplacementId = new InventoryEmplacementId(UUID.fromString("019296a1-9551-7ae3-9fce-25503bc7b5ab"));
            //Act
            Boolean result = inventoryEmplacementRepository.existsById(fakeInventoryEmplacementId);
            //Then
            assertThat(result).isFalse();
        }

        @Test
        void should_return_true_when_find_inventory_emplacement_with_existing_parent_id() {
            //Given + Act
            Boolean result = inventoryEmplacementRepository.existsById(parentId);
            //Then
            assertThat(result).isTrue();
        }

        @Test
        void should_create_inventory_emplacement() {
            //Given + Act
            InventoryEmplacement result = inventoryEmplacementRepository.save(inventoryEmplacement);

            //Then
            assertThat(result).isNotNull().isEqualTo(inventoryEmplacement);
        }
    }
}
