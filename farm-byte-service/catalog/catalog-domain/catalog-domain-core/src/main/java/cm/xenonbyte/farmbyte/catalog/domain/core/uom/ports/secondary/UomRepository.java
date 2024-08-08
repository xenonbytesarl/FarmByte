package cm.xenonbyte.farmbyte.catalog.domain.core.uom.ports.secondary;

import cm.xenonbyte.farmbyte.catalog.domain.core.uom.entity.Uom;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.vo.Name;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.vo.UomCategoryId;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.vo.UomType;

/**
 * @author bamk
 * @version 1.0
 * @since 06/08/2024
 */
public interface UomRepository {

    boolean existsByCategoryIdAndUomTypeAndActive(UomCategoryId uomCategoryId, UomType uomType);

    Uom save(Uom uom);

    boolean existsByNameAndCategoryAndActive(Name name, UomCategoryId uomCategoryId);
}
