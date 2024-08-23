package cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.uom;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author bamk
 * @version 1.0
 * @since 15/08/2024
 */
@Repository
public interface UomCategoryJpaRepository extends JpaRepository<UomCategoryJpa, UUID> {
    Boolean existsByName(String value);
}
