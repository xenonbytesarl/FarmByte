package cm.xenonbyte.farmbyte.admin.adapter.data.access.jpa;

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
 * @since 29/10/2024
 */
@Repository
public interface SequenceJpaRepository extends JpaRepository<SequenceJpa, UUID> {

    Boolean existsByNameIgnoreCase(String name);

    Boolean existsByCode(String code);

    Optional<SequenceJpa> findByCode(String code);

    @Query("select s from SequenceJpa s where lower(concat(s.name, '', s.code, '', coalesce(s.prefix, ''), coalesce(s.suffix, ''))) like lower(concat('%', :keyword, '%'))")
    Page<SequenceJpa> search(Pageable pageable, @Param("keyword") String keyword);
}
