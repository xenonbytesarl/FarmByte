package cm.xenonbyte.farmbyte.catalog.domain.core.uom;

/**
 * @author bamk
 * @version 1.0
 * @since 09/08/2024
 */
public final class UomReferenceDuplicateException extends UomException {
    public UomReferenceDuplicateException() {
        super("We can't have two units of measure with type reference in the same category");
    }
}
