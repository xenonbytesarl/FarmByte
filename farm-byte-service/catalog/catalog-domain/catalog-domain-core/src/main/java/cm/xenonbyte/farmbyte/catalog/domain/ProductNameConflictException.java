package cm.xenonbyte.farmbyte.catalog.domain;

import cm.xenonbyte.farmbyte.common.domain.exception.BaseDomainConflictException;

import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_NAME_CONFLICT_EXCEPTION;

/**
 * @author bamk
 * @version 1.0
 * @since 21/08/2024
 */
public final class ProductNameConflictException extends BaseDomainConflictException {

    public ProductNameConflictException(Object[] args) {
        super(PRODUCT_NAME_CONFLICT_EXCEPTION, args);
    }
}
