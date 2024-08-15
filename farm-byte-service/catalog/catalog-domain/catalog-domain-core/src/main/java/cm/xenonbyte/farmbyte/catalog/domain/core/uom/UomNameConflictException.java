package cm.xenonbyte.farmbyte.catalog.domain.core.uom;

import cm.xenonbyte.farmbyte.common.domain.exception.BaseDomainConflictException;

import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.UOM_NAME_CONFLICT_EXCEPTION;

/**
 * @author bamk
 * @version 1.0
 * @since 09/08/2024
 */
public final class UomNameConflictException extends BaseDomainConflictException {

    public UomNameConflictException(Object[] args) {
        super(UOM_NAME_CONFLICT_EXCEPTION, args);
    }
}
