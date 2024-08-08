package cm.xenonbyte.farmbyte.catalog.adapter.data.access.inmemory;

import cm.xenonbyte.farmbyte.catalog.domain.core.uom.Uom;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomType;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.ports.secondary.UomRepository;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.Active;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.Name;
import cm.xenonbyte.farmbyte.catalog.domain.core.uomcategory.UomCategoryId;

import java.util.HashMap;
import java.util.Map;

/**
 * @author bamk
 * @version 1.0
 * @since 06/08/2024
 */
public class InMemoryUomRepository implements UomRepository {

    private final Map<String, Uom> uomMap = new HashMap<>();

    @Override
    public boolean existsByCategoryIdAndUomTypeAndActive(UomCategoryId uomCategoryId, UomType uomType) {
        return uomMap.values().stream()
                .anyMatch(uom ->
                        uom.getUomCategoryId().equals(uomCategoryId) &&
                        uom.getUomType().equals(uomType) &&
                        uom.getActive().equals(Active.with(true))
                );
    }

    @Override
    public Uom save(Uom uom) {
        uomMap.put(uom.getUomId().getIdentifier().toString(), uom);
        return uom;
    }

    @Override
    public boolean existsByNameAndCategoryAndActive(Name name, UomCategoryId uomCategoryId) {
        return uomMap.values().stream().anyMatch(uom ->
                uom.getName().equals(name) &&
                uom.getUomCategoryId().equals(uomCategoryId) &&
                uom.getActive().equals(Active.with(true))
        );
    }

}
