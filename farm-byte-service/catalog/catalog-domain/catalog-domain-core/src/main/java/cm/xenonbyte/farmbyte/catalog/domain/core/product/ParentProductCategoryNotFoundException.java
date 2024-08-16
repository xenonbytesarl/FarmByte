package cm.xenonbyte.farmbyte.catalog.domain.core.product;

import cm.xenonbyte.farmbyte.common.domain.exception.BaseDomainNotFoundException;

import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PARENT_PRODUCT_CATEGORY_WITH_ID_NOT_FOUND_EXCEPTION;

/**
 * @author bamk
 * @version 1.0
 * @since 16/08/2024
 */
public final class ParentProductCategoryNotFoundException extends BaseDomainNotFoundException {
    public ParentProductCategoryNotFoundException(Object[] args) {
        super(PARENT_PRODUCT_CATEGORY_WITH_ID_NOT_FOUND_EXCEPTION, args);
    }
}
