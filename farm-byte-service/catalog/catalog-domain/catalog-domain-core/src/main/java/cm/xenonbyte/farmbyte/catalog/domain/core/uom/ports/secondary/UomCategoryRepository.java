package cm.xenonbyte.farmbyte.catalog.domain.core.uom.ports.secondary;

import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategory;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategoryId;
import cm.xenonbyte.farmbyte.common.domain.vo.Keyword;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.Page;
import cm.xenonbyte.farmbyte.common.domain.vo.Sort;
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

    Page<UomCategory> findAll(@Nonnull Integer page, @Nonnull Integer size, @Nonnull String sortAttribute, @Nonnull Sort sortDirection);

    Page<UomCategory> findByKeyWord(int page, int size, String sortAttribute, Sort sortDirection, Keyword keyword);
}
