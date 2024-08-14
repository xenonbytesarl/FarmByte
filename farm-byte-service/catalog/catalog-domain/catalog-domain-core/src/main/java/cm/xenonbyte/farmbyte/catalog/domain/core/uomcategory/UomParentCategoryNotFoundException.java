package cm.xenonbyte.farmbyte.catalog.domain.core.uomcategory;

import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.UOM_PARENT_CATEGORY_NOT_FOUND_EXCEPTION;

/**
 * @author bamk
 * @version 1.0
 * @since 14/08/2024
 */
public final class UomParentCategoryNotFoundException extends UomCategoryException {

    public UomParentCategoryNotFoundException(Object[] args) {
        super(UOM_PARENT_CATEGORY_NOT_FOUND_EXCEPTION, args);

    }
}
