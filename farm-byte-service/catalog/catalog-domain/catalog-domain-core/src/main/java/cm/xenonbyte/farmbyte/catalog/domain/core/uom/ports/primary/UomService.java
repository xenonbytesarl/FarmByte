package cm.xenonbyte.farmbyte.catalog.domain.core.uom.ports.primary;

import cm.xenonbyte.farmbyte.catalog.domain.core.uom.Uom;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomId;
import cm.xenonbyte.farmbyte.common.domain.vo.Direction;
import cm.xenonbyte.farmbyte.common.domain.vo.Keyword;
import cm.xenonbyte.farmbyte.common.domain.vo.PageInfo;
import jakarta.annotation.Nonnull;

/**
 * @author bamk
 * @version 1.0
 * @since 06/08/2024
 */
public interface UomService {

    @Nonnull
    Uom createUom(@Nonnull Uom uom);

    @Nonnull Uom findUomById(@Nonnull UomId uomId);

    @Nonnull PageInfo<Uom> findUoms(int page, int size, String attribute, Direction direction);

    @Nonnull PageInfo<Uom> searchUoms(int page, int size, String attribute, Direction direction, @Nonnull Keyword keyword);
}
