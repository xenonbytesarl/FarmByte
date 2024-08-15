package cm.xenonbyte.farmbyte.catalog.domain.core.uomcategory;

import cm.xenonbyte.farmbyte.common.domain.exception.BaseDomainConflictException;

import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.UOM_CATEGORY_NAME_CONFLICT_EXCEPTION;

/**
 * @author bamk
 * @version 1.0
 * @since 14/08/2024
 */
public final class UomCategoryConflictNameException extends BaseDomainConflictException {

    public UomCategoryConflictNameException(Object[] args) {
        super(UOM_CATEGORY_NAME_CONFLICT_EXCEPTION, args);

    }
}
