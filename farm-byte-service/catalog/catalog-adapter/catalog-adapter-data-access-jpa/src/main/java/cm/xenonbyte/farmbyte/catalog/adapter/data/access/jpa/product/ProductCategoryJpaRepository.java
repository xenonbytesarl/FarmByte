package cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author bamk
 * @version 1.0
 * @since 15/08/2024
 */
@Repository
public interface ProductCategoryJpaRepository extends JpaRepository<ProductCategoryJpa, UUID> {
    Boolean existsByName(String value);
    @Query("select pcj from ProductCategoryJpa pcj left join pcj.parentProductCategoryJpa ppcj where lower(concat(pcj.name, '', coalesce(ppcj.name, ''))) like lower(concat('%',:keyword,'%'))")
    Page<ProductCategoryJpa> search(Pageable pageable, @Param("keyword") String keyword);
}
