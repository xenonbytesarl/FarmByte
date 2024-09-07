package cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.uom;

import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * @author bamk
 * @version 1.0
 * @since 15/08/2024
 */
@Repository
public interface UomCategoryJpaRepository extends JpaRepository<UomCategoryJpa, UUID> {
    Boolean existsByName(String value);

    @Query("select ucj from UomCategoryJpa ucj left join ucj.parentUomCategoryJpa pucj where lower(concat(ucj.name, '', coalesce(pucj.name, ''))) like lower(concat('%',:keyword,'%'))")
    Page<UomCategoryJpa> search(Pageable pageable, @Param("keyword") String keyword);

    Optional<UomCategoryJpa> findByName(String name);
}
