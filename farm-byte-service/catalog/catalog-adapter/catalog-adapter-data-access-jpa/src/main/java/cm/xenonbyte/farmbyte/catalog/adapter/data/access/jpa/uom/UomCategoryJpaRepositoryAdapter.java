package cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.uom;

import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategory;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategoryId;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.ports.secondary.UomCategoryRepository;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * @author bamk
 * @version 1.0
 * @since 15/08/2024
 */
@Slf4j
@Service
public class UomCategoryJpaRepositoryAdapter implements UomCategoryRepository {

    private final UomCategoryJpaRepository uomCategoryJpaRepository;
    private final UomCategoryJpaMapper uomCategoryJpaMapper;

    public UomCategoryJpaRepositoryAdapter(final @Nonnull UomCategoryJpaRepository uomCategoryJpaRepository,
                                           final @Nonnull UomCategoryJpaMapper uomCategoryJpaMapper) {
        this.uomCategoryJpaRepository = Objects.requireNonNull(uomCategoryJpaRepository);
        this.uomCategoryJpaMapper = Objects.requireNonNull(uomCategoryJpaMapper);
    }


    @Override
    @Transactional(readOnly = true)
    public Boolean existsByName(@Nonnull Name name) {
        return uomCategoryJpaRepository.existsByName(name.getText().getValue());
    }

    @Override
    @Transactional
    public UomCategory save(@Nonnull UomCategory uomCategory) {
        return uomCategoryJpaMapper.toUomCategory(
                uomCategoryJpaRepository.save(uomCategoryJpaMapper.toUomCategoryJpa(uomCategory)));
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean existsById(UomCategoryId parentCategoryId) {
        return uomCategoryJpaRepository.existsById(parentCategoryId.getValue());
    }
}
