package cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.uom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    boolean existsByNameIgnoreCaseAndUomCategoryJpaAndActiveIsTrue(String name, UomCategoryJpa uomCategoryJpa);

    @Query("select uom from UomJpa uom where " +
            "lower(concat(uom.name ,'', uom.uomCategoryJpa.name,'',uom.uomTypeJpa)) like lower(concat('%', :keyword, '%'))")
    Page<UomJpa> search(Pageable pageable, @Param("keyword") String keyword);

    Optional<UomJpa> findByNameIgnoreCase(String name);

    List<UomJpa> findByUomCategoryJpaAndUomTypeJpaAndActiveIsTrue(UomCategoryJpa uomCategoryJpa, UomTypeJpa uomTypeJpa);
}
