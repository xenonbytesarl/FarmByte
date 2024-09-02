package cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.uom;

import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategory;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategoryId;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.ports.secondary.UomCategoryRepository;
import cm.xenonbyte.farmbyte.common.domain.vo.Direction;
import cm.xenonbyte.farmbyte.common.domain.vo.Keyword;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.PageInfo;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
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

    @Override
    public Optional<UomCategory> findById(UomCategoryId uomCategoryId) {
        return uomCategoryJpaRepository.findById(uomCategoryId.getValue())
                .map(uomCategoryJpaMapper::toUomCategory);
    }

    @Override
    public PageInfo<UomCategory> findAll(@Nonnull Integer page, @Nonnull Integer size, @Nonnull String sortAttribute, @Nonnull Direction direction) {
        Sort.Direction sortDirection = Direction.ASC.equals(direction) ? Sort.Direction.ASC : Sort.Direction.DESC;

        Page<UomCategoryJpa> uomCategoryJpaPage = uomCategoryJpaRepository.findAll(PageRequest.of(page, size, sortDirection, sortAttribute));


        return new PageInfo(
                uomCategoryJpaPage.isFirst(),
                uomCategoryJpaPage.isLast(),
                uomCategoryJpaPage.getSize(),
                uomCategoryJpaPage.getTotalElements(),
                uomCategoryJpaPage.getTotalPages(),
                uomCategoryJpaPage.getContent().stream().map(uomCategoryJpaMapper::toUomCategory).toList()
        );
    }

    @Override
    public PageInfo<UomCategory> search(int page, int size, String sortAttribute, Direction direction, Keyword keyword) {
        Sort.Direction sortDirection = Direction.ASC.equals(direction) ? Sort.Direction.ASC : Sort.Direction.DESC;

        Page<UomCategoryJpa> uomCategoryJpaPage = uomCategoryJpaRepository.findByKeyword(PageRequest.of(page, size, sortDirection, sortAttribute), keyword.getText().getValue());


        return new PageInfo(
                uomCategoryJpaPage.isFirst(),
                uomCategoryJpaPage.isLast(),
                size,
                uomCategoryJpaPage.getTotalElements(),
                uomCategoryJpaPage.getTotalPages(),
                uomCategoryJpaPage.getContent().stream().map(uomCategoryJpaMapper::toUomCategory).toList()
        );
    }
}
