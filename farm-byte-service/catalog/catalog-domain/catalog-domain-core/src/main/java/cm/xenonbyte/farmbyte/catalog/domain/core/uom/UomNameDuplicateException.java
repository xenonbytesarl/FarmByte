package cm.xenonbyte.farmbyte.catalog.domain.core.uom;

/**
 * @author bamk
 * @version 1.0
 * @since 09/08/2024
 */
public final class UomNameDuplicateException extends UomException {

    public UomNameDuplicateException(Object[] args) {
        super("UomNameDuplicateException.1", args);
    }
}
