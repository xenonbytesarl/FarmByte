package cm.xenonbyte.farmbyte.catalog.domain.core.product;

import cm.xenonbyte.farmbyte.common.domain.exception.BaseDomainNotFoundException;

import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_NOT_FOUND_EXCEPTION;

/**
 * @author bamk
 * @version 1.0
 * @since 04/09/2024
 */
public final class ProductNotFoundException extends BaseDomainNotFoundException {

    public ProductNotFoundException(Object[] args) {
        super(PRODUCT_NOT_FOUND_EXCEPTION, args);
    }
}
