package cm.xenonbyte.farmbyte.catalog.domain.core.uom;

import cm.xenonbyte.farmbyte.common.domain.exception.BaseDomainNotFoundException;

import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.UOM_NOT_FOUND_EXCEPTION;

/**
 * @author bamk
 * @version 1.0
 * @since 02/09/2024
 */
public final class UomNotFoundException extends BaseDomainNotFoundException {

    public UomNotFoundException(Object[] args) {
        super(UOM_NOT_FOUND_EXCEPTION, args);
    }
}
