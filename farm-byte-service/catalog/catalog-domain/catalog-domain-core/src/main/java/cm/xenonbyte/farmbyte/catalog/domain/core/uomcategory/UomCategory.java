package cm.xenonbyte.farmbyte.catalog.domain.core.uomcategory;

import cm.xenonbyte.farmbyte.common.domain.entity.BaseEntity;
import cm.xenonbyte.farmbyte.common.domain.vo.Active;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import jakarta.annotation.Nonnull;

import java.util.Objects;
import java.util.UUID;

import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.NAME_UOM_CATEGORY_IS_REQUIRED;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PARENT_UOM_CATEGORY_ID_IS_REQUIRED;

/**
 * @author bamk
 * @version 1.0
 * @since 14/08/2024
 */
public final class UomCategory  extends BaseEntity<UomCategoryId> {

    private final Name name;
    private UomCategoryId parentUomCategoryId;
    private Active active;

    public UomCategory(@Nonnull Name name) {
        this.name = Objects.requireNonNull(name, NAME_UOM_CATEGORY_IS_REQUIRED);
    }

    public UomCategory(@Nonnull Name name, @Nonnull UomCategoryId parentUomCategoryId) {
        this.name = Objects.requireNonNull(name, NAME_UOM_CATEGORY_IS_REQUIRED);
        this.parentUomCategoryId = Objects.requireNonNull(parentUomCategoryId, PARENT_UOM_CATEGORY_ID_IS_REQUIRED);
    }


    public Name getName() {
        return name;
    }

    public void initiate() {
        setId(new UomCategoryId(UUID.randomUUID()));
        this.active = Active.with(true);
    }

    public Active getActive() {
        return active;
    }

    public UomCategoryId getParentCategoryId() {
        return parentUomCategoryId;
    }
}
