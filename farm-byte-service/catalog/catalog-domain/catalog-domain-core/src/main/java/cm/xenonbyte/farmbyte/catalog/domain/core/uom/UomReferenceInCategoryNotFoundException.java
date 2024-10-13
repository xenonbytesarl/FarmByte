package cm.xenonbyte.farmbyte.catalog.domain.core.uom;

import cm.xenonbyte.farmbyte.common.domain.exception.BaseDomainNotFoundException;

import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.UOM_REFERENCE_IN_CATEGORY_NOT_FOUND;

/**
 * @author bamk
 * @version 1.0
 * @since 13/10/2024
 */
public final class UomReferenceInCategoryNotFoundException extends BaseDomainNotFoundException {
    public UomReferenceInCategoryNotFoundException(Object[] args) {
        super(UOM_REFERENCE_IN_CATEGORY_NOT_FOUND, args);
    }
}
