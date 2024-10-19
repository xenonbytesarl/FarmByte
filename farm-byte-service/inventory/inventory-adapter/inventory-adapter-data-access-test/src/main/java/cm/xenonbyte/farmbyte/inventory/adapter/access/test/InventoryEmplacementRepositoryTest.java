package cm.xenonbyte.farmbyte.inventory.adapter.access.test;

import cm.xenonbyte.farmbyte.common.domain.vo.Direction;
import cm.xenonbyte.farmbyte.common.domain.vo.Keyword;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.PageInfo;
import cm.xenonbyte.farmbyte.common.domain.vo.Text;
import cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacement;
import cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacementId;
import cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacementRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;
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
    protected InventoryEmplacementId inventoryEmplacementId;

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

    @Nested
    class FindInventoryEmplacementByIdRepositoryTest {

        @Test
        void should_success_when_find_inventory_emplacement_by_id() {
            //Given + Act
            Optional<InventoryEmplacement> result = inventoryEmplacementRepository.findById(inventoryEmplacementId);

            //Then
            assertThat(result.isPresent()).isTrue();
        }

        @Test
        void should_fail_when_find_inventory_emplacement_by_id() {
            //Given + Act
            Optional<InventoryEmplacement> result =
                    inventoryEmplacementRepository.findById(new InventoryEmplacementId(UUID.fromString("0192a66b-2ca4-771f-8b70-d57d4b66f797")));

            //Then
            assertThat(result.isPresent()).isFalse();
        }
    }

    @Nested
    class FindInventoryEmplacementsRepositoryTest {
        @Test
        void should_success_when_find_inventory_emplacements() {
            //Given
            Integer page = 0;
            Integer size = 2;
            String sortAttribute = "name";
            Direction direction = Direction.ASC;

            //Act
            PageInfo<InventoryEmplacement> result =
                    inventoryEmplacementRepository.findAll(page, size, sortAttribute, direction);

            //Then
            assertThat(result.getTotalElements()).isGreaterThan(0);
            assertThat(result.getTotalPages()).isGreaterThan(0);
            assertThat(result.getElements().size()).isGreaterThan(0);
            assertThat(result.getTotalPages()).isGreaterThan(0);
        }
    }

    @Nested
    class SearchInventoryEmplacementsRepositoryTest {
        @Test
        void should_success_when_find_inventory_emplacements() {
            //Given
            Integer page = 0;
            Integer size = 2;
            String sortAttribute = "name";
            Direction direction = Direction.ASC;
            Keyword keyword = Keyword.of(Text.of("int"));

            //Act
            PageInfo<InventoryEmplacement> result =
                    inventoryEmplacementRepository.search(page, size, sortAttribute, direction, keyword);

            //Then
            assertThat(result.getTotalElements()).isGreaterThan(0);
            assertThat(result.getTotalPages()).isGreaterThan(0);
            assertThat(result.getElements().size()).isGreaterThan(0);
            assertThat(result.getTotalPages()).isGreaterThan(0);
        }

        @Test
        void should_empty_when_find_inventory_emplacements() {
            //Given
            Integer page = 0;
            Integer size = 2;
            String sortAttribute = "name";
            Direction direction = Direction.ASC;
            Keyword keyword = Keyword.of(Text.of("xxxx"));

            //Act
            PageInfo<InventoryEmplacement> result =
                    inventoryEmplacementRepository.search(page, size, sortAttribute, direction, keyword);

            //Then
            assertThat(result.getTotalElements()).isEqualTo(0);
            assertThat(result.getTotalPages()).isEqualTo(0);
            assertThat(result.getElements().size()).isEqualTo(0);
            assertThat(result.getTotalPages()).isEqualTo(0);
        }
    }
}
