package cm.xenonbyte.farmbyte.inventory.adapter.data.access.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author bamk
 * @version 1.0
 * @since 16/10/2024
 */
@Repository
public interface InventoryEmplacementRepositoryJpa extends JpaRepository<InventoryEmplacementJpa, UUID> {
    Boolean existsByParentJpa(InventoryEmplacementJpa parentJpa);

    Boolean existsByName(String name);
}
