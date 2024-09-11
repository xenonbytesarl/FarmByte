package cm.xenonbyte.farmbyte.catalog.domain.core.uom.ports.secondary;

import cm.xenonbyte.farmbyte.catalog.domain.core.uom.Uom;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategoryId;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomId;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomType;
import cm.xenonbyte.farmbyte.common.domain.vo.Direction;
import cm.xenonbyte.farmbyte.common.domain.vo.Keyword;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.PageInfo;
import jakarta.annotation.Nonnull;

import java.util.Optional;

/**
 * @author bamk
 * @version 1.0
 * @since 06/08/2024
 */
public interface UomRepository {

    boolean existsByCategoryIdAndUomTypeAndActive(@Nonnull UomCategoryId uomCategoryId, @Nonnull UomType uomType);

    @Nonnull Uom save(@Nonnull Uom uom);

    boolean existsByNameAndCategoryAndActive(@Nonnull Name name, @Nonnull UomCategoryId uomCategoryId);

    Optional<Uom> findById(@Nonnull UomId uomId);

    boolean existsById(@Nonnull UomId uomId);

    @Nonnull PageInfo<Uom> findAll(int page, int size, String attribute, @Nonnull Direction direction);

    @Nonnull PageInfo<Uom> search(int page, int size, String attribute, @Nonnull Direction direction, @Nonnull Keyword keyword);

    @Nonnull Uom update(@Nonnull Uom oldUom, @Nonnull Uom newUom);

    Optional<Uom> findByName(@Nonnull Name name);

    Optional<Uom> findByCategoryIdAndUomTypeAndActive(@Nonnull UomCategoryId uomCategoryId, @Nonnull UomType uomType);
}
