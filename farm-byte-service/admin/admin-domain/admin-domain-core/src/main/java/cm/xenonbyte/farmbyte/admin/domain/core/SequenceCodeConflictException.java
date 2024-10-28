package cm.xenonbyte.farmbyte.admin.domain.core;

import cm.xenonbyte.farmbyte.common.domain.exception.BaseDomainConflictException;

/**
 * @author bamk
 * @version 1.0
 * @since 28/10/2024
 */
public final class SequenceCodeConflictException extends BaseDomainConflictException {
    public static final String SEQUENCE_CODE_CONFLICT = "SequenceCodeConflictException.1";

    public SequenceCodeConflictException(Object[] args) {
        super(SEQUENCE_CODE_CONFLICT, args);
    }
}
