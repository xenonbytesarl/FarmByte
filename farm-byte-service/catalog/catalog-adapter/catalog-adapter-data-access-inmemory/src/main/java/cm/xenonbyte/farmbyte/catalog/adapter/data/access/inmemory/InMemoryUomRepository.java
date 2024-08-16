package cm.xenonbyte.farmbyte.catalog.adapter.data.access.inmemory;

import cm.xenonbyte.farmbyte.catalog.domain.core.uom.Uom;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomId;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomType;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.ports.secondary.UomRepository;
import cm.xenonbyte.farmbyte.common.domain.vo.Active;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategoryId;
import jakarta.annotation.Nonnull;

import java.util.HashMap;
import java.util.Map;

/**
 * @author bamk
 * @version 1.0
 * @since 06/08/2024
 */
public class InMemoryUomRepository implements UomRepository {

    private final Map<UomId, Uom> uomMap = new HashMap<>();

    @Override
    public boolean existsByCategoryIdAndUomTypeAndActive(@Nonnull UomCategoryId uomCategoryId, @Nonnull UomType uomType) {
        return uomMap.values().stream()
                .anyMatch(uom ->
                        uom.getUomCategoryId().equals(uomCategoryId) &&
                        uom.getUomType().equals(uomType) &&
                        uom.getActive().equals(Active.with(true))
                );
    }

    @Override
    public Uom save(@Nonnull Uom uom) {
        uomMap.put(uom.getId(), uom);
        return uom;
    }

    @Override
    public boolean existsByNameAndCategoryAndActive(@Nonnull Name name, @Nonnull UomCategoryId uomCategoryId) {
        return uomMap.values().stream().anyMatch(uom ->
                uom.getName().equals(name) &&
                uom.getUomCategoryId().equals(uomCategoryId) &&
                uom.getActive().equals(Active.with(true))
        );
    }

}
