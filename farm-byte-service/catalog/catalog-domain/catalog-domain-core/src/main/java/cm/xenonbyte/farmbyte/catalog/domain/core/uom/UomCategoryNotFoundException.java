package cm.xenonbyte.farmbyte.catalog.domain.core.uom;

import cm.xenonbyte.farmbyte.common.domain.exception.BaseDomainNotFoundException;

import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.UOM_CATEGORY_NOT_FOUND_EXCEPTION;

/**
 * @author bamk
 * @version 1.0
 * @since 21/08/2024
 */
public final class UomCategoryNotFoundException extends BaseDomainNotFoundException {
    public UomCategoryNotFoundException(Object[] args) {
        super(UOM_CATEGORY_NOT_FOUND_EXCEPTION, args);
    }
}
