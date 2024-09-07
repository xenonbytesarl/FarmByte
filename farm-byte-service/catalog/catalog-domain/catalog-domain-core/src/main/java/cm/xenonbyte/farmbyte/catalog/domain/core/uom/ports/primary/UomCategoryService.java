package cm.xenonbyte.farmbyte.catalog.domain.core.uom.ports.primary;

import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategory;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategoryId;
import cm.xenonbyte.farmbyte.common.domain.vo.Keyword;
import cm.xenonbyte.farmbyte.common.domain.vo.PageInfo;
import cm.xenonbyte.farmbyte.common.domain.vo.Direction;
import jakarta.annotation.Nonnull;

/**
 * @author bamk
 * @version 1.0
 * @since 14/08/2024
 */
public interface UomCategoryService {
    @Nonnull UomCategory createUomCategory(@Nonnull UomCategory uomCategory);

    UomCategory findUomCategoryById(UomCategoryId uomCategoryId);

    PageInfo<UomCategory> findUomCategories(int page, int size, String sortAttribute, Direction direction);

    PageInfo<UomCategory> searchUomCategories(int page, int size, String sortAttribute, Direction direction, Keyword keyword);

    @Nonnull UomCategory updateUomCategory(@Nonnull UomCategoryId uomCategoryId, @Nonnull UomCategory uomCategoryToUpdate);
}
