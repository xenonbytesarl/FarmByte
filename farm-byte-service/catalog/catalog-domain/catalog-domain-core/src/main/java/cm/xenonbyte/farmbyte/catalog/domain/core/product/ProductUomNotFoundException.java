package cm.xenonbyte.farmbyte.catalog.domain.core.product;

import cm.xenonbyte.farmbyte.common.domain.exception.BaseDomainNotFoundException;

import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_UOM_NOT_FOUND_EXCEPTION;

/**
 * @author bamk
 * @version 1.0
 * @since 26/08/2024
 */
public final class ProductUomNotFoundException extends BaseDomainNotFoundException {
    public ProductUomNotFoundException(Object[] args) {
        super(PRODUCT_UOM_NOT_FOUND_EXCEPTION, args);
    }
}
