package cm.xenonbyte.farmbyte.catalog.domain.core.uomcategory.ports.secondary;

import cm.xenonbyte.farmbyte.catalog.domain.core.uomcategory.UomCategory;
import cm.xenonbyte.farmbyte.catalog.domain.core.uomcategory.UomCategoryId;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import jakarta.annotation.Nonnull;

/**
 * @author bamk
 * @version 1.0
 * @since 14/08/2024
 */
public interface UomCategoryRepository {
    Boolean existsByName(@Nonnull Name name);

    UomCategory save(@Nonnull UomCategory uomCategory);

    Boolean existsById(UomCategoryId parentCategoryId);
}
