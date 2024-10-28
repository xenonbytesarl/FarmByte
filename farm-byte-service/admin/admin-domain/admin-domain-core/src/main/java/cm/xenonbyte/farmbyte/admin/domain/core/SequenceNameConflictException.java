package cm.xenonbyte.farmbyte.admin.domain.core;

import cm.xenonbyte.farmbyte.common.domain.exception.BaseDomainConflictException;

/**
 * @author bamk
 * @version 1.0
 * @since 28/10/2024
 */
public final class SequenceNameConflictException extends BaseDomainConflictException {
    public static final String SEQUENCE_NAME_CONFLICT = "SequenceNameConflictException.1";

    public SequenceNameConflictException(Object[] args) {
        super(SEQUENCE_NAME_CONFLICT, args);
    }
}
