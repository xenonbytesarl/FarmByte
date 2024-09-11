package cm.xenonbyte.farmbyte.catalog.domain.core.uom;

import cm.xenonbyte.farmbyte.catalog.domain.core.uom.ports.primary.UomCategoryService;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.ports.secondary.UomCategoryRepository;
import cm.xenonbyte.farmbyte.common.domain.annotation.DomainService;
import cm.xenonbyte.farmbyte.common.domain.vo.Keyword;
import cm.xenonbyte.farmbyte.common.domain.vo.PageInfo;
import cm.xenonbyte.farmbyte.common.domain.vo.Direction;
import jakarta.annotation.Nonnull;

import java.util.Objects;
import java.util.Optional;

/**
 * @author bamk
 * @version 1.0
 * @since 14/08/2024
 */
@DomainService
public final class UomCategoryDomainService implements UomCategoryService {
    private final UomCategoryRepository uomCategoryRepository;

    public UomCategoryDomainService(final @Nonnull UomCategoryRepository uomCategoryRepository) {
        this.uomCategoryRepository = Objects.requireNonNull(uomCategoryRepository);
    }

    @Nonnull
    @Override
    public UomCategory createUomCategory(@Nonnull UomCategory uomCategory) {
        validateUomCategory(uomCategory);
        uomCategory.initiate();
        return uomCategoryRepository.save(uomCategory);
    }

    @Override
    public UomCategory findUomCategoryById(UomCategoryId uomCategoryId) {
        Optional<UomCategory> optionalUomCategory = uomCategoryRepository.findById(uomCategoryId);
        return optionalUomCategory.orElseThrow(() -> new UomCategoryNotFoundException(new String[]{uomCategoryId.getValue().toString()}));
    }

    @Override
    public PageInfo<UomCategory> findUomCategories(int page, int size, String sortAttribute, Direction direction) {
        return uomCategoryRepository.findAll(page, size, sortAttribute, direction);
    }

    @Override
    public PageInfo<UomCategory> searchUomCategories(int page, int size, String sortAttribute, Direction direction, Keyword keyword) {
        return uomCategoryRepository.search(page, size, sortAttribute, direction, keyword);
    }

    @Nonnull
    @Override
    public UomCategory updateUomCategory(@Nonnull UomCategoryId uomCategoryId, @Nonnull UomCategory uomCategoryToUpdate) {
        Optional<UomCategory> optionalUomCategory = uomCategoryRepository.findById(uomCategoryId);
        if (optionalUomCategory.isPresent()) {
            validateUomCategory(uomCategoryToUpdate);
            return uomCategoryRepository.updateUomCategory(optionalUomCategory.get(), uomCategoryToUpdate);
        }
        throw new UomCategoryNotFoundException(new String[]{uomCategoryId.getValue().toString()});
    }

    private void validateUomCategory(UomCategory uomCategory) {
        if(uomCategory.getParentUomCategoryId() != null && !uomCategoryRepository.existsById(uomCategory.getParentUomCategoryId())) {
            throw new UomParentCategoryNotFoundException(new String[] {uomCategory.getParentUomCategoryId().getValue().toString()});
        }
        //We check a unique name in case of creation. At this step, id it's null
        if(uomCategory.getId() == null && uomCategoryRepository.existsByName(uomCategory.getName())) {
            throw new UomCategoryNameConflictException(new String[] {uomCategory.getName().getText().getValue()});
        }

        //We check a unique name in case of update. At this step, id it's not null
        Optional<UomCategory> existingUomCategoryByName = uomCategoryRepository.findByName(uomCategory.getName());
        if(existingUomCategoryByName.isPresent() && uomCategory.getId() != null
                && !existingUomCategoryByName.get().getId().equals(uomCategory.getId())) {
            throw new UomCategoryNameConflictException(new String[] {uomCategory.getName().getText().getValue()});
        }
    }
}
