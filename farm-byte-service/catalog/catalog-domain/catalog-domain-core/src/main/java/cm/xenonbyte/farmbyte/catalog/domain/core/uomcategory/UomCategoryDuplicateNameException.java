package cm.xenonbyte.farmbyte.catalog.domain.core.uomcategory;

import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.UOM_CATEGORY_NAME_DUPLICATE_EXCEPTION;

/**
 * @author bamk
 * @version 1.0
 * @since 14/08/2024
 */
public final class UomCategoryDuplicateNameException extends UomCategoryException {

    public UomCategoryDuplicateNameException(Object[] args) {
        super(UOM_CATEGORY_NAME_DUPLICATE_EXCEPTION, args);

    }
}
