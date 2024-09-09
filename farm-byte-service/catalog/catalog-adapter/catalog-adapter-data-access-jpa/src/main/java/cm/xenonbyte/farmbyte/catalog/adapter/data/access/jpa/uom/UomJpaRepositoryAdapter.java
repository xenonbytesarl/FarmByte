package cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.uom;

import cm.xenonbyte.farmbyte.catalog.domain.core.uom.Uom;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomId;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomType;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.ports.secondary.UomRepository;
import cm.xenonbyte.farmbyte.common.domain.vo.Direction;
import cm.xenonbyte.farmbyte.common.domain.vo.Keyword;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategoryId;
import cm.xenonbyte.farmbyte.common.domain.vo.PageInfo;
import groovy.util.logging.Slf4j;
import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    private final UomJpaMapper uomJpaMapper;

    public UomJpaRepositoryAdapter(final @Nonnull UomJpaRepository uomJpaRepository, final @Nonnull UomJpaMapper uomJpaMapper) {
        this.uomJpaRepository = Objects.requireNonNull(uomJpaRepository);
        this.uomJpaMapper = Objects.requireNonNull(uomJpaMapper);
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
        return uomJpaMapper.fromUomJpa(uomJpaRepository.save(uomJpaMapper.fromUom(uom)));
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
    public Optional<Uom> findById(@Nonnull UomId uomId) {
        return uomJpaRepository.findById(uomId.getValue()).map(uomJpaMapper::fromUomJpa);
    }

    @Override
    public boolean existsById(@Nonnull UomId uomId) {
        return uomJpaRepository.existsById(uomId.getValue());
    }

    @Override
    public PageInfo<Uom> findAll(int page, int size, String attribute, Direction direction) {
        Sort.Direction sortDirection = direction == Direction.ASC ? Sort.Direction.ASC : Sort.Direction.DESC;
        Page<UomJpa> uomJpaPage = uomJpaRepository.findAll(PageRequest.of(page, size, sortDirection, attribute));
        return new PageInfo<>(
                uomJpaPage.isFirst(),
                uomJpaPage.isLast(),
                uomJpaPage.getSize(),
                uomJpaPage.getTotalElements(),
                uomJpaPage.getTotalPages(),
                uomJpaPage.getContent().stream().map(uomJpaMapper::fromUomJpa).toList()
        );
    }

    @Override
    public PageInfo<Uom> search(int page, int size, String attribute, Direction direction, Keyword keyword) {
        Sort.Direction sortDirection = direction == Direction.ASC ? Sort.Direction.ASC : Sort.Direction.DESC;
        Page<UomJpa> uomJpaPage = uomJpaRepository.search(PageRequest.of(page, size, sortDirection, attribute), keyword.getText().getValue());
        return new PageInfo<>(
                uomJpaPage.isFirst(),
                uomJpaPage.isLast(),
                uomJpaPage.getSize(),
                uomJpaPage.getTotalElements(),
                uomJpaPage.getTotalPages(),
                uomJpaPage.getContent().stream().map(uomJpaMapper::fromUomJpa).toList()
        );
    }

    @Nonnull
    @Override
    public Uom update(@Nonnull Uom oldUom, @Nonnull Uom uomToUpdated) {
        return null;
    }

    @Override
    public Optional<Uom> findByName(@Nonnull Name name) {
        return Optional.empty();
    }
}
