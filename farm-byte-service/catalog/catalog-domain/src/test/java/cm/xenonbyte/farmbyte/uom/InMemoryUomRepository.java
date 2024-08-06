package cm.xenonbyte.farmbyte.uom;

import cm.xenonbyte.farmbyte.uom.entity.Uom;
import cm.xenonbyte.farmbyte.uom.ports.secondary.UomRepository;
import cm.xenonbyte.farmbyte.uom.vo.Active;
import cm.xenonbyte.farmbyte.uom.vo.UomCategoryId;
import cm.xenonbyte.farmbyte.uom.vo.UomType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author bamk
 * @version 1.0
 * @since 06/08/2024
 */
public final class InMemoryUomRepository implements UomRepository {

    private final Map<String, Uom> uomMap = new HashMap<>();

    @Override
    public boolean existsByCategoryIdAndUomTypeAndActive(UomCategoryId uomCategoryId, UomType uomType) {
        return uomMap.values().stream()
                .filter(uom ->
                        uom.getUomCategoryId().equals(uomCategoryId) &&
                        uom.getUomType().equals(uomType) &&
                        uom.getActive().equals(Active.with(true))
                )
                .findFirst()
                .orElse(null) != null;
    }

    @Override
    public void save(Uom uom) {
        uomMap.put(uom.getUomId().getId().toString(), uom);
    }
}
