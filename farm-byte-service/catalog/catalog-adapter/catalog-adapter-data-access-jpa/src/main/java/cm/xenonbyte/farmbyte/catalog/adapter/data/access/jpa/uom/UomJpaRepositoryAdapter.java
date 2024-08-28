package cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.uom;

import cm.xenonbyte.farmbyte.catalog.domain.core.uom.Uom;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomId;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomType;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.ports.secondary.UomRepository;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategoryId;
import groovy.util.logging.Slf4j;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

/**
 * @author bamk
 * @version 1.0
 * @since 06/08/2024
 */
@Slf4j
@Service
public class UomJpaRepositoryAdapter implements UomRepository {

    private final UomJpaRepository uomJpaRepository;
    private final UomJpaMapper mapper;

    public UomJpaRepositoryAdapter(final @Nonnull UomJpaRepository uomJpaRepository, final @Nonnull UomJpaMapper mapper) {
        this.uomJpaRepository = Objects.requireNonNull(uomJpaRepository);
        this.mapper = Objects.requireNonNull(mapper);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByCategoryIdAndUomTypeAndActive(@Nonnull UomCategoryId uomCategoryId, @Nonnull UomType uomType) {
        return uomJpaRepository.existsByUomCategoryJpaAndUomTypeJpaAndActiveIsTrue(
                UomCategoryJpa.builder().id(uomCategoryId.getValue()).build(),
                UomTypeJpa.valueOf(uomType.name())
        );
    }

    @Override
    @Transactional
    public Uom save(@Nonnull Uom uom) {
        return mapper.fromUomJpa(uomJpaRepository.save(mapper.fromUom(uom)));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByNameAndCategoryAndActive(@Nonnull Name name, @Nonnull UomCategoryId uomCategoryId) {
        return uomJpaRepository.existsByNameAndUomCategoryJpaAndActiveIsTrue(
                name.getText().getValue(),
                UomCategoryJpa.builder().id(uomCategoryId.getValue()).build()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Uom> findByUomId(@Nonnull UomId uomId) {
        return uomJpaRepository.findById(uomId.getValue()).map(mapper::fromUomJpa);
    }

    @Override
    public boolean existsById(@Nonnull UomId uomId) {
        return uomJpaRepository.existsById(uomId.getValue());
    }
}
