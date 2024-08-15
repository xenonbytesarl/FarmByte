package cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.uomcategory;

import cm.xenonbyte.farmbyte.catalog.domain.core.uomcategory.UomCategory;
import cm.xenonbyte.farmbyte.catalog.domain.core.uomcategory.UomCategoryId;
import cm.xenonbyte.farmbyte.catalog.domain.core.uomcategory.ports.secondary.UomCategoryRepository;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author bamk
 * @version 1.0
 * @since 15/08/2024
 */
@Slf4j
@Service
public final class UomCategoryAdapterPostgresRepository implements UomCategoryRepository {

    private final UomCategoryJpaRepository uomCategoryJpaRepository;
    private final UomCategoryJpaMapper uomCategoryJpaMapper;

    public UomCategoryAdapterPostgresRepository(final @Nonnull UomCategoryJpaRepository uomCategoryJpaRepository,
                                                final @Nonnull UomCategoryJpaMapper uomCategoryJpaMapper) {
        this.uomCategoryJpaRepository = Objects.requireNonNull(uomCategoryJpaRepository);
        this.uomCategoryJpaMapper = Objects.requireNonNull(uomCategoryJpaMapper);
    }


    @Override
    public Boolean existsByName(@Nonnull Name name) {
        return uomCategoryJpaRepository.existsByName(name.getValue());
    }

    @Override
    public UomCategory save(@Nonnull UomCategory uomCategory) {
        return uomCategoryJpaMapper.toUomCategory(
                uomCategoryJpaRepository.save(uomCategoryJpaMapper.toUomCategoryJpa(uomCategory)));
    }

    @Override
    public Boolean existsById(UomCategoryId parentCategoryId) {
        return uomCategoryJpaRepository.existsById(parentCategoryId.getValue());
    }
}
