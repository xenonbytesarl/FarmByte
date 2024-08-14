package cm.xenonbyte.farmbyte.catalog.domain.core.uomcategory;

import cm.xenonbyte.farmbyte.catalog.domain.core.uomcategory.ports.primary.IUomCategoryService;
import cm.xenonbyte.farmbyte.catalog.domain.core.uomcategory.ports.secondary.UomCategoryRepository;
import jakarta.annotation.Nonnull;

import java.util.Objects;

/**
 * @author bamk
 * @version 1.0
 * @since 14/08/2024
 */
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
        if(uomCategory.getParentCategoryId() != null && !uomCategoryRepository.existsByParentUomCategoryId(uomCategory.getParentCategoryId())) {
            throw new UomParentCategoryNotFoundException(new String[] {uomCategory.getParentCategoryId().getValue().toString()});
        }
        if(uomCategoryRepository.existsByName(uomCategory.getName())) {
            throw new UomCategoryDuplicateNameException(new String[] {uomCategory.getName().getValue()});
        }
    }
}
