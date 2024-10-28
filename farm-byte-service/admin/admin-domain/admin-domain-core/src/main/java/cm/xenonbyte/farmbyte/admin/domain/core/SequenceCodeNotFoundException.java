package cm.xenonbyte.farmbyte.admin.domain.core;

import cm.xenonbyte.farmbyte.common.domain.exception.BaseDomainNotFoundException;

/**
 * @author bamk
 * @version 1.0
 * @since 28/10/2024
 */
public final class SequenceCodeNotFoundException extends BaseDomainNotFoundException {
    public static final String SEQUENCE_CODE_NOT_FOUND = "SequenceCodeNotFoundException.1";

    public SequenceCodeNotFoundException(Object[] args) {
        super(SEQUENCE_CODE_NOT_FOUND, args);
    }
}
