package cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.product;

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
 * @since 23/08/2024
 */
@Repository
public interface ProductJpaRepository extends JpaRepository<ProductJpa, UUID> {
    Boolean existsByNameIgnoreCase(String value);

    @Query("select pj from ProductJpa pj where lower(concat(pj.name, '', coalesce(pj.reference, ''), '', pj.typeJpa, '', pj.categoryJpa.name , '', coalesce(pj.stockUomJpa.name, ''), '', coalesce(pj.purchaseUomJpa.name, ''))) like lower(concat('%', :keyword, '%'))")
    Page<ProductJpa> search(Pageable pageable, @Param("keyword") String keyword);

    Boolean existsByReferenceIgnoreCase(String reference);

    Optional<ProductJpa> findByNameIgnoreCase(String name);

    Optional<ProductJpa> findByReferenceIgnoreCase(String reference);
}
