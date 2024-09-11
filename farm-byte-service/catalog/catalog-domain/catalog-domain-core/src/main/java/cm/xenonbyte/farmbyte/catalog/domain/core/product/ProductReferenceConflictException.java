package cm.xenonbyte.farmbyte.catalog.domain.core.product;

import cm.xenonbyte.farmbyte.common.domain.exception.BaseDomainConflictException;

import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_REFERENCE_CONFLICT_EXCEPTION;

/**
 * @author bamk
 * @version 1.0
 * @since 11/09/2024
 */
public final class ProductReferenceConflictException extends BaseDomainConflictException {
    public ProductReferenceConflictException(Object[] args) {
        super(PRODUCT_REFERENCE_CONFLICT_EXCEPTION, args);
    }
}
