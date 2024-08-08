package cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.uom;

import cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.uomcategory.UomCategoryJpa;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.Uom;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomType;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.ports.secondary.UomRepository;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.Name;
import cm.xenonbyte.farmbyte.catalog.domain.core.uomcategory.UomCategoryId;
import groovy.util.logging.Slf4j;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * @author bamk
 * @version 1.0
 * @since 06/08/2024
 */
@Slf4j
@Service
public class UomPostgreSqlRepository implements UomRepository {

    private final UomJpaRepository uomJpaRepository;
    private final UomJpaMapper mapper;

    public UomPostgreSqlRepository(final @Nonnull UomJpaRepository uomJpaRepository, final @Nonnull UomJpaMapper mapper) {
        this.uomJpaRepository = Objects.requireNonNull(uomJpaRepository);
        this.mapper = Objects.requireNonNull(mapper);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByCategoryIdAndUomTypeAndActive(UomCategoryId uomCategoryId, UomType uomType) {
        return uomJpaRepository.existsByUomCategoryJpaAndUomTypeJpaAndActive(
                UomCategoryJpa.builder().id(uomCategoryId.getIdentifier()).build(),
                UomTypeJpa.valueOf(uomType.name()),
                true
        );
    }

    @Override
    @Transactional
    public Uom save(Uom uom) {
        return mapper.fromUomJpa(uomJpaRepository.save(mapper.fromUom(uom)));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByNameAndCategoryAndActive(Name name, UomCategoryId uomCategoryId) {
        return uomJpaRepository.existsByNameAndUomCategoryJpaAndActive(
                name.getValue(),
                UomCategoryJpa.builder().id(uomCategoryId.getIdentifier()).build(),
                true
        );
    }
}
