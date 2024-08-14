package cm.xenonbyte.farmbyte.catalog.domain.core.uomcategory;

import cm.xenonbyte.farmbyte.common.domain.exception.BaseDomainException;

/**
 * @author bamk
 * @version 1.0
 * @since 14/08/2024
 */
public class UomCategoryException extends BaseDomainException {
    protected UomCategoryException(String message) {
        super(message);
    }

    protected UomCategoryException(String message, Object[] args) {
        super(message, args);
    }
}
