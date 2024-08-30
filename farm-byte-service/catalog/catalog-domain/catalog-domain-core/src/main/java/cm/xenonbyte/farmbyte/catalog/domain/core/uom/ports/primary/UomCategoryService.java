package cm.xenonbyte.farmbyte.catalog.domain.core.uom.ports.primary;

import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategory;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategoryId;
import jakarta.annotation.Nonnull;

/**
 * @author bamk
 * @version 1.0
 * @since 14/08/2024
 */
public interface UomCategoryService {
    @Nonnull UomCategory createUomCategory(@Nonnull UomCategory uomCategory);

    UomCategory findUomCategoryById(UomCategoryId uomCategoryId);
}
