package cm.xenonbyte.farmbyte.catalog.domain.core.uom;

/**
 * @author bamk
 * @version 1.0
 * @since 09/08/2024
 */
public final class UomNameDuplicateException extends UomException {
    public UomNameDuplicateException(String name) {
        super(String.format("An unit of measure with the name '%s' already exists", name));
    }
}
