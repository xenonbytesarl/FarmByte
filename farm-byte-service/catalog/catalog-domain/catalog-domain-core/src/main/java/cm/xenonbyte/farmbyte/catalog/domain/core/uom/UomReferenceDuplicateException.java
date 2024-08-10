package cm.xenonbyte.farmbyte.catalog.domain.core.uom;

import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.UOM_REFERENCE_DUPLICATE_IN_SAME_CATEGORY;

/**
 * @author bamk
 * @version 1.0
 * @since 09/08/2024
 */
public final class UomReferenceDuplicateException extends UomException {

    public UomReferenceDuplicateException() {
        super(UOM_REFERENCE_DUPLICATE_IN_SAME_CATEGORY);
    }
}
