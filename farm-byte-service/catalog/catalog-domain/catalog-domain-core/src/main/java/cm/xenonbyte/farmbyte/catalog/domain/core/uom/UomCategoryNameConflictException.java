package cm.xenonbyte.farmbyte.catalog.domain.core.uom;

import cm.xenonbyte.farmbyte.common.domain.exception.BaseDomainConflictException;

import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.UOM_CATEGORY_NAME_CONFLICT_EXCEPTION;

/**
 * @author bamk
 * @version 1.0
 * @since 14/08/2024
 */
public final class UomCategoryNameConflictException extends BaseDomainConflictException {

    public UomCategoryNameConflictException(Object[] args) {
        super(UOM_CATEGORY_NAME_CONFLICT_EXCEPTION, args);

    }
}
