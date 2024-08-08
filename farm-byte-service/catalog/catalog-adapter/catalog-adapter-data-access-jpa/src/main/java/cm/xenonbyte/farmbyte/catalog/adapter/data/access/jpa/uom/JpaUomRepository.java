package cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.uom;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author bamk
 * @version 1.0
 * @since 06/08/2024
 * @since 06/08/2024
 * @since 06/08/2024
 */
@Repository
public interface JpaUomRepository extends CrudRepository<UomJpa, String> {
    boolean existsByUomCategoryJpaAndTypeAndActive(UomCategoryJpa uomCategoryJpa, UomTypeJpa uomTypeJpa, Boolean active);

    boolean existsByNameAndUomCategoryJpaAndActive(String name, UomCategoryJpa uomCategoryJpa, Boolean active);
}
