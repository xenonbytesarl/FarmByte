package cm.xenonbyte.farmbyte.catalog.domain.core.uom;

import cm.xenonbyte.farmbyte.common.domain.exception.BaseDomainException;

/**
 * @author bamk
 * @version 1.0
 * @since 06/08/2024
 */
public abstract class UomException extends BaseDomainException {

    public UomException(String message, Object[] args) {
        super(message, args);
    }

    public UomException(String message) {
        super(message);
    }
}
