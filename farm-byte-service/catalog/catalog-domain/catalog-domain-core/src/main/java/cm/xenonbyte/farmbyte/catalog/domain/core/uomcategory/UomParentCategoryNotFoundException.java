package cm.xenonbyte.farmbyte.catalog.domain.core.uomcategory;

import cm.xenonbyte.farmbyte.common.domain.exception.BaseDomainNotFoundException;

import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.UOM_PARENT_CATEGORY_NOT_FOUND_EXCEPTION;

/**
 * @author bamk
 * @version 1.0
 * @since 14/08/2024
 */
public final class UomParentCategoryNotFoundException extends BaseDomainNotFoundException {

    public UomParentCategoryNotFoundException(Object[] args) {
        super(UOM_PARENT_CATEGORY_NOT_FOUND_EXCEPTION, args);

    }
}
