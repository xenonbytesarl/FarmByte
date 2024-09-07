package cm.xenonbyte.farmbyte.catalog.domain.core.uom.ports.secondary;

import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategory;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategoryId;
import cm.xenonbyte.farmbyte.common.domain.vo.Keyword;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.PageInfo;
import cm.xenonbyte.farmbyte.common.domain.vo.Direction;
import jakarta.annotation.Nonnull;

import java.util.Optional;

/**
 * @author bamk
 * @version 1.0
 * @since 14/08/2024
 */
public interface UomCategoryRepository {
    Boolean existsByName(@Nonnull Name name);

    UomCategory save(@Nonnull UomCategory uomCategory);

    Boolean existsById(UomCategoryId parentCategoryId);

    Optional<UomCategory> findById(UomCategoryId uomCategoryId);

    PageInfo<UomCategory> findAll(@Nonnull Integer page, @Nonnull Integer size, @Nonnull String sortAttribute, @Nonnull Direction direction);

    PageInfo<UomCategory> search(int page, int size, String sortAttribute, Direction direction, Keyword keyword);

    @Nonnull UomCategory updateUomCategory(UomCategory oldUomcategory, UomCategory newUomCategory);

    Optional<UomCategory> findByName(Name name);
}
