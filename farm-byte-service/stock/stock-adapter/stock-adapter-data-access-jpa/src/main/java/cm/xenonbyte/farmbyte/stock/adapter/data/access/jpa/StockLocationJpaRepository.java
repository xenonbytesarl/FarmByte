package cm.xenonbyte.farmbyte.stock.adapter.data.access.jpa;

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
 * @since 16/10/2024
 */
@Repository
public interface StockLocationJpaRepository extends JpaRepository<StockLocationJpa, UUID> {
    Boolean existsByParentJpa(StockLocationJpa parentJpa);

    Boolean existsByNameIgnoreCase(String name);

    @Query("select ie from StockLocationJpa ie left join ie.parentJpa iep where lower(concat(ie.name, '', ie.type, '', coalesce(iep.name, ''))) like lower(concat('%', :keyword, '%'))")
    Page<StockLocationJpa> search(Pageable pageable, @Param("keyword") String keyword);

    Optional<StockLocationJpa> findByName(String name);
}
