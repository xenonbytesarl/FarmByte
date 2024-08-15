package cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.uomcategory;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @author bamk
 * @version 1.0
 * @since 15/08/2024
 */
public interface UomCategoryJpaRepository extends JpaRepository<UomCategoryJpa, UUID> {
    Boolean existsByName(String value);
}
