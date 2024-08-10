package cm.xenonbyte.farmbyte.catalog.domain.core.uom;

import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.UOM_NAME_DUPLICATE_EXCEPTION;

/**
 * @author bamk
 * @version 1.0
 * @since 09/08/2024
 */
public final class UomNameDuplicateException extends UomException {

    public UomNameDuplicateException(Object[] args) {
        super(UOM_NAME_DUPLICATE_EXCEPTION, args);
    }
}
