package cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.product;

import cm.xenonbyte.farmbyte.catalog.domain.core.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    @Query("select pj from ProductJpa pj left join pj.categoryJpa pjcj left join pj.stockUomJpa pjsuj left join pj.purchaseUomJpa pjpuj where lower(concat(pj.name, '', coalesce(pj.reference, ''), '', pj.typeJpa, '', pjcj.name, '', coalesce(pjsuj.name, ''), coalesce(pjpuj.name, ''))) like lower(concat('%', :keyword, '%'))")
    Page<ProductJpa> search(Pageable pageable, @Param("keyword") String keyword);

    Boolean existsByReferenceIgnoreCase(String reference);

    Optional<ProductJpa> findByNameIgnoreCase(String name);

    Optional<ProductJpa> findByReferenceIgnoreCase(String reference);
}
