package cm.xenonbyte.farmbyte.catalog.domain.core.uom;

import cm.xenonbyte.farmbyte.catalog.domain.core.uom.ports.primary.IUomCategoryService;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.ports.secondary.UomCategoryRepository;
import cm.xenonbyte.farmbyte.common.domain.annotation.DomainService;
import jakarta.annotation.Nonnull;

import java.util.Objects;

/**
 * @author bamk
 * @version 1.0
 * @since 14/08/2024
 */
@DomainService
public final class UomCategoryService implements IUomCategoryService {
    private final UomCategoryRepository uomCategoryRepository;

    public UomCategoryService(final @Nonnull UomCategoryRepository uomCategoryRepository) {
        this.uomCategoryRepository = Objects.requireNonNull(uomCategoryRepository);
    }

    @Nonnull
    @Override
    public UomCategory createUomCategory(@Nonnull UomCategory uomCategory) {
        validateUomCategory(uomCategory);
        uomCategory.initiate();
        return uomCategoryRepository.save(uomCategory);
    }

    private void validateUomCategory(UomCategory uomCategory) {
        if(uomCategory.getParentUomCategoryId() != null && !uomCategoryRepository.existsById(uomCategory.getParentUomCategoryId())) {
            throw new UomParentCategoryNotFoundException(new String[] {uomCategory.getParentUomCategoryId().getValue().toString()});
        }
        if(uomCategoryRepository.existsByName(uomCategory.getName())) {
            throw new UomCategoryNameConflictException(new String[] {uomCategory.getName().getValue()});
        }
    }
}
