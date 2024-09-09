package cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.uom;

import cm.xenonbyte.farmbyte.catalog.domain.core.uom.Uom;
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
 * @since 06/08/2024
 * @since 06/08/2024
 * @since 06/08/2024
 */
@Repository
public interface UomJpaRepository extends JpaRepository<UomJpa, UUID> {
    boolean existsByUomCategoryJpaAndUomTypeJpaAndActiveIsTrue(UomCategoryJpa uomCategoryJpa, UomTypeJpa uomTypeJpa);

    boolean existsByNameAndUomCategoryJpaAndActiveIsTrue(String name, UomCategoryJpa uomCategoryJpa);

    @Query("select uj from UomJpa uj where lower(concat(uj.name ,'', uj.uomCategoryJpa.name,'',uj.uomTypeJpa)) like lower(concat('%', :keyword, '%'))")
    Page<UomJpa> search(Pageable pageable, @Param("keyword") String keyword);

    Optional<UomJpa> findByName(String name);
}
