package cm.xenonbyte.farmbyte.catalog.domain.core.uom;

import cm.xenonbyte.farmbyte.common.domain.exception.BaseDomainConflictException;

import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.UOM_REFERENCE_CONFLICT_CATEGORY;

/**
 * @author bamk
 * @version 1.0
 * @since 09/08/2024
 */
public final class UomReferenceConflictException extends BaseDomainConflictException {

    public UomReferenceConflictException() {
        super(UOM_REFERENCE_CONFLICT_CATEGORY);
    }
}
