package cm.xenonbyte.farmbyte.catalog.adapter.data.access.inmemory;

import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategory;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategoryId;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.ports.secondary.UomCategoryRepository;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import jakarta.annotation.Nonnull;

import java.util.HashMap;
import java.util.Map;

/**
 * @author bamk
 * @version 1.0
 * @since 14/08/2024
 */
public final class UomCategoryInMemoryRepositoryAdapter implements UomCategoryRepository {

    private final Map<UomCategoryId, UomCategory> uomCategories = new HashMap<>();

    @Override
    public Boolean existsByName(@Nonnull Name name) {
        return uomCategories.values().stream().anyMatch(uomCategory -> uomCategory.getName().equals(name));
    }

    @Override
    public UomCategory save(@Nonnull UomCategory uomCategory) {
        uomCategories.put(uomCategory.getId(), uomCategory);
        return uomCategory;
    }

    @Override
    public Boolean existsById(UomCategoryId parentCategoryId) {
        return uomCategories.get(parentCategoryId) != null;
    }
}
