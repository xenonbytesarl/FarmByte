package cm.xenonbyte.farmbyte.catalog.domain.core.product;

import cm.xenonbyte.farmbyte.common.domain.exception.BaseDomainNotFoundException;

import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_PARENT_CATEGORY_NOT_FOUND_EXCEPTION;

/**
 * @author bamk
 * @version 1.0
 * @since 16/08/2024
 */
public final class ProductParentCategoryNotFoundException extends BaseDomainNotFoundException {
    public ProductParentCategoryNotFoundException(Object[] args) {
        super(PRODUCT_PARENT_CATEGORY_NOT_FOUND_EXCEPTION, args);
    }
}
