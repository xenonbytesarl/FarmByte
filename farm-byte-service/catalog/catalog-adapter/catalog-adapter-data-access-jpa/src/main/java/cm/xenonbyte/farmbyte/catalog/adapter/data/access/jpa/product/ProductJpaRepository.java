package cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author bamk
 * @version 1.0
 * @since 23/08/2024
 */
@Repository
public interface ProductJpaRepository extends JpaRepository<ProductJpa, UUID> {
    Boolean existsByName(String value);
}
