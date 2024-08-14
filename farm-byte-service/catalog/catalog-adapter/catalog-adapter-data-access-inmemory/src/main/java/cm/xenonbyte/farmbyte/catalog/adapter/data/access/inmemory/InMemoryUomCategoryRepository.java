package cm.xenonbyte.farmbyte.catalog.adapter.data.access.inmemory;

import cm.xenonbyte.farmbyte.catalog.domain.core.uomcategory.UomCategory;
import cm.xenonbyte.farmbyte.catalog.domain.core.uomcategory.UomCategoryId;
import cm.xenonbyte.farmbyte.catalog.domain.core.uomcategory.ports.secondary.UomCategoryRepository;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import jakarta.annotation.Nonnull;

import java.util.HashMap;
import java.util.Map;

/**
 * @author bamk
 * @version 1.0
 * @since 14/08/2024
 */
public final class InMemoryUomCategoryRepository implements UomCategoryRepository {

    private final Map<UomCategoryId, UomCategory> uomCategories = new HashMap<>();

    @Override
    public Boolean existsByName(Name name) {
        return uomCategories.values().stream().anyMatch(uomCategory -> uomCategory.getName().equals(name));
    }

    @Override
    public UomCategory save(@Nonnull UomCategory uomCategory) {
        uomCategories.put(uomCategory.getId(), uomCategory);
        return uomCategory;
    }

    @Override
    public Boolean existsByParentUomCategoryId(UomCategoryId parentCategoryId) {
        return uomCategories.get(parentCategoryId) != null;
    }
}
