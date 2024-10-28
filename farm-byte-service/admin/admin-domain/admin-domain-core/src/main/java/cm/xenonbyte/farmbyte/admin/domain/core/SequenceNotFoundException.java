package cm.xenonbyte.farmbyte.admin.domain.core;

import cm.xenonbyte.farmbyte.common.domain.exception.BaseDomainNotFoundException;

/**
 * @author bamk
 * @version 1.0
 * @since 28/10/2024
 */
public final class SequenceNotFoundException extends BaseDomainNotFoundException {
    public static final String SEQUENCE_ID_NOT_FOUND = "SequenceNotFoundException.1";

    public SequenceNotFoundException(Object[] args) {
        super(SEQUENCE_ID_NOT_FOUND, args);
    }
}
