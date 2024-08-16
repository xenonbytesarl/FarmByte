package cm.xenonbyte.farmbyte.catalog.domain.core.product;

import cm.xenonbyte.farmbyte.common.domain.exception.BaseDomainConflictException;

import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_CATEGORY_NAME_CONFLICT_EXCEPTION;

/**
 * @author bamk
 * @version 1.0
 * @since 16/08/2024
 */
public final class ProductCategoryNameConflictException extends BaseDomainConflictException {

    public ProductCategoryNameConflictException(Object[] args) {
        super(PRODUCT_CATEGORY_NAME_CONFLICT_EXCEPTION, args);
    }
}
